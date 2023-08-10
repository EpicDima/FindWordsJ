package com.epicdima.findwords.solver;

import com.epicdima.findwords.mask.MaskType;
import com.epicdima.findwords.trie.WordTrie;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class VirtualMultiThreadedSolver extends MultiThreadedSolver {

    public VirtualMultiThreadedSolver(String linesSeparator, MaskType maskType, WordTrie wordTrie) {
        super(linesSeparator, maskType, wordTrie);
    }

    @Override
    protected void findFullMatches(List<WordAndMask> matchedWords) {
        List<WordAndMask>[][] matrix = createWordAndMaskMatrix(matchedWords);

        threadPool = Executors.newVirtualThreadPerTaskExecutor();
        try {
            f2(originalMask.copy(), matrix, 0, new ArrayList<>());
        } finally {
            threadPool.close();
        }
    }
}
