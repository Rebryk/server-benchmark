package rebryk.net.servers.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by rebryk on 20/05/16.
 */
public class TCPServerMultiThread extends TCPServer {
    @Override
    public void start(final int port) throws IOException {
        serverSocket = new ServerSocket(port);

        while (!serverSocket.isClosed()) {
            try {
                final Socket clientSocket = serverSocket.accept();
                new Thread(() -> {
                    while (!clientSocket.isClosed()) {
                        try {
                            handleRequest(clientSocket);
                        } catch (IOException e) {
                            // 'handleRequest' failed
                            break;
                        }
                    }
                }).start();
            } catch (IOException e) {
                // 'accept' failed
            }
        }
    }
}
