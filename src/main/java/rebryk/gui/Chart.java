package rebryk.gui;

/**
 * Created by rebryk on 23/05/16.
 */
public interface Chart {
    void clear();
    void addPoint(int value, long clientTime, long requestTime, long processTime);
}
