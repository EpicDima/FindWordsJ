package com.epicdima.findwords.solver;

import com.epicdima.findwords.mask.Mask;
import com.epicdima.findwords.trie.WordTrie;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MultiThreadedSolver extends FastSolver {

    public MultiThreadedSolver(String linesSeparator, WordTrie trie) {
        super(linesSeparator, trie);
    }

    @Override
    protected void ffff(List<WordAndMask> matchedWords) {
        Mask[] masks = getRawMasks(matchedWords);
        int[] minXY = getMinXAndMinY(masks);

        try (ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())) {
            List<Future<?>> futures = new ArrayList<>(masks.length);

            for (int i = 0; i < masks.length; i++) {
                if (masks[i].get(minXY[0], minXY[1])) {
                    int finalI = i;
                    futures.add(threadPool.submit(() -> {
                        boolean[] indexes = new boolean[masks.length];
                        indexes[finalI] = true;
                        f2(masks[finalI].copy().or(originalMask), indexes, 0, masks);
                    }));
                }
            }

            for (Future<?> future : futures) {
                try {
                    future.get();
                } catch (InterruptedException | ExecutionException e) {
                    Thread.currentThread().interrupt();
                    future.cancel(true);
                }
            }
        }
    }
}
