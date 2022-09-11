package com.andmal.client;

import java.io.UncheckedIOException;

public interface Sender {
    void sendMessages(String msgRoot) throws UncheckedIOException, InterruptedException;
}
