package com.epicdima.findwords.solver;

import com.epicdima.findwords.mask.MaskType;
import com.epicdima.findwords.trie.WordTrieType;

public class ForkJoinSolverTest extends SolverTest {

    @Override
    protected Solver createSolver() {
        return SolverType.FORKJOIN.createInstance(
                linesSeparator,
                MaskType.BOOLEAN,
                WordTrieType.HASH.createInstance(dictionaryPath)
        );
    }
}
