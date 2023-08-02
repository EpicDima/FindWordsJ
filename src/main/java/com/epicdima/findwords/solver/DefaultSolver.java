package com.epicdima.findwords.solver;

import com.epicdima.findwords.mask.BooleanMask;
import com.epicdima.findwords.mask.Mask;
import com.epicdima.findwords.trie.WordTrie;

public class DefaultSolver extends AbstractSolver {

    public DefaultSolver(String linesSeparator, WordTrie trie) {
        super(linesSeparator, trie);
    }

    @Override
    protected Mask createOriginalMask(int rows, int cols) {
        return new BooleanMask(rows, cols);
    }
}
