package org.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AsyncServer {

    public static void main(String[] args) throws Exception {
        final InetSocketAddress address = new InetSocketAddress("localhost", 8000);

        try (AsynchronousServerSocketChannel server = AsynchronousServerSocketChannel.open()) {
            server.bind(address);
            System.out.println("Server: " + address.toString());

            server.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {
                @Override
                public void completed(AsynchronousSocketChannel channel, Void attribute) {
                    try {
                        System.out.println("Server: completed method executing");
                        while (true) {
                            ByteBuffer buffer = ByteBuffer.allocate(32);
                            Future<Integer> integerFuture = channel.read(buffer);
                            Integer bytes = integerFuture.get();
                            if (bytes > 0) {
                                byte[] msg = new byte[bytes];
                                ByteBuffer messageBuffer = buffer.get(msg, 0, bytes);
                                System.out.println("Server: Message received: " + messageBuffer.toString());
                            } else {
                                Thread.sleep(2000);
                                System.out.println("No message from client.");
                            }
                        }
                    } catch (InterruptedException | ExecutionException ex) {
                        System.err.println(ex.getMessage());
                    }
                }

                @Override
                public void failed(Throwable exc, Void attachment) {
                    System.err.println("Server: Completion handler exception");
                    System.err.println(exc.getMessage());
                }
            });
        }
        while(true) {

            // wait for client connects
        }
    }
}
