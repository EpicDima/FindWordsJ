package com.epicdima.findwords.solver;

import com.epicdima.findwords.mask.BitSetMask;
import com.epicdima.findwords.mask.Mask;
import com.epicdima.findwords.trie.WordTrie;

public class FastSolver extends AbstractSolver {

    public FastSolver(String linesSeparator, WordTrie trie) {
        super(linesSeparator, trie);
    }

    @Override
    protected Mask createOriginalMask(int rows, int cols) {
        return new BitSetMask(rows, cols);
    }
}
