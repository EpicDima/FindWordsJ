package com.epicdima.findwords.trie;

import androidx.annotation.NonNull;

public final class SetWordTrieTest extends WordTrieTest {

    @NonNull
    @Override
    protected WordTrie createWordTrie() {
        return WordTrieType.SET.createInstance(dictionaryPath);
    }
}
