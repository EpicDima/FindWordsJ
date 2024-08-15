package com.epicdima.findwords.trie;

import androidx.annotation.NonNull;

public final class ArrayWordTrieTest extends TrieTest {

    @NonNull
    @Override
    protected WordTrie createWordTrie() {
        return WordTrieType.ARRAY.createInstance(dictionaryPath);
    }
}
