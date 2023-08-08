package com.epicdima.findwords.solver;

import com.epicdima.findwords.mask.Mask;
import com.epicdima.findwords.mask.MaskType;
import com.epicdima.findwords.trie.WordTrie;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MultiThreadedSolver extends DefaultSolver {

    protected ExecutorService threadPool;

    public MultiThreadedSolver(String linesSeparator, MaskType maskType, WordTrie wordTrie) {
        super(linesSeparator, maskType, wordTrie);
    }

    @Override
    protected void ffff(List<WordAndMask> matchedWords) {
        List<WordAndMask>[][] matrix = createWordAndMaskMatrix(matchedWords);

        threadPool = Executors.newCachedThreadPool();
        try {
            f2(originalMask.copy(), matrix, 0, new ArrayList<>());
        } finally {
            threadPool.close();
        }
    }

    @Override
    protected void f2(Mask mask, List<WordAndMask>[][] matrix, int startIndex, List<WordAndMask> result) {
        if (mask.isAllTrue()) {
            fullMatches.add(result);
            return;
        }

        for (int i = startIndex; i < rows * cols; i++) {
            if (mask.get(i)) {
                continue;
            }
            List<Future<?>> futures = new ArrayList<>();

            for (WordAndMask positionWordAndMask : matrix[i / cols][i % cols]) {
                if (mask.notIntersects(positionWordAndMask.mask())) {
                    final int finalI = i;
                    futures.add(threadPool.submit(() -> {
                        List<WordAndMask> tempResult = new ArrayList<>(result.size() + 1);
                        tempResult.addAll(result);
                        tempResult.add(positionWordAndMask);
                        f2(mask.copy().or(positionWordAndMask.mask()), matrix, finalI + 1, tempResult);
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
            break;
        }
    }
}
