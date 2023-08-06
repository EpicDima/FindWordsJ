package com.epicdima.findwords.trie;

public class HashWordTrieTest extends TrieTest {

    @Override
    protected WordTrie createWordTrie() {
        return WordTrieType.HASH.createInstance(dictionaryPath);
    }
}
