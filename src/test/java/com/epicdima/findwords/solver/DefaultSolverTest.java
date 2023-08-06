package com.epicdima.findwords.solver;

import com.epicdima.findwords.trie.WordTrieType;

public class DefaultSolverTest extends SolverTest {

    @Override
    protected Solver createSolver() {
        return SolverType.DEFAULT.createInstance(linesSeparator, WordTrieType.HASH.createInstance(dictionaryPath));
    }
}
