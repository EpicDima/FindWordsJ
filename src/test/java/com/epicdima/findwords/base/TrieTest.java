package com.epicdima.findwords.base;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public abstract class TrieTest {

    protected abstract WordTrie createWordTrie();

    protected abstract List<String> readDictionary();

    @Test
    void containsSubstring() {
        WordTrie trie = createWordTrie();

        Assertions.assertTrue(trie.containsSubstring("п"));
        Assertions.assertTrue(trie.containsSubstring("пр"));
        Assertions.assertTrue(trie.containsSubstring("при"));
        Assertions.assertTrue(trie.containsSubstring("прив"));
        Assertions.assertTrue(trie.containsSubstring("приве"));
        Assertions.assertTrue(trie.containsSubstring("привет"));

        Assertions.assertFalse(trie.containsSubstring("привет1"));
    }

    @Test
    void containsSubstringAll() {
        WordTrie trie = createWordTrie();

        List<String> words = readDictionary();

        for (String word : words) {
            for (int i = 2; i <= word.length(); i++) {
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

        Assertions.assertFalse(trie.containsWord("приветссс"));
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
