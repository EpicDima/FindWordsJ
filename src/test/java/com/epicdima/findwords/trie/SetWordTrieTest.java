package com.epicdima.findwords.trie;

public class SetWordTrieTest extends TrieTest {

    @Override
    protected WordTrie createWordTrie() {
        return WordTrieType.SET.createInstance(dictionaryPath);
    }
}
