package com.epicdima.findwords.trie;

import com.epicdima.findwords.base.TrieTest;
import com.epicdima.findwords.base.WordTrie;
import com.epicdima.findwords.utils.Utils;

public class ArrayWordTrieTest extends TrieTest {

    @Override
    protected WordTrie createWordTrie() {
        return ArrayWordTrie.createInstance(Utils.DEFAULT_DICTIONARY);
    }
}
