package com.epicdima.findwords.trie;

import com.epicdima.findwords.type.WordTrieType;

public class SetWordTrieTest extends TrieTest {

    @Override
    protected WordTrie createWordTrie() {
        return WordTrieType.SET.createInstance(dictionaryPath);
    }
}
