package com.epicdima.findwords.utils;

import androidx.annotation.NonNull;

public final class Utils {
    public static final int BLOCKED = ' ';

    @NonNull
    public static final String DEFAULT_DICTIONARY = "/dictionary.txt";

    private Utils() {
        throw new AssertionError("Private constructor");
    }
}
