package com.epicdima.findwords.solver;

import com.epicdima.findwords.trie.WordTrie;

public interface SolverInstantiator {

    Solver createInstance(String linesSeparator, WordTrie trie);
}
