package rebryk.net.clients.udp;

import rebryk.Settings;
import rebryk.net.clients.Client;
import rebryk.net.protobuf.ProtobufUtils;
import rebryk.net.protobuf.Protocol;
import rebryk.statistics.Statistics;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

/**
 * Created by rebryk on 21/05/16.
 */
public class UDPClient extends Client {
    private final InetSocketAddress serverAddress;
    private final byte[] buffer = new byte[Short.MAX_VALUE];
    private DatagramSocket socket;

    public UDPClient(final String host, final int port,
                     final int packetLeftCount, final int arrayLength, final long delay) {
        super(host, port, packetLeftCount, arrayLength, delay);
        serverAddress = new InetSocketAddress(host, port);
    }

    @Override
    public void sendPacket() {
        try {
            if (socket == null) {
                socket = new DatagramSocket();
                socket.setSoTimeout(Settings.UDP_PACKET_TIMEOUT);
            }

            extraInterval.start();
            ProtobufUtils.sendPacket(socket, serverAddress, generatePacket(), buffer);
            final Protocol.BenchmarkPacket packet = ProtobufUtils.receivePacket(socket, buffer);
            if (packet.getCount() != arrayLength) {
                LOGGER.error("got bad response!");
            }
        } catch (IOException e) {
            extraInterval.stop();
            LOGGER.debug("failed to send packet!");
        }

        if (--packetLeftCount == 0) {
            interval.stop();
            socket.close();
        }
    }
}
