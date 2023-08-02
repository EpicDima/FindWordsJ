package com.epicdima.findwords.trie;

import com.epicdima.findwords.type.WordTrieType;

public class ArrayWordTrieTest extends TrieTest {

    @Override
    protected WordTrie createWordTrie() {
        return WordTrieType.ARRAY.createInstance(dictionaryPath);
    }
}
