package rebryk.net.servers.tcp;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by rebryk on 20/05/16.
 */
public class TCPServerThreadPool extends TCPServer {
    private ExecutorService threadPool;

    @Override
    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        threadPool = Executors.newCachedThreadPool();

        while (!serverSocket.isClosed()) {
            try {
                final Socket clientSocket = serverSocket.accept();
                threadPool.execute(() -> {
                    while (!clientSocket.isClosed()) {
                        try {
                            handleRequest(clientSocket);
                        } catch (IOException e) {
                            // 'handleRequest' failed
                            break;
                        }
                    }
                });
            } catch (IOException e) {
                // 'accept' failed
            }
        }
    }

    @Override
    public void stop() throws IOException {
        threadPool.shutdown();
        super.stop();
    }
}
