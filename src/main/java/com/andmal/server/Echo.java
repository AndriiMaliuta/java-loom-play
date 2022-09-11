package com.andmal.server;

public class Echo {
    private final int echoWaitMs;

    public Echo(int echoWaitMs) {
        this.echoWaitMs = echoWaitMs;
    }




    private enum Threading {
        POOLED, VIRTUAL
    }
}
