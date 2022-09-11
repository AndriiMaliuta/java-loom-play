package com.andmal.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

public class PooledServer {
    private final Consumer<Socket> echo;
    private final ExecutorService pool;

    public PooledServer(Consumer<Socket> echo, int threadsCount) {
        this.echo = requireNonNull(echo);
        this.pool = Executors.newFixedThreadPool(threadsCount);
    }

    public void listen() throws IOException {
        ServerSocket server = new ServerSocket(8088);
        try (pool) {
            while (true) {
                Socket socket = server.accept();
                pool.submit(() -> echo.accept(socket));
            }
        }
    }
}
