package com.epicdima.findwords.solver;

import com.epicdima.findwords.base.Mask;
import com.epicdima.findwords.base.WordTrie;
import com.epicdima.findwords.mask.BooleanMask;

public class DefaultSolver extends AbstractSolver {

    public DefaultSolver(String linesSeparator, WordTrie trie) {
        super(linesSeparator, trie);
    }

    @Override
    protected Mask createOriginalMask(int rows, int cols) {
        return new BooleanMask(rows, cols);
    }
}
