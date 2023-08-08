package com.epicdima.findwords.solver;

import com.epicdima.findwords.mask.MaskType;
import com.epicdima.findwords.trie.WordTrieType;

public class VirtualMultiThreadedSolverTest extends SolverTest {

    @Override
    protected Solver createSolver() {
        return SolverType.VIRTUAL_MULTI_THREADED.createInstance(
                linesSeparator,
                MaskType.BOOLEAN,
                WordTrieType.HASH.createInstance(dictionaryPath)
        );
    }
}
