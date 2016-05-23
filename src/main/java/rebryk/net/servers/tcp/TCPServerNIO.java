package rebryk.net.servers.tcp;

import rebryk.net.protobuf.Protocol;
import rebryk.net.servers.Server;
import rebryk.statistics.Statistics;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by rebryk on 22/05/16.
 */
public class TCPServerNIO extends Server {
    private final static int THREADS_COUNT = 4;
    private final List<SocketChannel> channels = new LinkedList<>();
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private ExecutorService threadPool;

    @Override
    public void start(final int port) throws IOException {
        setupServer(port);

        try {
            while (serverSocketChannel.isOpen()) {
                selector.select();
                final Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    final SelectionKey selectionKey = iterator.next();
                    if (selectionKey.isAcceptable()) {
                        acceptClient(selectionKey);
                    }
                    if (selectionKey.isReadable()) {
                        readRequest(selectionKey);
                    }
                    if (selectionKey.isWritable()) {
                        sendResponse(selectionKey);
                    }
                    iterator.remove();
                }
            }
        } catch (IOException e) {
            // server stopped
        }
    }

    @Override
    public void stop() throws IOException {
        serverSocketChannel.close();
        for (final SocketChannel channel : channels) {
            channel.close();
        }
        threadPool.shutdown();
    }

    private void setupServer(final int port) throws IOException {
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        threadPool = Executors.newFixedThreadPool(THREADS_COUNT);
    }

    private void acceptClient(final SelectionKey selectionKey) throws IOException {
        final SocketChannel socketChannel = ((ServerSocketChannel) selectionKey.channel()).accept();
        socketChannel.configureBlocking(false);
        socketChannel.socket().setTcpNoDelay(true);
        socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE, new ClientChannelInfo());
        channels.add(socketChannel);
    }

    private void readRequest(final SelectionKey selectionKey) throws IOException {
        final SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        final ClientChannelInfo client = (ClientChannelInfo) selectionKey.attachment();

        if (client.size.hasRemaining()) {
            if (client.requestInterval == null) {
                client.requestInterval = new Statistics.Interval();
            }

            socketChannel.read(client.size);
            if (!client.size.hasRemaining()) {
                client.size.flip();
                client.request = ByteBuffer.allocate(client.size.getInt());
            }
        }

        if (client.request != null) {
            socketChannel.read(client.request);
            if (!client.request.hasRemaining()) {
                client.request.flip();
                final Protocol.BenchmarkPacket packet = Protocol.BenchmarkPacket.parseFrom(client.request.array());
                threadPool.submit(requestHandler(client, packet));
                client.request = null;
                client.size.clear();
            }
        }
    }

    private void sendResponse(final SelectionKey selectionKey) throws IOException {
        final SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        final ClientChannelInfo client = (ClientChannelInfo) selectionKey.attachment();

        if (client.response == null) {
            Protocol.BenchmarkPacket packet;
            synchronized (client) {
                packet = client.responsePacket;
                client.responsePacket = null;
            }
            if (packet != null) {
                client.response = ByteBuffer.allocate(4 + packet.getSerializedSize());
                client.response.putInt(packet.getSerializedSize());
                client.response.put(packet.toByteArray());
                client.response.flip();
            }
        }

        if (client.response != null) {
            socketChannel.write(client.response);
            if (!client.response.hasRemaining()) {
                client.requestInterval.stop();
                requestStatistics.add(client.requestInterval.getTime());
                client.response = null;
                client.requestInterval = null;
            }
        }
    }

    private Runnable requestHandler(final ClientChannelInfo client, final Protocol.BenchmarkPacket packet) {
        return () -> {
            final Statistics.Interval processTime = new Statistics.Interval();
            final Protocol.BenchmarkPacket processedPacket = process(packet);
            processTime.stop();
            synchronized (client) {
                client.responsePacket = processedPacket;
            }
            processStatistics.add(processTime.getTime());
        };
    }

    private static class ClientChannelInfo {
        private Protocol.BenchmarkPacket responsePacket;
        private Statistics.Interval requestInterval;
        private ByteBuffer size = ByteBuffer.allocate(4);
        private ByteBuffer request;
        private ByteBuffer response;
    }
}
