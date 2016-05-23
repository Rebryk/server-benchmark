package rebryk.gui;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by rebryk on 23/05/16.
 */
public class Plot {
    private final String name;
    private final List<Integer> x = new LinkedList<>();
    private final List<Integer> y = new LinkedList<>();
    private boolean visible;

    public Plot(final String name) {
        this.name = name;
        this.visible = true;
        clear();
    }

    public String getName() {
        return name;
    }

    public List<Integer> getX() {
        return x;
    }

    public List<Integer> getY() {
        return y;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(final boolean visible) {
        this.visible = visible;
    }

    public void addPoint(final int x, final int y) {
        this.x.add(x);
        this.y.add(y);
    }

    public void clear() {
        x.clear();
        y.clear();
        x.add(0);
        y.add(0);
    }
}
