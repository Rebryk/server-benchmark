package rebryk.net.servers.udp;

import rebryk.Settings;
import rebryk.statistics.Statistics;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by rebryk on 21/05/16.
 */
public class UDPServerThreadPool extends UDPServer {
    private final static int THREADS_COUNT = 4;
    private ExecutorService threadPool;

    @Override
    public void start(final int port) throws IOException {
        socket = new DatagramSocket(port);
        threadPool = Executors.newFixedThreadPool(THREADS_COUNT);

        while (!socket.isClosed()) {
            try {
                final DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                final Statistics.Interval requestTime = new Statistics.Interval();
                threadPool.execute(() -> {
                    try {
                        handleRequest(packet, requestTime);
                    } catch (IOException e) {
                        // 'handleRequest' failed
                    }
                });
            } catch (IOException e) {
                // time out
            }
        }
    }

    @Override
    public void stop() {
        threadPool.shutdown();
        super.stop();
    }
}
