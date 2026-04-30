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
import android.system.OsConstants;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 直连
 *
 * 查看：ifconfig tun0
 * 查看：ip route show table all
 *
 * 参考文章：
 * https://blog.csdn.net/qhairen/article/details/48679631
 * https://blog.csdn.net/2301_79966421/article/details/149217411
 * https://blog.csdn.net/weixin_30920513/article/details/96641117
 */
public class RelayVpnService extends VpnService {

    private boolean running = false;

    private ParcelFileDescriptor global;

    // srcHost => srcPort => dstHost => dstPort
    private final Map<String, Map<Integer, Map<String, Map<Integer, RelayWriter>>>> proxy = new ConcurrentHashMap<>();

    public interface RelayWriter {

        boolean isRunning();
        void connect(Network network) throws IOException;
        void sendOuter(byte[] packet, int headerLength, int length) throws IOException;
        void close();

    }

    private Network getWifiNetwork(Context context) {
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
            if (this.running) {
                Log.i("route", "已经打开VPN服务");
                return START_STICKY;
            }
            Log.i("route", "配置VPN服务(relay)");
            final Network wifiNetwork = this.getWifiNetwork(this);
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
                .addAddress(gateway, 24) // VPN网关
                .addRoute(ip, 24)        // 代理网络
//              .addRoute("0.0.0.0", 0)  // 代理全部流量
                .setSession("WiFiProxy")
                .setBlocking(false);     // 是否阻塞网络：tun2socks使用非阻塞；自定义协议使用阻塞；
            new Thread(() -> {
                try {
                    this.global = vpnBuilder
                        .addDisallowedApplication(this.getPackageName()) // 忽略应用本身
                        .establish();
                    if (this.global == null) {
                        Log.i("route", "打开VPN失败");
                        return;
                    }
                    this.running = true;
                    Log.i("route", "配置VPN成功");
                    int length;
                    final byte[] buffer = new byte[65536];
                    try (
                        final FileInputStream input = new FileInputStream(this.global.getFileDescriptor());
                        final FileOutputStream output = new FileOutputStream(this.global.getFileDescriptor());
                    ) {
                        while (this.running) {
                            try {
                                if ((length = input.read(buffer)) > 0) {
                                    this.handlePacket(buffer, length, this, output);
                                }
                            } catch (Exception e) {
                                Log.e("route", "接收VPN数据异常", e);
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.e("route", "配置VPN异常", e);
                } finally {
                    this.stop();
                }
            }, "VPNService-Thread").start();
            return START_STICKY;
        }
    }

    private void stop() {
        this.running = false;
        this.proxy.forEach((srcHost, srcHostMap) -> {
            srcHostMap.forEach((srcPort, srcPortMap) -> {
                srcPortMap.forEach((dstHost, dstHostMap) -> {
                    dstHostMap.forEach((dstPort, writer) -> {
                        writer.close();
                    });
                    dstHostMap.clear();
                });
                srcPortMap.clear();
            });
            srcHostMap.clear();
        });
        this.proxy.clear();
        if (this.global != null) {
            try {
                this.global.close();
            } catch (Exception e) {
                Log.i("route", "关闭VPN异常", e);
            }
            this.global = null;
        }
    }

    private void handlePacket(byte[] buffer, int length, Context context, FileOutputStream output) {
        // 解析IP头
        if (length < 20) {
            Log.w("route", "数据包大小错误：" + length);
            return;
        }
        final int version      = (buffer[0] >> 4) & 0x0F; // 协议版本
        final int headerLength = (buffer[0] & 0x0F) * 4;  // IP头长度
        // 处理IPv4
        if (version != 0x04) {
            Log.w("route", "忽略其他IP协议：" + version);
            return;
        }
        if (length < headerLength + 4) {
            // 两个端口
            Log.w("route", "数据包大小错误：" + length);
            return;
        }
        final int protocol = buffer[9] & 0xFF;
        final String srcHost = bytesToHost(buffer, 12);
        final String dstHost = bytesToHost(buffer, 16);
        final int srcPort = bytesToPort(buffer, headerLength);
        final int dstPort = bytesToPort(buffer, headerLength + 2);
        final Map<Integer, RelayWriter> map = this.proxy
                .computeIfAbsent(srcHost, key -> new ConcurrentHashMap<>())
                .computeIfAbsent(srcPort, key -> new ConcurrentHashMap<>())
                .computeIfAbsent(dstHost, key -> new ConcurrentHashMap<>());
        final RelayWriter relayWriter = map.get(dstPort);
        if(relayWriter != null && relayWriter.isRunning()) {
            try {
                relayWriter.sendOuter(buffer, headerLength, length);
            } catch (Exception e) {
                Log.e("route", String.format("代理发送数据异常：%s:%d -> %s:%d", srcHost, srcPort, dstHost, dstPort), e);
                relayWriter.close();
                map.remove(dstPort);
            }
            return;
        }
        if (protocol == OsConstants.IPPROTO_TCP) {
            Log.i("route", String.format("代理TCP协议：%s:%d -> %s:%d", srcHost, srcPort, dstHost, dstPort));
            final TcpRelay tcpRelay = this.forwardTCP(headerLength, buffer, length, srcHost, srcPort, dstHost, dstPort, context, output);
            map.put(dstPort, tcpRelay);
        } else if (protocol == OsConstants.IPPROTO_UDP) {
            Log.i("route", String.format("代理UDP协议：%s:%d -> %s:%d", srcHost, srcPort, dstHost, dstPort));
            final UdpRelay udpRelay = this.forwardUDP(headerLength, buffer, length, srcHost, srcPort, dstHost, dstPort, context, output);
            map.put(dstPort, udpRelay);
        } else {
            Log.i("route", "忽略不支持的协议：" + protocol);
        }
    }

    private TcpRelay forwardTCP(int headerLength, byte[] buffer, int length, String srcHost, int srcPort, String dstHost, int dstPort, Context context, FileOutputStream output) {
        TcpRelay tcpRelay = null;
        try {
            final Network wifiNetwork = getWifiNetwork(context);
            if (wifiNetwork == null) {
                Log.w("route", String.format("没有连接WIFI代理TCP失败：%s:%d -> %s:%d", srcHost, srcPort, dstHost, dstPort));
                return null;
            }
            tcpRelay = new TcpRelay(Arrays.copyOfRange(buffer, 12, 16), srcHost, srcPort, Arrays.copyOfRange(buffer, 16, 20), dstHost, dstPort, output);
            tcpRelay.connect(wifiNetwork);
            tcpRelay.sendOuter(buffer, headerLength, length);
            new Thread(tcpRelay, "TCP-Thread").start();
            return tcpRelay;
        } catch (Exception e) {
            Log.e("route", String.format("代理TCP协议连接异常：%s:%d -> %s:%d", srcHost, srcPort, dstHost, dstPort), e);
            if(tcpRelay != null) {
                tcpRelay.close();
            }
        }
        return null;
    }

    private static class TcpRelay implements Runnable, RelayWriter {

        private int clientSeq        = 0;
        private int clientDataBytes  = 0;
        private int serverSeq        = new Random().nextInt();
        private int serverDataBytes  = 0;

        private boolean fin = false;
        private boolean syn = false;
        private boolean rst = false;

        private boolean running = true;
        private Socket socket;
        private InputStream socketInput;
        private OutputStream socketOutput;

        private final byte[] srcAddr, dstAddr;
        private final String srcHost, dstHost;
        private final int srcPort, dstPort;
        private final FileOutputStream output;

        private final byte[] pseudo = new byte[65536];
        private final ByteBuffer buffer = ByteBuffer.allocate(65536);

        TcpRelay(byte[] srcAddr, String srcHost, int srcPort, byte[] dstAddr, String dstHost, int dstPort, FileOutputStream output) {
            this.srcAddr = srcAddr;
            this.srcHost = srcHost;
            this.srcPort = srcPort;
            this.dstAddr = dstAddr;
            this.dstHost = dstHost;
            this.dstPort = dstPort;
            this.output  = output;
            this.buffer.order(java.nio.ByteOrder.BIG_ENDIAN);
        }

        @Override
        public boolean isRunning() {
            return this.running;
        }

        @Override
        public void connect(Network network) throws IOException {
            this.socket = network.getSocketFactory().createSocket(this.dstHost, this.dstPort);
            this.socket.setSoTimeout(30000);
            this.socketInput  = this.socket.getInputStream();
            this.socketOutput = this.socket.getOutputStream();
            this.running = true;
            Log.i("route", String.format("打开TCP代理：%s:%d -> %s:%d", this.srcHost, this.srcPort, this.dstHost, this.dstPort));
        }

        @Override
        public void sendOuter(byte[] packet, int headerLength, int length) throws IOException {
            final int tcpHeaderLength = ((packet[headerLength + 12] & 0xF0) >> 4) * 4;
            final int payloadOffset =          headerLength + tcpHeaderLength;
            final int payloadLength = length - headerLength - tcpHeaderLength;
            final byte flags = packet[headerLength + 13];
            final boolean isFin = (flags & 0x01) != 0;  // FIN
            final boolean isSyn = (flags & 0x02) != 0;  // SYN
            final boolean isRst = (flags & 0x04) != 0;  // RST
            final boolean isPsh = (flags & 0x08) != 0;  // PSH
            final boolean isAck = (flags & 0x10) != 0;  // ACK
            final boolean isUrg = (flags & 0x20) != 0;  // URG
            final int seq = bytesToInt(packet, headerLength + 4);
            final int ack = bytesToInt(packet, headerLength + 8);
            Log.d("route", String.format(
                "TCP代理处理标记：%s:%d -> %s:%d = %d %d | %b %b %b %b %b %b",
                this.srcHost, this.srcPort,
                this.dstHost, this.dstPort,
                seq, ack,
                isFin, isSyn, isRst, isPsh, isAck, isUrg
            ));
            if(isAck) {
                if(this.fin) {
                    this.close();
                    return;
                }
            }
            if(isFin && !this.fin) {
                this.fin = true;
                this.clientSeq = seq;
                this.clientDataBytes = 0;
                this.clientDataBytes += 1;
                this.serverDataBytes = 0;
                this.sendInner((byte) 0x11, new byte[0], 0);
                this.serverDataBytes += 1;
//              this.close();
                return;
            }
            // 不要判断是否syn每次都要刷新
            if(isSyn && !this.syn) {
                // 可以改为这里connect可以快速失败不然服务端连接不上容易导致客户端超时失败
                this.syn = true;
                this.clientSeq = seq;
                this.clientDataBytes = 0;
                this.clientDataBytes += 1;
                this.serverDataBytes = 0;
                this.sendInner((byte) 0x12, new byte[0], 0);
                this.serverDataBytes += 1;
            }
            if(isRst) {
                this.rst = true;
                this.close();
                return;
            }
            if(payloadLength <= 0) {
                return;
            }
            this.clientDataBytes += payloadLength;
            Log.d("route", String.format(
                "TCP代理发送数据：%s:%d -> %s:%d = %s",
                this.srcHost, this.srcPort,
                this.dstHost, this.dstPort,
                new String(packet, payloadOffset, payloadLength))
            );
            this.socketOutput.write(packet, payloadOffset, payloadLength);
        }

        public void sendInner(byte flags, byte[] packet, int length) throws IOException {
            final int seqNum = this.serverSeq + this.serverDataBytes;
            final int ackNum = this.clientSeq + this.clientDataBytes;
            if(length > 0) {
                this.serverDataBytes += length;
            }
            this.buffer.clear();
            final int sendLength = this.buildTCPPacket(
                this.buffer,
                this.dstAddr, this.dstPort,
                this.srcAddr, this.srcPort,
                packet, 0, length,
                flags,
                seqNum, ackNum,
                this.pseudo
            );
            Log.d("route", String.format(
                "TCP代理接收数据：%s:%d -> %s:%d = %d %d %d %s",
                this.srcHost, this.srcPort,
                this.dstHost, this.dstPort,
                0, length, sendLength,
                new String(packet, 0, length))
            );
            synchronized (this.output) {
                this.output.write(this.buffer.array(), 0, sendLength);
                this.output.flush();
            }
        }

        @Override
        public void close() {
            Log.i("route", String.format("关闭TCP代理：%s:%d -> %s:%d", this.srcHost, this.srcPort, this.dstHost, this.dstPort));
            if(this.syn) {
                if(!this.fin && !this.rst) {
                    try {
                        this.sendInner((byte) 0x11, new byte[0], 0);
                    } catch (Exception e) {
                        Log.e("route", "TCP发送FIN异常", e);
                    }
                }
            }
            this.running = false;
            if(this.socketInput != null) {
                try {
                    this.socketInput.close();
                    this.socketInput = null;
                } catch (Exception e) {
                    Log.e("route", "关闭TCP代理异常", e);
                }
            }
            if(this.socketOutput != null) {
                try {
                    this.socketOutput.close();
                    this.socketOutput = null;
                } catch (Exception e) {
                    Log.e("route", "关闭TCP代理异常", e);
                }
            }
            try {
                if(this.socket != null) {
                    this.socket.close();
                    this.socket = null;
                }
            } catch (Exception e) {
                Log.e("route", "关闭TCP代理异常", e);
            }
        }

        @Override
        public void run() {
            try {
                int length;
                final byte[] buffer = new byte[65536];
                long heartbeat = System.currentTimeMillis();
                while (this.running) {
                    try {
                        length = this.socketInput.read(buffer);
                        if(length < 0) {
                            break;
                        }
                        if(length == 0) {
                            continue;
                        }
                        heartbeat = System.currentTimeMillis();
                    } catch (SocketTimeoutException e) {
                        if(System.currentTimeMillis() - heartbeat > 120 * 1000) {
                            Log.i("route", "TCP接收超时关闭连接");
                            break;
                        } else {
                            continue;
                        }
                    }
                    this.sendInner((byte) 0x18, buffer, length);
                }
            } catch (Exception e) {
                Log.e("route", String.format("代理TCP接收数据异常：%s:%d -> %s:%d", this.srcHost, this.srcPort, this.dstHost, this.dstPort), e);
            }
            this.close();
        }

        private int buildTCPPacket(ByteBuffer buffer, byte[] srcAddr, int srcPort, byte[] dstAddr, int dstPort, byte[] payload, int offset, int length, byte flags, int seq, int ack, byte[] pseudo) {
            final int tcpHeaderLength = 20;
            final int totalLength = 20 + tcpHeaderLength + length; // IP头 + TCP头(没有选项) + payload
            // IP头
            buffer.put((byte) 0x45); // Version + IHL
            buffer.put((byte) 0);    // DSCP/ECN
            buffer.putShort((short) totalLength);
            buffer.putInt(0);        // ID + Flags + Frag
            buffer.put((byte) 64);   // TTL
            buffer.put((byte) OsConstants.IPPROTO_TCP);
            buffer.putShort((short) 0); // IP checksum
            buffer.put(srcAddr);
            buffer.put(dstAddr);
            // TCP头
            buffer.putShort((short) srcPort);
            buffer.putShort((short) dstPort);
            buffer.putInt(seq); // seq Number
            buffer.putInt(ack); // ack Number
            buffer.putShort((short) (((tcpHeaderLength / 4) << 12) | flags)); // Data Offset | Flags
            buffer.putShort((short) 65535); // Window Size
            buffer.putShort((short) 0);     // TCP checksum
            buffer.putShort((short) 0);     // Urgent Pointer
            // Payload
            if(length > 0) {
                buffer.put(payload, offset, length);
            }
            // 必填：IP checksum
            buffer.putShort(10, computeIPChecksum(buffer.array()));
            // 必选：TCP checksum
            buffer.putShort(36, computeTCPChecksum(pseudo, srcAddr, dstAddr, buffer.array(), 20, 20 + length));
            Log.d("route", String.format("TCP回包：%s:%d -> %s:%d = %s", bytesToHost(srcAddr, 0), srcPort, bytesToHost(dstAddr, 0), dstPort, new String(payload, offset, length)));
            return totalLength;
        }

    }

    private UdpRelay forwardUDP(int headerLength, byte[] buffer, int length, String srcHost, int srcPort, String dstHost, int dstPort, Context context, FileOutputStream output) {
        UdpRelay udpRelay = null;
        try {
            final Network wifiNetwork = getWifiNetwork(context);
            if (wifiNetwork == null) {
                Log.w("route", String.format("没有连接WIFI代理UDP失败：%s:%d -> %s:%d", srcHost, srcPort, dstHost, dstPort));
                return null;
            }
            udpRelay = new UdpRelay(Arrays.copyOfRange(buffer, 12, 16), srcHost, srcPort, Arrays.copyOfRange(buffer, 16, 20), dstHost, dstPort, output);
            udpRelay.connect(wifiNetwork);
            udpRelay.sendOuter(buffer, headerLength, length);
            new Thread(udpRelay, "UDP-Thread").start();
            return udpRelay;
        } catch (Exception e) {
            Log.e("route", String.format("代理UDP协议连接异常：%s:%d -> %s:%d", srcHost, srcPort, dstHost, dstPort), e);
            if (udpRelay != null) {
                udpRelay.close();
            }
        }
        return null;
    }

    private static class UdpRelay implements Runnable, RelayWriter {

        private boolean running = true;
        private DatagramSocket socket;

        private final byte[] srcAddr, dstAddr;
        private final String srcHost, dstHost;
        private final int srcPort, dstPort;
        private final FileOutputStream output;

        private final byte[] pseudo = new byte[65536];
        private final ByteBuffer buffer = ByteBuffer.allocate(65536);

        UdpRelay(byte[] srcAddr, String srcHost, int srcPort, byte[] dstAddr, String dstHost, int dstPort, FileOutputStream output) {
            this.srcAddr = srcAddr;
            this.srcHost = srcHost;
            this.srcPort = srcPort;
            this.dstAddr = dstAddr;
            this.dstHost = dstHost;
            this.dstPort = dstPort;
            this.output  = output;
            this.buffer.order(java.nio.ByteOrder.BIG_ENDIAN);
        }

        @Override
        public boolean isRunning() {
            return this.running;
        }

        @Override
        public void connect(Network network) throws IOException {
            final DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout(30000);
            network.bindSocket(socket);
            socket.connect(new InetSocketAddress(this.dstHost, this.dstPort));
            this.running = true;
            this.socket  = socket;
            Log.i("route", String.format("打开UDP代理：%s:%d -> %s:%d", this.srcHost, this.srcPort, this.dstHost, this.dstPort));
        }

        @Override
        public void sendOuter(byte[] packet, int headerLength, int length) throws IOException {
            final int udpHeaderLength = 8;
            final int payloadOffset =          headerLength + udpHeaderLength;
            final int payloadLength = length - headerLength - udpHeaderLength;
            if(payloadLength <= 0) {
                return;
            }
            Log.d("route", String.format(
                "UDP代理发送数据：%s:%d -> %s:%d = %s",
                this.srcHost, this.srcPort,
                this.dstHost, this.dstPort,
                new String(packet, payloadOffset, payloadLength))
            );
            this.socket.send(new DatagramPacket(packet, payloadOffset, payloadLength));
        }

        public void sendInner(DatagramPacket packet) throws IOException {
            this.buffer.clear();
            final int sendLength = this.buildUDPPacket(
                this.buffer,
//              this.dstAddr, this.dstPort,
                packet.getAddress().getAddress(), packet.getPort(),
                this.srcAddr, this.srcPort,
                packet.getData(), packet.getOffset(), packet.getLength(),
                this.pseudo
            );
            Log.d("route", String.format(
                "UDP代理接收数据：%s:%d -> %s:%d -> %s:%d = %d %d %d %s",
                packet.getAddress(), packet.getPort(),
                this.srcHost, this.srcPort,
                this.dstHost, this.dstPort,
                packet.getOffset(), packet.getLength(), sendLength,
                new String(packet.getData(), packet.getOffset(), packet.getLength()))
            );
            synchronized (this.output) {
                this.output.write(this.buffer.array(), 0, sendLength);
                this.output.flush();
            }
        }

        @Override
        public void close() {
            Log.i("route", String.format("关闭UDP代理：%s:%d -> %s:%d", this.srcHost, this.srcPort, this.dstHost, this.dstPort));
            this.running = false;
            try {
                if(this.socket != null) {
                    this.socket.close();
                    this.socket = null;
                }
            } catch (Exception e) {
                Log.e("route", "关闭UDP代理异常", e);
            }
        }

        @Override
        public void run() {
            try {
                final byte[] buffer = new byte[65536];
                final DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                long heartbeat = System.currentTimeMillis();
                while (this.running) {
                    try {
                        this.socket.receive(packet);
                        heartbeat = System.currentTimeMillis();
                    } catch (SocketTimeoutException e) {
                        if(System.currentTimeMillis() - heartbeat > 120 * 1000) {
                            Log.i("route", "UDP接收超时关闭连接");
                            break;
                        } else {
                            continue;
                        }
                    }
                    this.sendInner(packet);
                }
            } catch (Exception e) {
                Log.e("route", String.format("代理UDP接收数据异常：%s:%d -> %s:%d", this.srcHost, this.srcPort, this.dstHost, this.dstPort), e);
            }
            this.close();
        }

        private int buildUDPPacket(ByteBuffer buffer, byte[] srcAddr, int srcPort, byte[] dstAddr, int dstPort, byte[] payload, int offset, int length, byte[] pseudo) {
            final int totalLength = 20 + 8 + length; // IP头 + UDP头 + payload
            // IP头
            buffer.put((byte) 0x45); // Version + IHL
            buffer.put((byte) 0);    // DSCP/ECN
            buffer.putShort((short) totalLength);
            buffer.putInt(0);        // ID + Flags + Frag
            buffer.put((byte) 64);   // TTL
            buffer.put((byte) OsConstants.IPPROTO_UDP);
            buffer.putShort((short) 0); // IP checksum
            buffer.put(srcAddr);
            buffer.put(dstAddr);
            // UDP头
            buffer.putShort((short) srcPort);
            buffer.putShort((short) dstPort);
            buffer.putShort((short) (8 + length));
            buffer.putShort((short) 0); // UDP checksum
            // Payload
            if(length > 0) {
                buffer.put(payload, offset, length);
            }
            // 必填：IP checksum
            buffer.putShort(10, computeIPChecksum(buffer.array()));
            // 可选：UDP checksum
//          buffer.putShort(26, computeUDPChecksum(pseudo, srcAddr, dstAddr, buffer.array(), 20, 8 + length));
            Log.d("route", String.format("UDP回包：%s:%d -> %s:%d = %s", bytesToHost(srcAddr, 0), srcPort, bytesToHost(dstAddr, 0), dstPort, new String(payload, offset, length)));
            return totalLength;
        }

    }

    private static String bytesToHost(byte[] bytes, int offset) {
        return (bytes[offset    ] & 0xFF) + "." +
               (bytes[offset + 1] & 0xFF) + "." +
               (bytes[offset + 2] & 0xFF) + "." +
               (bytes[offset + 3] & 0xFF);
    }

    private static int bytesToPort(byte[] bytes, int offset) {
        return ((bytes[offset] & 0xFF) << 8) | (bytes[offset + 1] & 0xFF);
    }

    private static int bytesToInt(byte[] buf, int offset) {
        return ((buf[offset    ] & 0xFF) << 24) |
               ((buf[offset + 1] & 0xFF) << 16) |
               ((buf[offset + 2] & 0xFF) <<  8) |
               (buf [offset + 3] & 0xFF);
    }

    private static short checksum(byte[] buffer, int offset, int length) {
        long sum = 0;
        if (length <= 0) {
            return 0;
        }
        final int total = offset + length;
        for (int i = offset; i < total - 1; i += 2) {
            sum += ((buffer[i] & 0xFF) << 8) | (buffer[i + 1] & 0xFF);
        }
        // 奇数补零
        if (length % 2 != 0) {
            sum += ((buffer[total - 1] & 0xFF) << 8);
        }
        while ((sum >> 16) != 0) {
            sum = (sum & 0xFFFF) + (sum >> 16);
        }
        return (short) ~sum;
    }

    private static short computeIPChecksum(byte[] buffer) {
        return checksum(buffer, 0, 20);
    }

    private static short computeTCPChecksum(byte[] pseudo, byte[] srcAddr, byte[] dstAddr, byte[] buffer, int offset, int length) {
        System.arraycopy(srcAddr, 0, pseudo, 0, 4);
        System.arraycopy(dstAddr, 0, pseudo, 4, 4);
        pseudo[8]  = 0;
        pseudo[9]  = (byte) OsConstants.IPPROTO_TCP;
        pseudo[10] = (byte) (length >> 8);
        pseudo[11] = (byte) (length & 0xFF);
        System.arraycopy(buffer, offset, pseudo, 12, length);
        return checksum(pseudo, 0, 12 + length);
    }

    private static short computeUDPChecksum(byte[] pseudo, byte[] srcAddr, byte[] dstAddr, byte[] buffer, int offset, int length) {
        System.arraycopy(srcAddr, 0, pseudo, 0, 4);
        System.arraycopy(dstAddr, 0, pseudo, 4, 4);
        pseudo[8]  = 0;
        pseudo[9]  = (byte) OsConstants.IPPROTO_UDP;
        pseudo[10] = (byte) (length >> 8);
        pseudo[11] = (byte) (length & 0xFF);
        System.arraycopy(buffer, offset, pseudo, 12, length);
        return checksum(pseudo, 0, 12 + length);
    }

}
