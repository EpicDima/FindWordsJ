package com.epicdima.findwords.trie;

public class ArrayWordTrieTest extends TrieTest {

    @Override
    protected WordTrie createWordTrie() {
        return WordTrieType.ARRAY.createInstance(dictionaryPath);
    }
}
