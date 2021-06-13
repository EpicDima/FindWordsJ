package com.epicdima.findwords.solver;

import com.epicdima.findwords.base.Solver;
import com.epicdima.findwords.base.SolverTest;
import com.epicdima.findwords.trie.HashWordTrie;
import com.epicdima.findwords.utils.Utils;

public class DefaultSolverTest extends SolverTest {

    @Override
    protected Solver createSolver() {
        return new DefaultSolver(HashWordTrie.createInstance(Utils.DEFAULT_DICTIONARY));
    }
}
