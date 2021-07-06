package com.epicdima.findwords.type;

import com.epicdima.findwords.base.WordTrie;
import com.epicdima.findwords.base.WordTrieInstantiator;
import com.epicdima.findwords.trie.ArrayWordTrie;
import com.epicdima.findwords.trie.SetWordTrie;
import com.epicdima.findwords.trie.HashWordTrie;

import java.io.InputStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public enum WordTrieType implements WordTrieInstantiator {
    HASH(HashWordTrie.class),
    ARRAY(ArrayWordTrie.class),
    SET(SetWordTrie.class);

    private final MethodHandle createInstanceInputStreamMH;
    private final MethodHandle createInstanceStringMH;

    WordTrieType(Class<?> wordTrieClass) {
        try {
            createInstanceInputStreamMH = MethodHandles.publicLookup()
                    .findStatic(wordTrieClass, "createInstance",
                            MethodType.methodType(WordTrie.class, InputStream.class));

            createInstanceStringMH = MethodHandles.publicLookup()
                    .findStatic(wordTrieClass, "createInstance",
                            MethodType.methodType(WordTrie.class, String.class));
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public WordTrie createInstance(InputStream inputStream) {
        try {
            return (WordTrie) createInstanceInputStreamMH.invoke(inputStream);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
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
