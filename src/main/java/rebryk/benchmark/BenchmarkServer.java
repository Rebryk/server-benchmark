package rebryk.benchmark;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rebryk.Settings;
import rebryk.net.servers.Server;
import rebryk.net.servers.ServerFactory;

/**
 * Created by rebryk on 20/05/16.
 */
public class BenchmarkServer {
    private static final Logger LOGGER = LogManager.getLogger(BenchmarkServer.class.getName());

    private ServerSocket serverSocket;
    private Server server;

    public void start() throws IOException {
        LOGGER.debug("started");
        serverSocket = new ServerSocket(Settings.BENCHMARK_SERVER_PORT);

        try {
            while (!serverSocket.isClosed()) {
                final Socket clientSocket = serverSocket.accept();
                LOGGER.debug("BenchmarkClient has connected");
                try {
                    while (!clientSocket.isClosed()) {
                        handleRequest(clientSocket);
                    }
                } catch (IOException e) {
                    LOGGER.debug("BenchmarkClient has disconnected");
                }
            }
        } catch (IOException e) {
            LOGGER.debug("stopped");
        }
    }

    public void stop() throws IOException {
        serverSocket.close();
        if (server != null) {
            server.stop();
        }
    }

    private void handleRequest(final Socket socket) throws IOException {
        final DataInputStream input = new DataInputStream(socket.getInputStream());
        final DataOutputStream output = new DataOutputStream(socket.getOutputStream());

        final Command command = Command.get(input.readInt());
        switch (command) {
            case START_SERVER:
                LOGGER.debug("command to start testing server");
                server = ServerFactory.create(ServerType.get(input.readInt()));
                new Thread(() -> {
                    try {
                        server.start(Settings.BENCHMARK_TESTING_SERVER_PORT);
                    } catch (IOException e) {
                        LOGGER.error("failed to start testing server!");
                        e.printStackTrace();
                    }
                }).start();
                break;
            case STOP_SERVER:
                LOGGER.debug("command to stop testing server");
                server.stop();
                output.writeLong(server.getRequestStatistics());
                output.writeLong(server.getProcessStatistics());
                output.flush();
                break;
            default:
                break;
        }
    }

    public static void main(String[] args) {
        try {
            final BenchmarkServer server = new BenchmarkServer();
            server.start();
            server.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public enum Command {
        START_SERVER(0),
        STOP_SERVER(1);

        private final int id;

        Command(final int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static Command get(final int id) {
            return values()[id];
        }
    }

    public enum ServerType {
        TCP_SINGLE_THREAD(0),
        TCP_MULTI_THREAD(1),
        TCP_THREAD_POOL(2),
        TCP_NIO(3),
        UDP_MULTI_THREAD(4),
        UDP_THREAD_POOL(5);

        private final int id;

        ServerType(final int id) {
            this.id = id;
        }
        public int getId() {
            return id;
        }

        public static ServerType get(final int id) {
            return values()[id];
        }
    }
}
