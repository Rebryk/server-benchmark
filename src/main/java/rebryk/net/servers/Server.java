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

        //packet.getArrayList().stream().map(x -> x.toString()).sorted().map(x -> Integer.parseInt(x)).forEach(builder::addArray);

        // very slow sort
        Integer[] list = new Integer[packet.getCount()];
        for (int k = 0; k < packet.getCount(); ++k) {
            list = packet.getArrayList().stream().sorted().toArray(Integer[]::new);
            for (int i = 0; i < packet.getCount(); ++i) {
                for (int j = i + 1; j < packet.getCount(); ++j) {
                    if (list[i] < list[j]) {
                        final Integer tmp = list[i];
                        list[i] = list[j];
                        list[j] = tmp;
                    }
                }
            }
        }

        for (final Integer x : list) {
            builder.addArray(x);
        }

        return builder.build();
    }
}
