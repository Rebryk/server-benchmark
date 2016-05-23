package rebryk.net.clients.tcp;

import java.io.IOException;
import java.net.Socket;

import rebryk.net.clients.Client;
import rebryk.net.protobuf.ProtobufUtils;
import rebryk.net.protobuf.Protocol;

/**
 * Created by rebryk on 20/05/16.
 */
public class TCPClient extends Client {
    private Socket socket;

    public TCPClient(final String host, final int port,
                     final int packetLeftCount, final int arrayLength, final long delay) {
        super(host, port, packetLeftCount, arrayLength, delay);
    }

    @Override
    public void sendPacket() {
        try {
            if (socket == null) {
                socket = new Socket(host, port);
            }
            ProtobufUtils.sendPacket(socket, generatePacket());
            final Protocol.BenchmarkPacket packet = ProtobufUtils.receivePacket(socket);
            if (packet.getCount() != arrayLength) {
                LOGGER.error("got bad response!");
            }
        } catch (IOException e) {
            LOGGER.error("failed to send packet!");
            e.printStackTrace();
        }

        if (--packetLeftCount == 0) {
            interval.stop();
            try {
                socket.close();
            } catch (IOException e) {
                LOGGER.warn("failed to close socket");
            }
        }
    }
}
