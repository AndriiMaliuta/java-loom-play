package com.andmal;

import com.andmal.server.Echo;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.List;

public class Exp {
    public static void main(String[] args) {
        try {
            Echo.main(List.of("", "").toArray(String[]::new));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
