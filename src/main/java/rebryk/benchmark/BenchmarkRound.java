package rebryk.benchmark;


import rebryk.net.clients.Client;
import rebryk.statistics.Statistics;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * Created by rebryk on 20/05/16.
 */
public class BenchmarkRound {
    private static final int THREADS_COUNT = 3;

    private final int clientsCount;
    private final ScheduledExecutorService threadPool;
    private final AtomicInteger packetsLeftCount;
    private final Statistics clientStatistics;

    public BenchmarkRound(final int clientsCount) {
        this.clientsCount = clientsCount;
        this.threadPool = Executors.newScheduledThreadPool(THREADS_COUNT);
        this.packetsLeftCount = new AtomicInteger(0);
        this.clientStatistics = new Statistics();
    }

    public int getPacketsLeftCount() {
        return packetsLeftCount.get();
    }

    public long getTime() {
        return clientStatistics.getAverage();
    }

    public void run(final Supplier<Client> clientFactory) {
        for (int i = 0; i < clientsCount; ++i) {
            final Client client = clientFactory.get();
            packetsLeftCount.getAndAdd(client.getPacketsLeftCount());
            threadPool.schedule(() -> handle(client), 0, TimeUnit.MILLISECONDS);
        }
    }

    public void finish() {
        threadPool.shutdown();
    }

    private void handle(final Client client) {
        client.sendPacket();
        packetsLeftCount.getAndDecrement();
        if (client.getPacketsLeftCount() > 0) {
            threadPool.schedule(() -> handle(client), client.getDelay(), TimeUnit.MILLISECONDS);
        } else {
            clientStatistics.add(client.getTime());
        }
    }
}
