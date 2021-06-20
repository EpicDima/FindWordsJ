package com.epicdima.findwords.solver;

import com.epicdima.findwords.base.Mask;
import com.epicdima.findwords.base.WordTrie;
import com.epicdima.findwords.mask.BitSetMask;

public class FastSolver extends AbstractSolver {

    public FastSolver(String linesSeparator, WordTrie trie) {
        super(linesSeparator, trie);
    }

    @Override
    protected Mask createOriginalMask(int rows, int cols) {
        return new BitSetMask(rows, cols);
    }
}
