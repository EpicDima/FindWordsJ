package com.epicdima.findwords.utils;

import androidx.annotation.NonNull;

public final class TestUtils {

    @NonNull
    public static final String DEFAULT_DICTIONARY = "src/main/resources/dictionary.txt";

    private TestUtils() {
        throw new AssertionError("Private constructor");
    }
}
