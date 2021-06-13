package com.epicdima.findwords.base;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public abstract class TrieTest {
    protected abstract WordTrie createWordTrie();

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
    void containsWord() {
        WordTrie trie = createWordTrie();

        Assertions.assertFalse(trie.containsWord("п"));
        Assertions.assertFalse(trie.containsWord("пр"));
        Assertions.assertFalse(trie.containsWord("при"));
        Assertions.assertFalse(trie.containsWord("прив"));
        Assertions.assertFalse(trie.containsWord("приве"));
        Assertions.assertTrue(trie.containsWord("привет"));

        Assertions.assertFalse(trie.containsWord("привет1"));
    }
}
