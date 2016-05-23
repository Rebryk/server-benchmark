package rebryk.net.servers;

import rebryk.benchmark.BenchmarkServer;
import rebryk.net.servers.tcp.TCPServerMultiThread;
import rebryk.net.servers.tcp.TCPServerNIO;
import rebryk.net.servers.tcp.TCPServerSingleThread;
import rebryk.net.servers.tcp.TCPServerThreadPool;
import rebryk.net.servers.udp.UDPServerMultithread;
import rebryk.net.servers.udp.UDPServerThreadPool;

/**
 * Created by rebryk on 20/05/16.
 */
public final class ServerFactory {
    private ServerFactory() {}

    public static Server create(final BenchmarkServer.ServerType type) {
        switch (type) {
            case TCP_NIO:
                return new TCPServerNIO();
            case TCP_MULTI_THREAD:
                return new TCPServerMultiThread();
            case TCP_THREAD_POOL:
                return new TCPServerThreadPool();
            case UDP_MULTI_THREAD:
                return new UDPServerMultithread();
            case UDP_THREAD_POOL:
                return new UDPServerThreadPool();
            default: // TCP_SINGLE_THREAD
                return new TCPServerSingleThread();
        }
    }
}
