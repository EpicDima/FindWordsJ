package com.epicdima.findwords.trie;

import androidx.annotation.NonNull;

public final class FlatWordTrieTest extends TrieTest {

    @NonNull
    @Override
    protected WordTrie createWordTrie() {
        return WordTrieType.FLAT.createInstance(dictionaryPath);
    }
}
