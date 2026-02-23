package com.epicdima.findwords.trie;

import androidx.annotation.NonNull;
import com.epicdima.findwords.utils.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public abstract class TrieTest {
    @NonNull
    protected final String dictionaryPath = TestUtils.DEFAULT_DICTIONARY;

    @NonNull
    protected abstract WordTrie createWordTrie();

    @NonNull
    protected final List<String> readDictionary() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(dictionaryPath), StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public final void containsSubstring() {
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
    public final void containsSubstringAll() {
        WordTrie trie = createWordTrie();

        List<String> words = readDictionary();

        for (String word : words) {
            for (int i = 1; i < word.length(); i++) {
                Assertions.assertTrue(trie.containsSubstring(word.substring(0, i)));
                Assertions.assertFalse(trie.containsSubstring(word + word + word)); // there is mistake on two (there are two-word words)
                Assertions.assertFalse(trie.containsSubstring(word + word + word + word));
            }
        }
    }

    @Test
    public final void containsWord() {
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
    public final void containsWordAll() {
        WordTrie trie = createWordTrie();

        List<String> words = readDictionary();

        for (String word : words) {
            Assertions.assertTrue(trie.containsWord(word));
            Assertions.assertFalse(trie.containsWord(word + word + word));
            Assertions.assertFalse(trie.containsWord(word + word + word + word));
        }
    }

    @Test
    public void searchWordWithSurrogatePair() {
        String customDict = "a\uD83D\uDE0Ab\n";
        InputStream is = new ByteArrayInputStream(customDict.getBytes(StandardCharsets.UTF_8));

        WordTrie trie;
        if (this instanceof ArrayWordTrieTest) {
            trie = WordTrieType.ARRAY.createInstance(is);
        } else if (this instanceof HashWordTrieTest) {
            trie = WordTrieType.HASH.createInstance(is);
        } else {
            trie = WordTrieType.SET.createInstance(is);
        }

        Assertions.assertTrue(trie.containsWord("a\uD83D\uDE0Ab"));
        Assertions.assertFalse(trie.containsWord("a\uD83D\uDE0Ac"));
    }

    @Test
    public void verifyNoFalsePositivesWithSameModulo() {
        String customDict = "A\nE\n";
        InputStream is = new ByteArrayInputStream(customDict.getBytes(StandardCharsets.UTF_8));

        WordTrie trie;
        if (this instanceof ArrayWordTrieTest) {
            trie = WordTrieType.ARRAY.createInstance(is);
        } else if (this instanceof HashWordTrieTest) {
            trie = WordTrieType.HASH.createInstance(is);
        } else {
            trie = WordTrieType.SET.createInstance(is);
        }

        Assertions.assertTrue(trie.containsWord("A"));
        Assertions.assertTrue(trie.containsWord("E"));
        Assertions.assertFalse(trie.containsWord("I"));
    }
}
