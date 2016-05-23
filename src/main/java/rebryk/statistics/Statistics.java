package rebryk.statistics;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by rebryk on 20/05/16.
 */
public class Statistics {
    private final AtomicInteger totalCount;
    private final AtomicLong totalTime;

    public Statistics() {
        totalCount = new AtomicInteger(0);
        totalTime = new AtomicLong(0);
    }

    public void add(final long time) {
        totalCount.getAndAdd(1);
        totalTime.getAndAdd(time);
    }

    public long getAverage() {
        return totalTime.get() / totalCount.get();
    }

    static public class Interval {
        private long start;
        private long time;

        public Interval() {
            this.time = 0;
            start();
        }

        public void start() {
            start = System.currentTimeMillis();
        }

        public void stop() {
            time += System.currentTimeMillis() - start;
        }

        public long getTime() {
            return time;
        }
    }
}
