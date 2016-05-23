package rebryk.gui;

import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.style.Styler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by rebryk on 23/05/16.
 */
public class ChartPanel extends JPanel implements Chart {
    private final XChartPanel<XYChart> chartPanel;
    private final Plot clientTime = new Plot("Client time");
    private final Plot requestTime = new Plot("Request time");
    private final Plot processTime = new Plot("Process time");


    public ChartPanel() {
        chartPanel = new XChartPanel<>(new XYChart(400, 400, Styler.ChartTheme.Matlab));
        chartPanel.getChart().addSeries(clientTime.getName(), clientTime.getX(), clientTime.getY(), null);
        chartPanel.getChart().addSeries(requestTime.getName(), requestTime.getX(), requestTime.getY(), null);
        chartPanel.getChart().addSeries(processTime.getName(), processTime.getX(), processTime.getY(), null);

        setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.PAGE_START;
        c.weighty = 1;
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 0;

        final Box settingsBox = Box.createHorizontalBox();

        final JCheckBox clientTimeCheckbox = new JCheckBox(clientTime.getName(), true);
        settingsBox.add(clientTimeCheckbox);
        clientTimeCheckbox.addItemListener(getItemListener(clientTime));

        final JCheckBox requestTimeCheckbox = new JCheckBox(requestTime.getName(), true);
         settingsBox.add(requestTimeCheckbox);
        requestTimeCheckbox.addItemListener(getItemListener(requestTime));

        final JCheckBox serverTimeCheckbox = new JCheckBox(processTime.getName(), true);
        settingsBox.add(serverTimeCheckbox);
        serverTimeCheckbox.addItemListener(getItemListener(processTime));

        add(settingsBox);

        c.gridy = 1;
        c.fill = GridBagConstraints.BOTH;
        add(chartPanel, c);
    }

    @Override
    public void clear() {
        clientTime.clear();
        requestTime.clear();
        processTime.clear();
        updatePlots();
    }

    @Override
    public void addPoint(int value, long clientTime, long requestTime, long serverTime) {
        this.clientTime.addPoint(value, (int) clientTime);
        this.requestTime.addPoint(value, (int) requestTime);
        this.processTime.addPoint(value, (int) serverTime);
        updatePlots();
    }

    private void updatePlots() {
        updatePlot(clientTime);
        updatePlot(requestTime);
        updatePlot(processTime);
    }

    private void updatePlot(Plot plot) {
        if (plot.isVisible()) {
            chartPanel.updateSeries(plot.getName(), plot.getX(), plot.getY(), null);
        } else {
            chartPanel.updateSeries(plot.getName(), new LinkedList<>(), new LinkedList<>(), null);
        }
    }

    private ItemListener getItemListener(final Plot plot) {
        return (e) -> {
            plot.setVisible(e.getStateChange() == ItemEvent.SELECTED);
            updatePlots();
            chartPanel.repaint();
        };
    }
}
