package rebryk.net.clients;

import rebryk.benchmark.BenchmarkServer;
import rebryk.net.clients.tcp.TCPClient;
import rebryk.net.clients.tcp.TCPReClient;
import rebryk.net.clients.udp.UDPClient;

import java.util.function.Supplier;

/**
 * Created by rebryk on 20/05/16.
 */
public class ClientFactory implements Supplier<Client> {
    private final BenchmarkServer.ServerType serverType;
    private final String host;
    private final int port;
    private final int packetLeftCount;
    private final int arrayLength;
    private final long delay;

    public ClientFactory(final BenchmarkServer.ServerType serverType, final String host, final int port,
                         final int packetLeftCount, final int arrayLength, final long delay) {
        this.serverType = serverType;
        this.host = host;
        this.port = port;
        this.packetLeftCount = packetLeftCount;
        this.arrayLength = arrayLength;
        this.delay = delay;
    }

    @Override
    public Client get() {
        switch (serverType) {
            case TCP_SINGLE_THREAD:
                return new TCPReClient(host, port, packetLeftCount, arrayLength, delay);
            case UDP_MULTI_THREAD:
            case UDP_THREAD_POOL:
                return new UDPClient(host, port, packetLeftCount, arrayLength, delay);
            default: // TCP_MULTI_THREAD or TCP_THREAD_POOL or TCP_NIO
                return new TCPClient(host, port, packetLeftCount, arrayLength, delay);
        }
    }
}
