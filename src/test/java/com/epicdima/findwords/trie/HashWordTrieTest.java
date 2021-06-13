package com.epicdima.findwords.trie;

import com.epicdima.findwords.base.TrieTest;
import com.epicdima.findwords.base.WordTrie;
import com.epicdima.findwords.utils.Utils;

public class HashWordTrieTest extends TrieTest {

    @Override
    protected WordTrie createWordTrie() {
        return HashWordTrie.createInstance(Utils.DEFAULT_DICTIONARY);
    }
}
