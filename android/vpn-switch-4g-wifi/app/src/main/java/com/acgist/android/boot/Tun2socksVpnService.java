package com.acgist.android.boot;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.LinkAddress;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.VpnService;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import androidx.compose.runtime.AtomicInt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * tun2socks
 */
public class Tun2socksVpnService extends VpnService {

    private ParcelFileDescriptor global;

    private static Network getWifiNetwork(Context context) {
        final ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final Network[] networks = manager.getAllNetworks();
        for (final Network network : networks) {
            final NetworkCapabilities capabilities = manager.getNetworkCapabilities(network);
            if (capabilities != null && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return network;
            }
        }
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.stop();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        synchronized (this) {
            if (this.global != null) {
                Log.i("route", "已经打开VPN服务");
                return START_STICKY;
            }
            Log.i("route", "配置VPN服务(tun2socks)");
            final Network wifiNetwork = getWifiNetwork(this);
            final ConnectivityManager manager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (wifiNetwork == null) {
                Log.i("route", "没有连接WIFI网络");
                return START_STICKY;
            }
            final Network active = manager.getActiveNetwork();
            if (active == wifiNetwork) {
                Log.i("route", "已经连接WIFI网络");
                return START_STICKY;
            }
            final LinkProperties linkProperties = manager.getLinkProperties(wifiNetwork);
            if (linkProperties == null) {
                Log.i("route", "没有WIFI配置信息");
                return START_STICKY;
            }
            String ip = linkProperties.getLinkAddresses().stream()
                .map(LinkAddress::getAddress)
                .filter(x -> x instanceof Inet4Address && !x.isLoopbackAddress())
                .map(InetAddress::getHostAddress)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse("192.168.50.1"); // 如果没有IP地址默认代理地址
            ip = ip.substring(0, ip.lastIndexOf('.')) + ".0";
            final String gateway = "192.168.100.1"; // VPN网关
            Log.i("route", String.format("开始代理网络(target->(tun->gateway)->target)：%s -> %s -> %s", ip, gateway, ip));
            final Builder vpnBuilder = new Builder()
                .setMtu(1500)
                .addAddress(gateway, 32) // VPN网关
                .addRoute(ip, 24)        // 代理网络
//              .addRoute("0.0.0.0", 0)  // 代理全部流量
                .setSession("WiFiProxy")
                .setBlocking(false);     // 是否阻塞网络：tun2socks使用非阻塞；自定义协议使用阻塞；
            new Thread(() -> {
                try {
                    this.global = vpnBuilder.addDisallowedApplication(this.getPackageName()).establish();
                    if (this.global == null) {
                        Log.i("route", "打开VPN失败");
                        return;
                    }
                    LocalSocks5Proxy.start(this);
                    final boolean success = MainNative.startTun2Socks(
                        MainNative.LogLevel.INFO,
                        this.global,     // TUN文件描述符
                        1500,            // MTU
                        "127.0.0.1",     // SOCKS5 服务器地址
                        1080,            // SOCKS5 端口
                        gateway,         // IPv4TUN网关IP
                        null,            // IPv6TUN网关IP
                        "255.255.255.0", // 子网掩码
                        true             // UDP
                    );
                    if (success) {
                        Log.i("route", "配置VPN成功");
                    } else {
                        Log.w("route", "配置VPN失败");
                    }
                } catch (Exception e) {
                    Log.e("route", "配置VPN异常", e);
                } finally {
                    this.stop();
                }
            }, "VPN-Thread").start();
            return START_STICKY;
        }
    }

    private void stop() {
        if (this.global != null) {
            try {
                this.global.close();
            } catch (Exception e) {
                Log.i("route", "关闭VPN异常", e);
            }
            this.global = null;
        }
        MainNative.stopTun2Socks();
        LocalSocks5Proxy.stop();
    }

    public static class LocalSocks5Proxy {

        private static boolean running = false;
        private static ServerSocket serverSocket;

        public static void start(Context context) {
            running = true;
            try {
                // 不要使用换回地址
                serverSocket = new ServerSocket(1080, 50, InetAddress.getByName("127.0.0.1"));
//              serverSocket = new ServerSocket(1080, 50, InetAddress.getLoopbackAddress());
                // 接收线程
                new Thread(() -> {
                    while (running) {
                        try {
                            final Socket client = serverSocket.accept();
                            Log.i("route", "接收客户端：" + client.getInetAddress());
                            handleClient(client, context);
                        } catch (Exception e) {
                            Log.e("route", "接收客户端异常", e);
                        }
                    }
                }, "Socks5-Thread").start();
            } catch (Exception e) {
                Log.e("route", "代理网关异常", e);
            }
        }

        public static void stop() {
            running = false;
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (Exception e) {
                    // 忽略
                }
            }
        }

        private static void handleClient(Socket client, Context context) throws IOException, InterruptedException {
            try {
                final InputStream input = client.getInputStream();
                final OutputStream output = client.getOutputStream();
                final byte[] buffer = new byte[65536];
                int length = input.read(buffer);
                if (length < 2 || buffer[0] != 0x05) {
                    // SOCKS5协议
                    return;
                }
                // 仅支持无认证
                output.write(new byte[] { 5, 0 });
                output.flush();
                length = input.read(buffer);
                // 开始处理协议
                if (buffer[1] == 1) {
                    // TCP CONNECT
                    forwardTCP(buffer, length, client, context, output);
                } else if (buffer[1] == 3) {
                    // UDP ASSOCIATE
                    forwardUDP(buffer, length, client, context, output);
                } else {
                    Log.d("route", "忽略其他模式：" + buffer[1]);
                }
            } catch (IOException | InterruptedException e) {
                client.close();
                throw e;
            }
        }

        private static void forwardTCP(byte[] buffer, int length, Socket client, Context context, OutputStream out) throws IOException, InterruptedException {
            try {
                Log.i("route", "TCP代理开始：" + client.getInetAddress());
                int    targetPort;
                String targetHost;
                if (buffer[3] == 0x01) {
                    // IPv4
                    targetHost = String.format(Locale.getDefault(), "%d.%d.%d.%d", buffer[4] & 0xFF, buffer[5] & 0xFF, buffer[6] & 0xFF, buffer[7] & 0xFF);
                    targetPort = ((buffer[8] & 0xFF) << 8) | (buffer[9] & 0xFF);
                } else if (buffer[3] == 0x03) {
                    // 域名
                    final int domainLength = buffer[4] & 0xFF;
                    targetHost = new String(buffer, 5, domainLength, StandardCharsets.US_ASCII);
                    targetPort = ((buffer[5 + domainLength] & 0xFF) << 8) | (buffer[6 + domainLength] & 0xFF);
                } else {
                    // 0x04 = IPv6
                    Log.w("route", "不支持的TCP代理目标地址类型：" + buffer[3]);
                    return;
                }
                final Network wifiNetwork = getWifiNetwork(context);
                if (wifiNetwork == null) {
                    Log.w("route", "没有连接WIFI代理TCP失败");
                    return;
                }
                Log.i("route", String.format("代理TCP连接: %s:%d", targetHost, targetPort));
                final Socket socket = wifiNetwork.getSocketFactory().createSocket(targetHost, targetPort);
                // 回复成功
                out.write(new byte[] {
                    5, 0, 0, 1,
                    0, 0, 0, 0,
                    0, 0
                });
                out.flush();
                // 双向转发
                forwardTCPData(client, socket);
            } finally {
                Log.i("route", "TCP代理结束：" + client.getInetAddress());
                try {
                    client.close();
                } catch (Exception e) {
                    // 忽略
                }
            }
        }

        private static void forwardTCPData(Socket inner, Socket outer) throws InterruptedException {
            // 出现异常直接退出转发
            final Thread sendThread = new Thread(() -> {
                try (
                    final InputStream input = inner.getInputStream();
                    final OutputStream output = outer.getOutputStream();
                ) {
                    int length;
                    final byte[] buffer = new byte[4096];
                    while ((length = input.read(buffer)) >= 0) {
                        output.write(buffer, 0, length);
                        output.flush();
                    }
                } catch (Exception e) {
                    Log.e("route", "TCP代理发送异常：inner -> outer", e);
                }
            }, "TCPSend-Thread");
            final Thread recvThread = new Thread(() -> {
                try (
                    final InputStream input = outer.getInputStream();
                    final OutputStream output = inner.getOutputStream();
                ) {
                    int length;
                    final byte[] buffer = new byte[4096];
                    while ((length = input.read(buffer)) >= 0) {
                        output.write(buffer, 0, length);
                        output.flush();
                    }
                } catch (Exception e) {
                    Log.e("route", "TCP代理接收异常：outer -> inner", e);
                }
            }, "TCPRecv-Thread");
            sendThread.start();
            recvThread.start();
            // 阻塞
            sendThread.join();
            recvThread.join();
        }
    }

    private static void forwardUDP(byte[] buf, int len, Socket client, Context context, OutputStream out) throws IOException, InterruptedException {
        new Thread(() -> {
            DatagramSocket innerSocket = null;
            DatagramSocket outerSocket = null;
            try {
                innerSocket = new DatagramSocket(0, InetAddress.getByName("127.0.0.1"));
                outerSocket = new DatagramSocket();
                Log.i("route", "UDP代理开始：" + client.getInetAddress());
                // 端口
                final int innerPort = innerSocket.getPort();
                final int localPort = innerSocket.getLocalPort();
                // 回复成功
                final byte[] reply = new byte[10];
                reply[0] = 5; // VER
                reply[1] = 0; // REP=success
                reply[2] = 0; // RSV
                reply[3] = 1; // ATYP=IPv4
                // BND.ADDR = 127.0.0.1
                reply[4] = 127;
                reply[5] = 0;
                reply[6] = 0;
                reply[7] = 1;
                // BND.PORT
                reply[8] = (byte) ((localPort >> 8) & 0xFF);
                reply[9] = (byte) (localPort & 0xFF);
                // 回复成功
                out.write(reply);
                out.flush();
                // 双向转发
                forwardUDPData(client, context, innerSocket, outerSocket);
            } catch (Exception e) {
                Log.i("route", "UDP代理异常：" + client.getInetAddress(), e);
            } finally {
                Log.i("route", "UDP代理结束：" + client.getInetAddress());
                try {
                    client.close();
                } catch (Exception e) {
                    // 忽略
                }
                if (innerSocket != null) {
                    innerSocket.close();
                }
                if (outerSocket != null) {
                    outerSocket.close();
                }
            }
        }, "UDP-Thread").start();
    }

    private static void forwardUDPData(Socket client, Context context, DatagramSocket innerSocket, DatagramSocket outerSocket) {
        final AtomicLong heartbeat = new AtomicLong(System.currentTimeMillis());
        final AtomicInt port = new AtomicInt(0);
        final AtomicBoolean running = new AtomicBoolean(true);
        final AtomicReference<InetAddress> host = new AtomicReference<>();
        Log.i("route", String.format("打开UDP代理: 127.0.0.1:%d -> %d", innerSocket.getLocalPort(), outerSocket.getLocalPort()));
        try {
            final Network wifiNetwork = getWifiNetwork(context);
            if (wifiNetwork == null) {
                Log.w("route", "没有连接WIFI代理UDP失败");
                return;
            }
            // 绑定WIFI网络
            wifiNetwork.bindSocket(outerSocket);
            final Thread sendThread = new Thread(() -> {
                int offset = 0;
                int headerLength = 0;
                int targetPort = 0;
                String targetHost = null;
                InetAddress targetAddr = null;
                final byte[] buffer = new byte[65536];
                final DatagramPacket recvPacket = new DatagramPacket(buffer, buffer.length);
                while (running.get()) {
                    try {
                        innerSocket.receive(recvPacket);
                        // 解析SOCKS5 UDP请求头
                        offset = recvPacket.getOffset();
                        final byte frag = buffer[offset + 2];
                        if (frag != 0) {
                            Log.w("route", "不支持的UDP数据");
                            continue;
                        }
                        final byte atyp = buffer[offset + 3];
                        if (atyp == 0x01) {
                            // IPv4
                            targetHost = String.format(Locale.getDefault(), "%d.%d.%d.%d", buffer[offset + 4] & 0xFF, buffer[offset + 5] & 0xFF, buffer[offset + 6] & 0xFF, buffer[offset + 7] & 0xFF);
                            targetAddr = InetAddress.getByName(targetHost);
                            targetPort = ((buffer[offset + 8] & 0xFF) << 8) | (buffer[offset + 9] & 0xFF);
                            headerLength = 10; // RSV(2) + FRAG(1) + ATYP + HOST + PORT = 2 + 1 + 1 + 4 + 2 = 10
                        } else if (atyp == 0x03) {
                            // 域名
                            final int domainLength = buffer[offset + 4] & 0xFF;
                            targetHost = new String(buffer, offset + 5, domainLength, StandardCharsets.US_ASCII);
                            targetAddr = InetAddress.getByName(targetHost);
                            targetPort = ((buffer[offset + 5 + domainLength] & 0xFF) << 8) | (buffer[offset + 6 + domainLength] & 0xFF);
                            headerLength = 7 + domainLength; // RSV(2) + FRAG(1) + ATYP + DOMAIN_LENGTH + DOMAIN.LENGTH() + PORT = 2 + 1 + 1 + 1 + 2 = 7 + DOMAIN.LENGTH()
                        } else if (atyp == 4) {
                            // 0x04 = IPv6
                            Log.w("route", "不支持的UDP代理目标地址类型：" + atyp);
                            continue;
                        }
                        port.set(recvPacket.getPort());
                        host.set(recvPacket.getAddress());
                        // 转发真实目标
                        final DatagramPacket sendPacket = new DatagramPacket(
                            buffer,
                            offset + headerLength,
                            recvPacket.getLength() - offset - headerLength,
                            targetAddr,
                            targetPort
                        );
//                        /*
                        Log.d("route", String.format(
                            "UDP代理发送数据：%s:%d -> %s:%d = %s",
                            recvPacket.getAddress(), recvPacket.getPort(),
                            targetHost, targetPort,
                            new String(buffer, headerLength, recvPacket.getLength() - headerLength))
                        );
//                         */
                        outerSocket.send(sendPacket);
                        heartbeat.set(System.currentTimeMillis());
                    } catch (Exception e) {
                        Log.e("route", "UDP代理发送异常：inner -> outer", e);
                    }
                }
            }, "UDPSend-Thread");
            final Thread recvThread = new Thread(() -> {
                final byte[] buffer = new byte[65536];
                final DatagramPacket recvPacket = new DatagramPacket(buffer, buffer.length);
                final ByteBuffer sendBuffer = ByteBuffer.allocate(65536);
                while (running.get()) {
                    try {
                        outerSocket.receive(recvPacket);
                        if(port.get() == 0 || host.get() == null) {
                            Log.w("route", String.format("UDP代理失败没有地址映射：%s:%d", recvPacket.getAddress().getHostAddress(), recvPacket.getPort()));
                            continue;
                        }
//                        /*
                        Log.d("route", String.format(
                            "UDP代理接收数据：%s:%d -> %s:%d = %s",
                            recvPacket.getAddress(), recvPacket.getPort(),
                            host.get(), port.get(),
                            new String(recvPacket.getData(), 0, recvPacket.getLength()))
                        );
//                        */
                        // 构造SOCKS5 UDP响应包：RSV(2) + FRAG(1) + ATYP + DST.ADDR + DST.PORT + DATA
                        sendBuffer.clear();
                        sendBuffer.putShort((short) 0);                       // RSV
                        sendBuffer.put((byte) 0);                             // FRAG
                        sendBuffer.put((byte) 1);                             // ATYP = IPv4
                        // 注意：需大端序
                        sendBuffer.put(recvPacket.getAddress().getAddress()); // DST.ADDR
                        sendBuffer.putShort((short) recvPacket.getPort());    // DST.PORT
                        // 写入 payload
                        sendBuffer.put(recvPacket.getData(), 0, recvPacket.getLength());
                        final byte[] bytes = sendBuffer.array();
                        final DatagramPacket sendPacket = new DatagramPacket(
                            bytes,
                            0,
                            sendBuffer.position(),
                            host.get(),
                            port.get()
                        );
                        innerSocket.send(sendPacket);
                        heartbeat.set(System.currentTimeMillis());
                    } catch (Exception e) {
                        Log.e("route", "UDP代理接收异常：outer -> inner", e);
                    }
                }
            }, "UDPRecv-Thread");
            sendThread.start();
            recvThread.start();
            // 阻塞
            while (client.isConnected()) {
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    // 忽略
                }
                if(System.currentTimeMillis() - heartbeat.get() >= 120 * 1000) {
                    Log.i("route", "UDP代理超时关闭");
                    running.set(false);
                    break;
                }
            }
            sendThread.join();
            recvThread.join();
        } catch (Exception e) {
            Log.e("route", "代理UDP异常", e);
        } finally {
            running.set(false);
            Log.i("route", String.format("结束UDP代理: 127.0.0.1:%d -> %d", innerSocket.getLocalPort(), outerSocket.getLocalPort()));
        }
    }

}
