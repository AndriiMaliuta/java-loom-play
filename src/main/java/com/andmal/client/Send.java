package com.andmal.client;

import java.io.*;
import java.net.Socket;
import java.time.Duration;
import java.time.Instant;

public class Send {

    public static void main(String[] args) throws InterruptedException {
        var threading = Threading.POOLED;
        int msgCount = 10;
        int threads = 100;
        Sender sender = switch (threading) {
            case POOLED -> new PooledSender(Send::sendMessageAndWaitForReply, msgCount, threads);
            case VIRTUAL -> new VirtualThreadSender(Send::sendMessageAndWaitForReply, msgCount);

        };
        System.out.printf("Sender up - start sending %d messages...%n", msgCount);
        var startTime = System.currentTimeMillis();
        sender.sendMessages("fOo bAR");
        var elapsedTime = System.currentTimeMillis() - startTime;
        System.out.printf("Done in %dms%n(NOTE: This measurement is very unreliable for various reasons! E.g. `println` itself.)%n", elapsedTime);
    }

    private static void sendMessageAndWaitForReply(String message) {
        Instant start = Instant.now();
        System.out.printf("%s: Sending: '%s'%n", start, message);
        try (var socket = new Socket("localhost", 8088);
             var receiver = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             var sender = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()))
        ) {
            sender.println(message);
            sender.flush();
            var reply = receiver.readLine();
            var end = Instant.now();
            System.out.printf("%s: Received: '%s'.%n (%s)", end, reply, Duration.between(start, end));
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }


    private enum Threading {
        POOLED,
        VIRTUAL
    }
}
