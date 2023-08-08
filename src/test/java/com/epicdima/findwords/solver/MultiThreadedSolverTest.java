package com.epicdima.findwords.solver;

import com.epicdima.findwords.mask.MaskType;
import com.epicdima.findwords.trie.WordTrieType;

public class MultiThreadedSolverTest extends SolverTest {

    @Override
    protected Solver createSolver() {
        return SolverType.MULTI_THREADED.createInstance(
                linesSeparator,
                MaskType.BOOLEAN,
                WordTrieType.HASH.createInstance(dictionaryPath)
        );
    }
}
