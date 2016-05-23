package rebryk.statistics;

/**
 * Created by rebryk on 20/05/16.
 */
public class ValueInterval {
    private final int from;
    private final int to;
    private final int step;
    private int value;

    public ValueInterval(final int from, final int to, final int step) {
        this.from = from;
        this.value = from;
        this.to = to;
        this.step = step;
    }

    public boolean next() {
        if (step > 0 && value + step <= to) {
            value += step;
            return true;
        }
        return false;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public int getStep() {
        return step;
    }

    public int getValue() {
        return value;
    }

    public void reset() {
        value = from;
    }
}
