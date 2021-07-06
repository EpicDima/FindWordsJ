package com.epicdima.findwords.base;

import java.io.InputStream;

public interface WordTrieInstantiator {

    WordTrie createInstance(InputStream inputStream);

    WordTrie createInstance(String dictionaryPath);
}
