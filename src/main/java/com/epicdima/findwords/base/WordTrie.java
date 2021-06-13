package com.epicdima.findwords.base;

public interface WordTrie {
    void insert(final String word);

    boolean containsSubstring(final String substring);

    boolean containsWord(final String word);
}
