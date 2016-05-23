package rebryk.net.servers.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by rebryk on 20/05/16.
 */
public class TCPServerSingleThread extends TCPServer {
    @Override
    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);

        while (!serverSocket.isClosed()) {
            try {
                try (final Socket clientSocket = serverSocket.accept()) {
                    handleRequest(clientSocket);
                }
            } catch (IOException e) {
                // 'accept' or 'handleRequest' failed
            }
        }
    }
}
