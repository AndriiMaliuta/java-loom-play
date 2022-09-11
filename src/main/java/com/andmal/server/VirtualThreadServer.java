package com.andmal.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class VirtualThreadServer {
    private final Consumer<Socket> echo;

    public VirtualThreadServer(Consumer<Socket> echo) {
        this.echo =  Objects.requireNonNull(echo);
    }
    public void listen() {
        ServerSocket server = null;
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            server = new ServerSocket(8080);
            while (true) {
                Socket socket = server.accept();
                executor.execute(() -> echo.accept(socket));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
