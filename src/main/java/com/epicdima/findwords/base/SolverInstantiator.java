package com.epicdima.findwords.base;

public interface SolverInstantiator {

    Solver createInstance(String linesSeparator, WordTrie trie);
}
