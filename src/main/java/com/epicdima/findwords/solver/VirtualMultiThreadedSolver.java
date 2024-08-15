package com.epicdima.findwords.solver;

import androidx.annotation.NonNull;
import com.epicdima.findwords.mask.MaskType;
import com.epicdima.findwords.trie.WordTrie;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public final class VirtualMultiThreadedSolver extends MultiThreadedSolver {

    public VirtualMultiThreadedSolver(@NonNull String linesSeparator, @NonNull MaskType maskType, @NonNull WordTrie wordTrie) {
        super(linesSeparator, maskType, wordTrie);
    }

    @Override
    protected void findFullMatches(@NonNull List<WordAndMask> matchedWords) {
        List<WordAndMask>[][] matrix = createWordAndMaskMatrix(matchedWords);

        threadPool = Executors.newVirtualThreadPerTaskExecutor();
        try {
            f2(originalMask.copy(), matrix, 0, new ArrayList<>());
        } finally {
            threadPool.close();
        }
    }
}
