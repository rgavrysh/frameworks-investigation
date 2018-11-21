package org.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Scanner;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class AsyncClient {
    public static void main(String[] args) throws Exception {
        try (AsynchronousSocketChannel client = AsynchronousSocketChannel.open()) {
            InetSocketAddress address = new InetSocketAddress("localhost", 8000);
            System.out.println("Client: Trying to connect to: " + address);
            Future<Void> future = client.connect(address);
            System.out.println("Client: Waiting for connection to complete");
            future.get(5, TimeUnit.SECONDS);

            String message;
            do {
                System.out.println("Enter a message: ");
                Scanner scanner = new Scanner(System.in);
                message = scanner.nextLine();
                System.out.println("Client: Sending ...");
                ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
                System.out.println("Client: Message sent: " + new String(buffer.array()));
                client.write(buffer);
            } while (!message.equalsIgnoreCase("quit"));
        }
    }
}
