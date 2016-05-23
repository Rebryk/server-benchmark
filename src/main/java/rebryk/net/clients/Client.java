package rebryk.net.clients;

/**
 * Created by rebryk on 20/05/16.
 */

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rebryk.net.protobuf.Protocol;
import rebryk.statistics.Statistics;

import java.util.Random;

public abstract class Client {
    protected static final Logger LOGGER = LogManager.getLogger(Client.class.getName());

    protected String host;
    protected int port;

    protected final int arrayLength;
    protected int packetLeftCount;
    protected final long delay;
    protected final Statistics.Interval interval;

    public Client(final String host, final int port,
                  final int packetLeftCount, final int arrayLength, final long delay) {
        this.host = host;
        this.port = port;

        this.packetLeftCount = packetLeftCount;
        this.arrayLength = arrayLength;
        this.delay = delay;
        this.interval = new Statistics.Interval();
    }

    protected Protocol.BenchmarkPacket generatePacket() {
        final Protocol.BenchmarkPacket.Builder builder = Protocol.BenchmarkPacket.newBuilder();
        builder.setCount(arrayLength);
        new Random().ints(arrayLength).forEach(builder::addArray);
        return builder.build();
    }

    public abstract void sendPacket();

    public int getPacketsLeftCount() {
        return packetLeftCount;
    }

    public long getDelay() {
        return delay;
    }

    public long getTime() {
        return interval.getTime();
    }
}
