package com.epicdima.findwords.trie;

import androidx.annotation.NonNull;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public interface WordTrie {

    void insert(@NonNull final String word);

    boolean containsSubstring(@NonNull final String substring);

    boolean containsWord(@NonNull final String word);

    static void fill(@NonNull WordTrie wordTrie, @NonNull String dictionaryPath) {
        try {
            fill(wordTrie, new FileInputStream(dictionaryPath));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("No correct file in " + dictionaryPath, e);
        }
    }

    static void fill(@NonNull WordTrie wordTrie, @NonNull InputStream inputStream) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String word;
            while ((word = reader.readLine()) != null) {
                wordTrie.insert(word);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @NonNull
    static int[] getAbc(@NonNull String dictionaryPath) {
        try {
            return getAbc(new FileInputStream(dictionaryPath));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("No correct file in " + dictionaryPath, e);
        }
    }

    @NonNull
    static int[] getAbc(@NonNull InputStream inputStream) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines().flatMapToInt(String::chars).distinct().toArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
