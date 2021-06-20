package com.epicdima.findwords.utils;

import java.util.Objects;

public final class Utils {
    public static final char BLOCKED = ' ';

    public static final String DEFAULT_DICTIONARY;

    static {
        if (isJar()) {
            DEFAULT_DICTIONARY = "/dictionary.txt";
        } else {
            DEFAULT_DICTIONARY = "src/main/resources/dictionary.txt";
        }
    }

    private Utils() {
        throw new AssertionError("private constructor, bitch");
    }

    private static boolean isJar() {
        String className = Utils.class.getName().replace('.', '/');
        String classJar = Objects.requireNonNull(Utils.class.getResource("/" + className + ".class")).toString();
        return classJar.startsWith("jar:");
    }
}
