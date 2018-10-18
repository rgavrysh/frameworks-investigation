package org.nio;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AsyncFileRead {
    public static void main(String[] args) throws IOException {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors());

        try (AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(Paths.get("/Users/rgavrysh/Java/spring/spring-basis/src/main/resources/items.txt"),
                EnumSet.of(StandardOpenOption.READ), executor)) {
            System.out.println("Main Thread ID: " + Thread.currentThread().getId());
            CompletionHandler<Integer, ByteBuffer> completionHandler = new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    for (int i = 0; i < attachment.limit(); i++) {
                        System.out.print((char) attachment.get(i));
                    }
                    System.out.println("\nCompletion Handler Thread ID: " + Thread.currentThread().getId());
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    System.err.println("Failed.");
                }
            };

            final int bufferCount = 5;
            ByteBuffer[] buffers = new ByteBuffer[bufferCount];
            for (int i = 0; i < bufferCount; i++) {
                buffers[i] = ByteBuffer.allocate(10);
                fileChannel.read(buffers[i], i * 10, buffers[i], completionHandler);
            }

            executor.awaitTermination(1, TimeUnit.SECONDS);
            System.out.println("Byte Buffers");
            for (ByteBuffer byteBuffer : buffers) {
                for (int i = 0; i < byteBuffer.limit(); i++) {
                    System.out.print((char) byteBuffer.get(i));
                }
                System.out.println();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
