package com.epicdima.findwords.solver;

import com.epicdima.findwords.trie.WordTrieType;

public class MultiThreadedSolverTest extends SolverTest {

    @Override
    protected Solver createSolver() {
        return SolverType.MULTITHREADED.createInstance(linesSeparator, WordTrieType.HASH.createInstance(dictionaryPath));
    }
}
