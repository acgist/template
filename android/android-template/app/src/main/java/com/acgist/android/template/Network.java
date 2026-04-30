package com.acgist.android.template;

import android.content.Context;
import android.util.Log;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Network {

    public static void test(Context context) {
        new Thread(() -> {
//            tcp();
//            udp();
            http();
        }).start();
    }

    private static void tcp() {
        try {
            final Socket socket = new Socket();
            socket.connect(new InetSocketAddress("192.168.50.208", 8888));
            final InputStream input = socket.getInputStream();
            final OutputStream output = socket.getOutputStream();
            new Thread(() -> {
                final byte[] bytes = new byte[1024];
                while(socket.isConnected()) {
                    try {
                        final int length = input.read(bytes);
                        if(length < 0) {
                            break;
                        }
                        Log.i(Network.class.getSimpleName(), "TCP接收：" + new String(bytes, 0, length, "utf-8"));
                    } catch (Exception e) {
                        Log.e(Network.class.getSimpleName(), "接收异常", e);
                    }
                }
            }).start();
//            output.write("qiao god nb!!!".getBytes());
//            output.flush();
            while(true) {
                final byte[] bytes = "qiao god nb !!!!".getBytes();
                output.write(bytes);
                output.flush();
                Log.i(Network.class.getSimpleName(), "发送：" + new String(bytes));
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            Log.e(Network.class.getSimpleName(), "网络异常", e);
        }
    }

    private static void udp() {
        try {
            DatagramSocket socket = new DatagramSocket();
            new Thread(() -> {
                final byte[] bytes = new byte[1024];
                final DatagramPacket packet = new DatagramPacket(bytes, 1024);
                while(true) {
                    try {
                        socket.receive(packet);
                        Log.i(Network.class.getSimpleName(), "UDP接收：" + new String(packet.getData(), 0, packet.getLength(), "utf-8"));
                    } catch (Exception e) {
                        Log.e(Network.class.getSimpleName(), "接收异常", e);
                    }
                }
            }).start();
//                final byte[] bytes = "qiao god nb !!!".getBytes();
//                final DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
//                packet.setPort(1080);
//                packet.setAddress(InetAddress.getByName("192.168.50.208"));
//                socket.send(packet);
//                Log.i(Network.class.getSimpleName(), "发送：" + new String(bytes));
            while(true) {
                final byte[] bytes = "qiao god nb !!!".getBytes();
                final DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
                packet.setPort(1080);
                packet.setAddress(InetAddress.getByName("192.168.50.208"));
                socket.send(packet);
                Log.i(Network.class.getSimpleName(), "发送：" + new String(bytes));
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            Log.e(Network.class.getSimpleName(), "网络异常", e);
        }
    }

    private static void http() {
        HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) URI.create("http://192.168.50.208:8080").toURL().openConnection();
//            https(connection);
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(10000);
            connection.connect();
            StringBuilder builder = new StringBuilder();
            try(var input = connection.getInputStream()) {
                int length = 0;
                byte[] buffer = new byte[1024];
                while((length = input.read(buffer)) >= 0) {
                    builder.append(new String(buffer, 0, length));
                }
            }
            Log.i(Network.class.getSimpleName(), "网卡 访问 http://192.168.50.208:8080：" + builder.length());
        } catch (Exception e) {
            Log.i(Network.class.getSimpleName(), "网卡 访问 http://192.168.50.208:8080 失败", e);
        } finally {
            connection = null;
        }
//        try {
//            connection = (HttpsURLConnection) URI.create("https://www.acgist.com").toURL().openConnection();
//            connection.setReadTimeout(1000);
//            connection.setConnectTimeout(1000);
//            connection.connect();
//            StringBuilder builder = new StringBuilder();
//            try(var input = connection.getInputStream()) {
//                int length = 0;
//                byte[] buffer = new byte[1024];
//                while((length = input.read(buffer)) >= 0) {
//                    builder.append(new String(buffer, 0, length));
//                }
//            }
//            Log.i(Network.class.getSimpleName(), "网卡 访问 https://www.acgist.com：" + builder.length());
//        } catch (Exception e) {
//            Log.i(Network.class.getSimpleName(), "网卡 访问 https://www.acgist.com 失败", e);
//        } finally {
//            connection = null;
//        }
    }

    private static void https(HttpsURLConnection connection) throws KeyManagementException, NoSuchAlgorithmException {
        final TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {}

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {}

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }
        };
        final SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        connection.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        });
        connection.setSSLSocketFactory(sslContext.getSocketFactory());
    }

}
