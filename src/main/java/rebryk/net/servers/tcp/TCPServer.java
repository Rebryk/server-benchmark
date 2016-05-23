package rebryk.net.servers.tcp;

import rebryk.net.protobuf.ProtobufUtils;
import rebryk.net.protobuf.Protocol;
import rebryk.net.servers.Server;
import rebryk.statistics.Statistics;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by rebryk on 20/05/16.
 */
public abstract class TCPServer extends Server {
    protected ServerSocket serverSocket;

    public TCPServer() {
        super();
    }

    @Override
    public void stop() throws IOException {
        serverSocket.close();
    }

    protected void handleRequest(final Socket socket) throws IOException {
        final byte[] data = ProtobufUtils.receiveBytePacket(socket);
        final Statistics.Interval requestTime = new Statistics.Interval();
        final Protocol.BenchmarkPacket packet = Protocol.BenchmarkPacket.parseFrom(data);
        final Statistics.Interval processTime = new Statistics.Interval();
        final Protocol.BenchmarkPacket processedPacket = process(packet);
        processTime.stop();
        ProtobufUtils.sendPacket(socket, processedPacket);
        requestTime.stop();

        requestStatistics.add(requestTime.getTime());
        processStatistics.add(processTime.getTime());
    }
}
