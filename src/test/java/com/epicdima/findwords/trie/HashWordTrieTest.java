package com.epicdima.findwords.trie;

import com.epicdima.findwords.base.TrieTest;
import com.epicdima.findwords.base.WordTrie;
import com.epicdima.findwords.type.WordTrieType;
import com.epicdima.findwords.utils.Utils;

public class HashWordTrieTest extends TrieTest {

    @Override
    protected WordTrie createWordTrie() {
        return WordTrieType.HASH.createInstance(dictionaryPath);
    }
}
