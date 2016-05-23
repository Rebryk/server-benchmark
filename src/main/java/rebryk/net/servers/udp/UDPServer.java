package rebryk.net.servers.udp;

import rebryk.net.protobuf.ProtobufUtils;
import rebryk.net.protobuf.Protocol;
import rebryk.net.servers.Server;
import rebryk.statistics.Statistics;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by rebryk on 21/05/16.
 */
public abstract class UDPServer extends Server {
    protected final byte[] buffer = new byte[Short.MAX_VALUE];
    protected DatagramSocket socket;

    public UDPServer() {
        super();
    }

    @Override
    public void stop() {
        socket.close();
    }

    protected void handleRequest(final DatagramPacket datagramPacket) throws IOException {
        final Statistics.Interval requestTime = new Statistics.Interval();
        final Protocol.BenchmarkPacket packet = ProtobufUtils.parsePacket(datagramPacket);
        final Statistics.Interval processTime = new Statistics.Interval();
        final Protocol.BenchmarkPacket processedPacket = process(packet);
        processTime.stop();
        ProtobufUtils.sendPacket(socket, datagramPacket.getSocketAddress(), processedPacket, buffer);
        requestTime.stop();

        requestStatistics.add(requestTime.getTime());
        processStatistics.add(processTime.getTime());
    }
}
