package com.andmal.server;

import java.io.*;
import java.net.Socket;

public class Echo {
    private final int echoWaitMs;

    public Echo(int echoWaitMs) {
        this.echoWaitMs = echoWaitMs;
    }

    public static void main(String[] args) throws IOException {
        Threading config = Threading.POOLED;
        var echo = new Echo(1000);
        int threadsCount = 100;
        Server server = switch (config) {
            case POOLED -> new PooledServer(echo::echo, threadsCount);
            case VIRTUAL -> new VirtualThreadServer(echo::echo);
        };

        System.out.println("Server up - start listening on 8080...");
        server.listen();
    }

    private void echo(Socket socket) {
        try (socket;
             var receiver = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             var sender = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()))
        ) {
            var message = receiver.readLine();
            System.out.printf("Echoed '%s'.%n", message);
            Thread.sleep(echoWaitMs);
            sender.println(message);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        } catch (InterruptedException ex) {
            // if the thread was interrupted during `sleep`,
            // the socket and streams will be closed without replying
            Thread.currentThread().interrupt();
        }
    }

    private enum Threading {
        POOLED, VIRTUAL
    }
}
