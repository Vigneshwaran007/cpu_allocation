package com.cpu.logger;

import java.io.PrintStream;

public class Logger {
    private final static PrintStream out = System.out;

    public static void log(String message) {
        log(message, null);
    }

    public static void log(String message, Object... arguments) {
        out.println(String.format(message, arguments));
    }
}
