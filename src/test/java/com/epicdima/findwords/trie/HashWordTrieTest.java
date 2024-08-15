package com.epicdima.findwords.trie;

import androidx.annotation.NonNull;

public final class HashWordTrieTest extends TrieTest {

    @NonNull
    @Override
    protected WordTrie createWordTrie() {
        return WordTrieType.HASH.createInstance(dictionaryPath);
    }
}
