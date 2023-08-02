package com.epicdima.findwords.trie;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public interface WordTrie {

    static void fill(WordTrie wordTrie, String dictionaryPath) {
        try {
            fill(wordTrie, new FileInputStream(dictionaryPath));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    static void fill(WordTrie wordTrie, InputStream inputStream) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String word;
            while ((word = reader.readLine()) != null) {
                wordTrie.insert(word);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void insert(final String word);

    boolean containsSubstring(final String substring);

    boolean containsWord(final String word);
}
