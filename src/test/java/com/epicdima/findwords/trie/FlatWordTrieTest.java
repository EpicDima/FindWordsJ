package com.epicdima.findwords.trie;

import androidx.annotation.NonNull;

public final class FlatWordTrieTest extends WordTrieTest {

    @NonNull
    @Override
    protected WordTrie createWordTrie() {
        return WordTrieType.FLAT.createInstance(dictionaryPath);
    }
}
