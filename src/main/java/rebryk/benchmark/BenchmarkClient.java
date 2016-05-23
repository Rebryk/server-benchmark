package rebryk.benchmark;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rebryk.Settings;
import rebryk.gui.Chart;
import rebryk.net.clients.ClientFactory;
import rebryk.statistics.ValueInterval;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by rebryk on 20/05/16.
 */
public class BenchmarkClient {
    private static final Logger LOGGER = LogManager.getLogger(BenchmarkClient.class.getName());

    private final String host;
    private final Socket socket;

    public BenchmarkClient(final String host) throws IOException {
        this.host = host;
        this.socket = new Socket(host, Settings.BENCHMARK_SERVER_PORT);
    }

    public void stop() throws IOException {
        socket.close();
    }

    public void test(final BenchmarkServer.ServerType serverType, final int requestsCount,
                     final ValueInterval clientsCount, final ValueInterval arrayLength, final ValueInterval delay, final Chart chart) {
        LOGGER.debug("testing started");
        final List<List<Long>> statistics = new ArrayList<>();

        chart.clear();

        int roundNum = 0;
        do {
            List<Long> roundStatistics = new ArrayList<>();
            try {
                LOGGER.debug("starting round: " + ++roundNum);

                startTestingServer(serverType);
                LOGGER.debug("sent command to start testing server");
                Thread.sleep(100);

                final BenchmarkRound round = new BenchmarkRound(clientsCount.getValue());
                round.run(new ClientFactory(serverType, host, Settings.BENCHMARK_TESTING_SERVER_PORT,
                        requestsCount, arrayLength.getValue(), delay.getValue()));

                LOGGER.debug("benchmark has started");
                while (round.getPacketsLeftCount() > 0) {
                    Thread.sleep(100);
                }
                round.finish();
                LOGGER.debug("benchmark has finished");

                stopTestingServer();
                LOGGER.debug("sent command to stop testing server");

                roundStatistics.add(new Long(clientsCount.getValue()));
                roundStatistics.add(new Long(arrayLength.getValue()));
                roundStatistics.add(new Long(delay.getValue()));
                roundStatistics.add(round.getTime()); // client working average time
                addServerStatistics(roundStatistics);

                chart.addPoint(roundNum, roundStatistics.get(3), roundStatistics.get(4), roundStatistics.get(5));
                LOGGER.debug("successfully finished round");
            } catch (IOException | InterruptedException e) {
                LOGGER.error("round has failed!");
                roundStatistics = null;
                e.printStackTrace();
            }

            if (roundStatistics != null) {
                statistics.add(roundStatistics);
            }
        } while (clientsCount.next() || arrayLength.next() || delay.next());

        try {
            saveStatistics(serverType, requestsCount, clientsCount, arrayLength, delay, statistics);
            LOGGER.debug("saved statistics");
        } catch (IOException e) {
            LOGGER.error("failed to save statistics!");
            e.printStackTrace();
        }
    }

    private void startTestingServer(final BenchmarkServer.ServerType serverType) throws IOException {
        final DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        output.writeInt(BenchmarkServer.Command.START_SERVER.getId());
        output.writeInt(serverType.getId());
        output.flush();
    }

    private void stopTestingServer() throws IOException {
        final DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        output.writeInt(BenchmarkServer.Command.STOP_SERVER.getId());
        output.flush();
    }

    private void addServerStatistics(final List<Long> roundStatistics) throws IOException {
        final DataInputStream input = new DataInputStream(socket.getInputStream());
        roundStatistics.add(input.readLong()); // request average time
        roundStatistics.add(input.readLong()); // process average time
    }

    private void saveStatistics(final BenchmarkServer.ServerType serverType, final int requestsCount,
                                final ValueInterval clientsCount, final ValueInterval arrayLength, final ValueInterval delay,
                                final List<List<Long>> statistics) throws IOException {
        final String timeStamp = getCurrentTimeStamp();

        if (!Files.exists(Paths.get("statistics"))) {
            Files.createDirectory(Paths.get("statistics"));
        }

        Path path = Paths.get("statistics", timeStamp + ".txt");
        if (!path.toFile().exists()) {
            Files.createFile(path);
        }

        final DataOutputStream settingsStream = new DataOutputStream(new FileOutputStream(path.toFile()));
        settingsStream.write(("Server type: " + serverType + "\n").getBytes("UTF-8"));
        settingsStream.write(("Requests count: " + Integer.toString(requestsCount) + "\n").getBytes("UTF-8"));
        settingsStream.write("Clients count: ".getBytes("UTF-8"));
        settingsStream.write(("from = " + clientsCount.getFrom() + ", to = " + clientsCount.getTo() + ", step = " + clientsCount.getStep() + "\n").getBytes("UTF-8"));
        settingsStream.write("Array length: ".getBytes("UTF-8"));
        settingsStream.write(("from = " + arrayLength.getFrom() + ", to = " + arrayLength.getTo() + ", step = " + arrayLength.getStep() + "\n").getBytes("UTF-8"));
        settingsStream.write("Delay: ".getBytes("UTF-8"));
        settingsStream.write(("from = " + delay.getFrom() + ", to = " + delay.getTo() + ", step = " + delay.getStep() + "\n").getBytes("UTF-8"));
        settingsStream.close();

        path = Paths.get("statistics", timeStamp + ".csv");
        if (!path.toFile().exists()) {
            Files.createFile(path);
        }

        final DataOutputStream statisticsStream = new DataOutputStream(new FileOutputStream(path.toFile()));
        statisticsStream.write("Clients count, Array length, Delay, Client time, Request time, Process time\n".getBytes("UTF-8"));
        for (final List<Long> round : statistics) {
            statisticsStream.write((round.stream().map(x -> x.toString()).collect(Collectors.joining(",")) + "\n").getBytes("UTF-8"));
        }
        statisticsStream.close();
    }

    private String getCurrentTimeStamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
}
