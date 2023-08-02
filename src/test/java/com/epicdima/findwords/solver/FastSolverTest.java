package com.epicdima.findwords.solver;

import com.epicdima.findwords.type.SolverType;
import com.epicdima.findwords.type.WordTrieType;

public class FastSolverTest extends SolverTest {

    @Override
    protected Solver createSolver() {
        return SolverType.FAST.createInstance(linesSeparator, WordTrieType.HASH.createInstance(dictionaryPath));
    }
}
