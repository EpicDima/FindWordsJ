package com.epicdima.findwords.trie;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.io.InputStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public enum WordTrieType {
    HASH(HashWordTrie.class),
    ARRAY(ArrayWordTrie.class),
    SET(SetWordTrie.class),
    FLAT(FlatWordTrie.class);

    @NonNull
    private final Class<? extends WordTrie> wordTrieClass;

    @NonNull
    private final MethodHandle createInstanceInputStreamMH;

    @NonNull
    private final MethodHandle createInstanceStringMH;

    WordTrieType(@NonNull Class<? extends WordTrie> wordTrieClass) {
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

    @NonNull
    public final WordTrie createInstance(@Nullable InputStream inputStream) {
        try {
            return (WordTrie) createInstanceInputStreamMH.invoke(inputStream);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    @NonNull
    public final WordTrie createInstance(@NonNull String dictionaryPath) {
        try {
            return (WordTrie) createInstanceStringMH.invoke(dictionaryPath);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    @NonNull
    public final Class<? extends WordTrie> getWordTrieClass() {
        return wordTrieClass;
    }
}
