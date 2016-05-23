package rebryk.net.clients.tcp;

import rebryk.net.clients.Client;
import rebryk.net.protobuf.ProtobufUtils;
import rebryk.net.protobuf.Protocol;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by rebryk on 20/05/16.
 */
public class TCPReClient extends Client {
    public TCPReClient(final String host, final int port,
                     final int packetLeftCount, final int arrayLength, final long delay) {
        super(host, port, packetLeftCount, arrayLength, delay);
    }

    @Override
    public void sendPacket() {
        try {
            try (final Socket socket = new Socket(host, port)) {
                ProtobufUtils.sendPacket(socket, generatePacket());
                final Protocol.BenchmarkPacket packet = ProtobufUtils.receivePacket(socket); //TODO: validate result
                if (packet.getCount() != arrayLength) {
                    LOGGER.error("got bad response!");
                }
            }
        } catch (IOException e) {
            LOGGER.debug("failed to send packet!");
            e.printStackTrace();
        }

        if (--packetLeftCount == 0) {
            interval.stop();
        }
    }
}
