package com.epicdima.findwords.trie;

import androidx.annotation.NonNull;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

public final class SetWordTrie implements WordTrie {
    @NonNull
    private final Set<String> words = new HashSet<>();
    @NonNull
    private final Set<String> substrings = new HashSet<>();

    @SuppressWarnings("unused") // used via MethodHandle
    @NonNull
    public static WordTrie createInstance(@NonNull String dictionaryPath) {
        WordTrie wordTrie = new SetWordTrie();
        WordTrie.fill(wordTrie, dictionaryPath);
        return wordTrie;
    }

    @SuppressWarnings("unused") // used via MethodHandle
    @NonNull
    public static WordTrie createInstance(@NonNull InputStream inputStream) {
        WordTrie wordTrie = new SetWordTrie();
        WordTrie.fill(wordTrie, inputStream);
        return wordTrie;
    }

    @Override
    public void insert(@NonNull String word) {
        words.add(word);
        final StringBuilder stringBuilder = new StringBuilder(word.length());
        for (int i = 0; i < word.length(); ) {
            int codePoint = word.codePointAt(i);
            stringBuilder.appendCodePoint(codePoint);
            substrings.add(stringBuilder.toString());
            i += Character.charCount(codePoint);
        }
    }

    @Override
    public boolean containsSubstring(@NonNull String substring) {
        return substrings.contains(substring);
    }

    @Override
    public boolean containsWord(@NonNull String word) {
        return words.contains(word);
    }
}
