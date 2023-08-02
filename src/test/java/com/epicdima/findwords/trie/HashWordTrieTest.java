package com.epicdima.findwords.trie;

import com.epicdima.findwords.type.WordTrieType;

public class HashWordTrieTest extends TrieTest {

    @Override
    protected WordTrie createWordTrie() {
        return WordTrieType.HASH.createInstance(dictionaryPath);
    }
}
