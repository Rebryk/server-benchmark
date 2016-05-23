package rebryk.gui;

import rebryk.benchmark.BenchmarkClient;
import rebryk.benchmark.BenchmarkServer;
import rebryk.statistics.ValueInterval;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by rebryk on 22/05/16.
 */
public final class BenchmarkGUI {
    private final static String[] SERVERS_LIST = {"TCP_SINGLE_THREAD", "TCP_MULTI_THREAD", "TCP_THREAD_POOL",
            "TCP_NIO", "UDP_MULTI_THREAD", "UDP_THREAD_POOL", "ALL"};

    private static JFrame frame;

    private static JTextField serverIP;
    private static JComboBox serversList;
    private static JButton buttonRun;

    private static JTable table;
    private static ChartPanel chartPanel;

    private static BenchmarkClient benchmarkClient;
    private static BenchmarkServer benchmarkServer;

    private BenchmarkGUI() {}

    public static void main(String[] args) {
        int confirmed = JOptionPane.showConfirmDialog(null,
                "Do you want to run client?", "Benchmark",
                JOptionPane.YES_NO_OPTION);

        if (confirmed == JOptionPane.YES_OPTION) {
            buildClientUI();
        } else {
            buildServerUI();
            try {
                benchmarkServer = new BenchmarkServer();
                benchmarkServer.start();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Failed to start server!");
            }
        }
    }

    private static void buildServerUI() {
        frame = new JFrame("Benchmark server");
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    if (benchmarkServer != null) {
                        benchmarkServer.stop();
                    }
                } catch (IOException ex) {
                    // failed to stop benchmarkClient
                }
                super.windowClosing(e);
            }
        });
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(200, 60);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());

        final JLabel label = new JLabel("Server is running...");
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("Trebuchet MS", 0, 15));
        frame.add(label, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private static void buildClientUI() {
        frame = new JFrame("Benchmark client");
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    if (benchmarkClient != null) {
                        benchmarkClient.stop();
                    }
                } catch (IOException ex) {
                    // failed to stop benchmarkClient
                }
                super.windowClosing(e);
            }
        });
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(1250, 600);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());
        frame.add(buildSettingsPanel(), BorderLayout.WEST);
        frame.add(buildGraphicPanel(), BorderLayout.EAST);
        frame.setVisible(true);
    }

    private static JToolBar buildToolbar() {
        final JToolBar toolbar = new JToolBar(JToolBar.HORIZONTAL);
        toolbar.setFloatable(false);
        toolbar.setLayout(new FlowLayout(FlowLayout.LEFT));

        final JLabel labelServerIP = new JLabel("IP: ");
        labelServerIP.setVerticalAlignment(SwingConstants.CENTER);
        toolbar.add(labelServerIP);

        serverIP = new JTextField(9);
        serverIP.setText("127.0.0.1");
        toolbar.add(serverIP);

        final JLabel labelServerType = new JLabel("Type: ");
        labelServerType.setVerticalAlignment(SwingConstants.CENTER);
        toolbar.add(labelServerType);

        serversList = new JComboBox(SERVERS_LIST);
        toolbar.add(serversList);

        buttonRun = new JButton("Run");
        buttonRun.setMargin(new Insets(3, 5, 3, 5));
        buttonRun.addActionListener(runBenchmark);
        toolbar.add(buttonRun);

        return toolbar;
    }

    private static boolean checkTestParameters() {
        try {
            InetAddress.getByName(serverIP.getText()).getHostName();
        } catch (UnknownHostException e) {
            JOptionPane.showMessageDialog(frame, "Incorrect sever IP address!");
            return false;
        }

        int changingValuesCount = 0;
        for (int i = 1; i < 4; ++i) {
            final Integer from = (Integer) table.getValueAt(i, 1);
            final Integer to = (Integer) table.getValueAt(i, 2);
            final Integer step = (Integer) table.getValueAt(i, 3);

            if (from > to) {
                JOptionPane.showMessageDialog(frame, "'From' value is bigger than 'To' value.");
                return false;
            }

            if ((from.equals(to) && step != 0) || (!from.equals(to) && step == 0)) {
                JOptionPane.showMessageDialog(frame, "Incorrect 'Step' value");
                return false;
            }

            if (!from.equals(to)) {
                ++changingValuesCount;
            }
        }

        if (changingValuesCount > 1) {
            JOptionPane.showMessageDialog(frame, "It's possible to change just 1 parameter!");
            return false;
        }

        return true;
    }

    private static JPanel buildSettingsPanel() {
        final JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(450, frame.getHeight()));
        panel.setLayout(new BorderLayout());
        panel.add(buildToolbar(), BorderLayout.NORTH);
        table = buildSettingGrid();
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private static JTable buildSettingGrid() {
        final JTable table = new JTable(new AbstractTableModel() {
            private final String[] columnNames = new String[]{"Parameter", "From", "To", "Step"};
            private final String[] parametersNames = new String[]{"Requests count:", "Clients count:", "Array length:", "Delay:"};

            public final Integer[][] parameters = new Integer[][]{
                {0, 50, 0, 0},
                {0, 5, 5, 0},
                {0, 200, 200, 0},
                {0, 10, 10, 0}
            };

            @Override
            public String getColumnName(int column) {
                return columnNames[column];
            }

            @Override
            public int getRowCount() {
                return 4;
            }

            @Override
            public int getColumnCount() {
                return columnNames.length;
            }

            @Override
            public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
                if (columnIndex > 0) {
                    try {
                        final Integer value = Integer.parseInt(aValue.toString());
                        if (value >= 0) {
                            parameters[rowIndex][columnIndex] = value;
                        } else {
                            JOptionPane.showMessageDialog(frame, "Enter a non-negative integer!");
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(frame, "Enter a non-negative integer!");
                    }
                }
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                switch (columnIndex) {
                    case 0:
                        return parametersNames[rowIndex];
                    default:
                        return parameters[rowIndex][columnIndex];
                }
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return columnIndex != 0 && ((rowIndex == 0 && columnIndex == 1) || rowIndex > 0);
            }
        });

        final Font font = new Font("Trebuchet MS", 0, 14);

        table.getTableHeader().setFont(font);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);

        table.setColumnSelectionAllowed(false);
        table.setRowSelectionAllowed(false);
        table.getColumnModel().getColumn(0).setMinWidth(130);
        table.setRowHeight(20);
        table.setFont(font);

        table.getColumnModel().getColumn(1).setCellRenderer(new EditTextRender());
        table.getColumnModel().getColumn(2).setCellRenderer(new EditTextRender());
        table.getColumnModel().getColumn(3).setCellRenderer(new EditTextRender());

        table.setDefaultRenderer( Object.class, new BorderLessTableCellRenderer() );
        return table;
    }

    private static JPanel buildGraphicPanel() {
        chartPanel = new ChartPanel();
        chartPanel.setPreferredSize(new Dimension(800, frame.getHeight()));
        return chartPanel;
    }


    private static class EditTextRender extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row2, int column) {
            DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(Locale.getDefault());
            decimalFormat.setGroupingUsed(false);

            final JTextField field = new JFormattedTextField(decimalFormat);
            field.setEditable(true);
            field.setText(table.getModel().getValueAt(row2, column).toString());
            field.addActionListener((e) -> table.getModel().setValueAt(((JTextField) e.getSource()).getText(), row2, column));

            if (row2 == 0 && column > 1) {
                return null;
            }

            return field;
        }
    }

    private static class BorderLessTableCellRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected,
                                                       final boolean hasFocus, final int row, final int col) {
            return super.getTableCellRendererComponent(table, value, isSelected, false, row, col);
        }
    }

    private static ActionListener runBenchmark = (e) -> {
        if (!checkTestParameters()) {
            return;
        }

        final String host = serverIP.getText();
        final Integer requestsCount = (Integer) table.getValueAt(0, 1);
        final ValueInterval clientsCount =
                new ValueInterval((Integer) table.getValueAt(1, 1), (Integer) table.getValueAt(1, 2), (Integer) table.getValueAt(1, 3));
        final ValueInterval arrayLength =
                new ValueInterval((Integer) table.getValueAt(2, 1), (Integer) table.getValueAt(2, 2), (Integer) table.getValueAt(2, 3));
        final ValueInterval delay =
                new ValueInterval((Integer) table.getValueAt(3, 1), (Integer) table.getValueAt(3, 2), (Integer) table.getValueAt(3, 3));

        new Thread(() -> {
            try {
                benchmarkClient = new BenchmarkClient(InetAddress.getByName(host).getHostName());
            } catch (IOException ex) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(frame, "Failed to start client!"));
                return;
            }

            SwingUtilities.invokeLater(() -> buttonRun.setEnabled(false));

            if (serversList.getSelectedIndex() < 6) {
                benchmarkClient.test(BenchmarkServer.ServerType.get(serversList.getSelectedIndex()),
                        requestsCount, clientsCount, arrayLength, delay, chartPanel);
            } else {
                for (int i = 0; i < 6; ++i) {
                    clientsCount.reset();
                    arrayLength.reset();
                    delay.reset();
                    benchmarkClient.test(BenchmarkServer.ServerType.get(i),
                            requestsCount, clientsCount, arrayLength, delay, chartPanel);
                }
            }

            try {
                benchmarkClient.stop();
            } catch (IOException ex) {
                // failed to stop benchmarkClient
            }

            SwingUtilities.invokeLater(() -> {
                buttonRun.setEnabled(true);
                JOptionPane.showMessageDialog(frame, "Benchmark has finished!");
            });
        }).start();
    };
}
