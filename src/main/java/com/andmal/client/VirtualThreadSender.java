package com.andmal.client;

import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class VirtualThreadSender implements Sender {
    private final Consumer<String> sender;
    private final int messageCount;

    public VirtualThreadSender(Consumer<String> sender, int messageCount) {
        this.sender = Objects.requireNonNull(sender);
        this.messageCount = messageCount;
    }

    @Override
    public void sendMessages(String messageRoot) throws UncheckedIOException {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            IntStream.range(0, messageCount)
                    .forEach(counter -> {
                        String message = messageRoot + " " + counter;
                        Runnable send = () -> sender.accept(message);
                        executor.execute(send);
                    });
        } catch (Exception ex) {
            if (ex.getCause() instanceof RuntimeException runtimeException)
                throw runtimeException;
            else
                throw new RuntimeException(ex.getCause());
        }
    }
}
