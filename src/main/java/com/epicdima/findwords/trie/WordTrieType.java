package com.epicdima.findwords.trie;

import java.io.InputStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public enum WordTrieType {
    HASH(HashWordTrie.class),
    ARRAY(ArrayWordTrie.class),
    SET(SetWordTrie.class);

    private final Class<? extends WordTrie> wordTrieClass;

    private final MethodHandle createInstanceInputStreamMH;
    private final MethodHandle createInstanceStringMH;

    WordTrieType(Class<? extends WordTrie> wordTrieClass) {
        this.wordTrieClass = wordTrieClass;
        try {
            createInstanceInputStreamMH = MethodHandles.publicLookup()
                    .findStatic(wordTrieClass, "createInstance", MethodType.methodType(WordTrie.class, InputStream.class));
            createInstanceStringMH = MethodHandles.publicLookup()
                    .findStatic(wordTrieClass, "createInstance", MethodType.methodType(WordTrie.class, String.class));
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public WordTrie createInstance(InputStream inputStream) {
        try {
            return (WordTrie) createInstanceInputStreamMH.invoke(inputStream);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    public WordTrie createInstance(String dictionaryPath) {
        try {
            return (WordTrie) createInstanceStringMH.invoke(dictionaryPath);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    public Class<? extends WordTrie> getWordTrieClass() {
        return wordTrieClass;
    }
}
