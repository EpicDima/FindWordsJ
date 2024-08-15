package com.epicdima.findwords.solver;

import androidx.annotation.NonNull;
import com.epicdima.findwords.mask.MaskType;
import com.epicdima.findwords.trie.WordTrieType;

public final class DefaultSolverTest extends SolverTest {

    @NonNull
    @Override
    protected Solver createSolver() {
        return SolverType.DEFAULT.createInstance(
                linesSeparator,
                MaskType.BOOLEAN,
                WordTrieType.HASH.createInstance(dictionaryPath)
        );
    }
}
