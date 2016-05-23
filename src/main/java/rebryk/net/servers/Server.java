package rebryk.net.servers;

import rebryk.net.protobuf.Protocol;
import rebryk.statistics.Statistics;

import java.io.IOException;
/**
 * Created by rebryk on 20/05/16.
 */

public abstract class Server {
    protected final Statistics requestStatistics;
    protected final Statistics processStatistics;

    public Server() {
        requestStatistics = new Statistics();
        processStatistics = new Statistics();
    }

    public long getRequestStatistics() {
        return requestStatistics.getAverage();
    }

    public long getProcessStatistics() {
        return processStatistics.getAverage();
    }

    public abstract void start(final int port) throws IOException;

    public abstract void stop() throws IOException;

    protected Protocol.BenchmarkPacket process(final Protocol.BenchmarkPacket packet) {
        final Protocol.BenchmarkPacket.Builder builder = Protocol.BenchmarkPacket.newBuilder();
        builder.setCount(packet.getCount());

        Integer[] list = packet.getArrayList().stream().toArray(Integer[]::new);
        for (int i = 0; i < packet.getCount(); ++i) {
            for (int j = 0; j < packet.getCount(); ++j) {
                if (i < j && list[i] > list[j]) {
                    final Integer tmp = list[i];
                    list[i] = list[j];
                    list[j] = tmp;
                }
            }
        }

        for (final Integer x : list) {
            builder.addArray(x);
        }

        return builder.build();
    }
}
