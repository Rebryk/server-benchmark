package rebryk.net.protobuf;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;

/**
 * Created by rebryk on 20/05/16.
 */
public class ProtobufUtils {
    public static Protocol.BenchmarkPacket receivePacket(final Socket socket) throws IOException {
        final DataInputStream input = new DataInputStream(socket.getInputStream());
        final byte[] data = new byte[input.readInt()];
        input.readFully(data);
        return Protocol.BenchmarkPacket.parseFrom(data);
    }

    public static void sendPacket(final Socket socket, final Protocol.BenchmarkPacket packet) throws IOException {
        final DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        output.writeInt(packet.getSerializedSize());
        output.write(packet.toByteArray());
        output.flush();
    }

    public static Protocol.BenchmarkPacket parsePacket(final DatagramPacket datagramPacket) throws IOException {
        final ByteBuffer buffer = ByteBuffer.wrap(datagramPacket.getData());
        final byte[] data = new byte[buffer.getInt()];
        buffer.get(data);
        return Protocol.BenchmarkPacket.parseFrom(data);
    }

    public static Protocol.BenchmarkPacket receivePacket(final DatagramSocket socket, final byte[] buf) throws IOException {
        final DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length);
        socket.receive(datagramPacket);
        return parsePacket(datagramPacket);
    }

    public static void sendPacket(final DatagramSocket socket, final SocketAddress address,
                                  final Protocol.BenchmarkPacket packet, final byte[] buf) throws IOException {
        final ByteBuffer buffer = ByteBuffer.wrap(buf);
        buffer.putInt(packet.getSerializedSize());
        buffer.put(packet.toByteArray());
        socket.send(new DatagramPacket(buffer.array(), buffer.array().length, address));
    }
}
