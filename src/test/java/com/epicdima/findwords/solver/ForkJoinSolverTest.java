package com.epicdima.findwords.solver;

import com.epicdima.findwords.trie.WordTrieType;

public class ForkJoinSolverTest extends SolverTest {

    @Override
    protected Solver createSolver() {
        return SolverType.FORKJOIN.createInstance(linesSeparator, WordTrieType.HASH.createInstance(dictionaryPath));
    }
}
