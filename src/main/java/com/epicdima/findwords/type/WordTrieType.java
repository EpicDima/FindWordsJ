package com.epicdima.findwords.type;

import com.epicdima.findwords.trie.ArrayWordTrie;
import com.epicdima.findwords.trie.HashWordTrie;
import com.epicdima.findwords.trie.SetWordTrie;
import com.epicdima.findwords.trie.WordTrie;
import com.epicdima.findwords.trie.WordTrieInstantiator;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public enum WordTrieType implements WordTrieInstantiator {
    HASH(HashWordTrie.class),
    ARRAY(ArrayWordTrie.class),
    SET(SetWordTrie.class);

    private final MethodHandle createInstanceStringMH;

    WordTrieType(Class<?> wordTrieClass) {
        try {
            createInstanceStringMH = MethodHandles.publicLookup()
                    .findStatic(wordTrieClass, "createInstance",
                            MethodType.methodType(WordTrie.class, String.class));
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public WordTrie createInstance(String dictionaryPath) {
        try {
            return (WordTrie) createInstanceStringMH.invoke(dictionaryPath);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }
}
