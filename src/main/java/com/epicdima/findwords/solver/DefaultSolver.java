package com.epicdima.findwords.solver;

import com.epicdima.findwords.mask.Mask;
import com.epicdima.findwords.mask.MaskType;
import com.epicdima.findwords.trie.WordTrie;

public class DefaultSolver extends AbstractSolver {

    public DefaultSolver(String linesSeparator, WordTrie trie) {
        super(linesSeparator, trie);
    }

    @Override
    protected Mask createOriginalMask(int rows, int cols) {
        return MaskType.BOOLEAN.createInstance(rows, cols);
    }
}
