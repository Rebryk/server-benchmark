package rebryk.net.servers.udp;

import rebryk.Settings;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by rebryk on 21/05/16.
 */
public class UDPServerMultithread extends UDPServer {
    @Override
    public void start(final int port) throws IOException {
        socket = new DatagramSocket(port);

        while (!socket.isClosed()) {
            try {
                final DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                new Thread(() -> {
                    try {
                        handleRequest(packet);
                    } catch (IOException e) {
                        // 'handleRequest' failed
                    }
                }).start();
            } catch (IOException e) {
                // time out
            }
        }
    }
}
