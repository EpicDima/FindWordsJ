package com.epicdima.findwords.solver;

import com.epicdima.findwords.type.SolverType;
import com.epicdima.findwords.type.WordTrieType;

public class CoroutineSolverTest extends SolverTest {

    @Override
    protected Solver createSolver() {
        return SolverType.COROUTINE.createInstance(linesSeparator, WordTrieType.HASH.createInstance(dictionaryPath));
    }
}
