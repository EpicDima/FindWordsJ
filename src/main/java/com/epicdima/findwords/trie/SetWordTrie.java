package com.epicdima.findwords.trie;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

public class SetWordTrie implements WordTrie {
    private final Set<String> words = new HashSet<>();
    private final Set<String> substrings = new HashSet<>();

    public static WordTrie createInstance(String dictionaryPath) {
        WordTrie wordTrie = new SetWordTrie();
        WordTrie.fill(wordTrie, dictionaryPath);
        return wordTrie;
    }

    public static WordTrie createInstance(InputStream inputStream) {
        WordTrie wordTrie = new SetWordTrie();
        WordTrie.fill(wordTrie, inputStream);
        return wordTrie;
    }

    @Override
    public void insert(String word) {
        words.add(word);

        for (int i = 1; i < word.length(); i++) {
            substrings.add(word.substring(0, i));
        }
    }

    @Override
    public boolean containsSubstring(String substring) {
        return substrings.contains(substring);
    }

    @Override
    public boolean containsWord(String word) {
        return words.contains(word);
    }
}
