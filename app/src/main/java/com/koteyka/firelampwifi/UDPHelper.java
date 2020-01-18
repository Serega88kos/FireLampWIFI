package com.koteyka.firelampwifi;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPHelper extends Thread {

    private BroadcastListener listener;
    private Context ctx;
    private DatagramSocket clientSocket;
    private static final int PORT = 8888;


    UDPHelper(Context ctx, BroadcastListener listener) {
        this.listener = listener;
        this.ctx = ctx;
    }

    void send() throws IOException {
        clientSocket = new DatagramSocket();
        clientSocket.setBroadcast(true);
        byte[] sendData = "DISCOVER".getBytes();
        DatagramPacket sendPacket = new DatagramPacket(
                sendData, sendData.length, getBroadcastAddress(), PORT);
        clientSocket.send(sendPacket);
    }

    @Override
    public void run() {
        try {
            byte[] buf = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            sleep(1000);
            clientSocket.receive(packet);
            listener.onReceive(
                    new String(packet.getData(), 0, packet.getLength()),
                    packet.getAddress().getHostAddress());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


//    void end() {
//        clientSocket.close();
//    }

    public interface BroadcastListener {
        void onReceive(String msg, String ip);
    }

    private InetAddress getBroadcastAddress() throws IOException {
        WifiManager wifi = (WifiManager) ctx.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcp = wifi.getDhcpInfo();
        if(dhcp == null)
            return InetAddress.getByName("255.255.255.255");
        int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
        byte[] quads = new byte[4];
        for (int k = 0; k < 4; k++)
            quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
        return InetAddress.getByAddress(quads);
    }
}
