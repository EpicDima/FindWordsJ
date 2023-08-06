package com.epicdima.findwords.solver;

import com.epicdima.findwords.trie.WordTrieType;

public class CoroutineSolverTest extends SolverTest {

    @Override
    protected Solver createSolver() {
        return SolverType.COROUTINE.createInstance(linesSeparator, WordTrieType.HASH.createInstance(dictionaryPath));
    }
}
