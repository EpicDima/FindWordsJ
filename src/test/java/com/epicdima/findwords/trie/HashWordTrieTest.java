package com.epicdima.findwords.trie;

import com.epicdima.findwords.base.TrieTest;
import com.epicdima.findwords.base.WordTrie;
import com.epicdima.findwords.utils.Utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class HashWordTrieTest extends TrieTest {

    @Override
    protected WordTrie createWordTrie() {
        return HashWordTrie.createInstance(Utils.DEFAULT_DICTIONARY);
    }

    @Override
    protected List<String> readDictionary() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(Utils.DEFAULT_DICTIONARY), StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
