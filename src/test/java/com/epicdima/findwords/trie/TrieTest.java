package com.epicdima.findwords.trie;

import com.epicdima.findwords.utils.Utils;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public abstract class TrieTest {
    protected final String dictionaryPath = Utils.DEFAULT_DICTIONARY;

    protected abstract WordTrie createWordTrie();

    protected List<String> readDictionary() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(dictionaryPath), StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void containsSubstring() {
        WordTrie trie = createWordTrie();

        Assertions.assertTrue(trie.containsSubstring("п"));
        Assertions.assertTrue(trie.containsSubstring("пр"));
        Assertions.assertTrue(trie.containsSubstring("при"));
        Assertions.assertTrue(trie.containsSubstring("прив"));
        Assertions.assertTrue(trie.containsSubstring("приве"));
        Assertions.assertTrue(trie.containsSubstring("привет"));

        Assertions.assertFalse(trie.containsSubstring("приветъ"));
    }

    @Test
    void containsSubstringAll() {
        WordTrie trie = createWordTrie();

        List<String> words = readDictionary();

        for (String word : words) {
            for (int i = 1; i < word.length(); i++) {
                Assertions.assertTrue(trie.containsSubstring(word.substring(0, i)));
                Assertions.assertFalse(trie.containsSubstring(word + word + word)); // на двух ошибка
                Assertions.assertFalse(trie.containsSubstring(word + word + word + word));
            }
        }
    }

    @Test
    void containsWord() {
        WordTrie trie = createWordTrie();

        Assertions.assertFalse(trie.containsWord("п"));
        Assertions.assertFalse(trie.containsWord("пр"));
        Assertions.assertFalse(trie.containsWord("при"));
        Assertions.assertFalse(trie.containsWord("прив"));
        Assertions.assertFalse(trie.containsWord("приве"));
        Assertions.assertTrue(trie.containsWord("привет"));

        Assertions.assertFalse(trie.containsWord("приветъ"));
    }

    @Test
    void containsWordAll() {
        WordTrie trie = createWordTrie();

        List<String> words = readDictionary();

        for (String word : words) {
            Assertions.assertTrue(trie.containsWord(word));
            Assertions.assertFalse(trie.containsWord(word + word + word));
            Assertions.assertFalse(trie.containsWord(word + word + word + word));
        }
    }
}
