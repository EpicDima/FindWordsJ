package com.epicdima.findwords.solver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    @Nullable
    protected ExecutorService threadPool = null;

    public MultiThreadedSolver(@NonNull String linesSeparator, @NonNull MaskType maskType, @NonNull WordTrie wordTrie) {
        super(linesSeparator, maskType, wordTrie);
    }

    private static final int MAX_DEPTH = 3;

    @Override
    protected void findFullMatches(@NonNull List<WordAndMask> matchedWords) {
        List<WordAndMask>[][] matrix = createWordAndMaskMatrix(matchedWords);

        threadPool = Executors.newCachedThreadPool();
        try {
            f2MT(originalMask.copy(), matrix, new ArrayList<>(), 0);
        } finally {
            threadPool.close();
        }
    }

    protected void f2MT(@NonNull Mask mask, @NonNull List<WordAndMask>[][] matrix, @NonNull List<WordAndMask> result, int depth) {
        if (mask.isAllTrue()) {
            fullMatches.add(result);
            return;
        }

        int targetIndex = -1;
        int minOptions = Integer.MAX_VALUE;

        for (int i = 0; i < rows * cols; i++) {
            if (!mask.get(i)) {
                int optionsCount = 0;
                for (WordAndMask wordAndMask : matrix[i / cols][i % cols]) {
                    if (mask.notIntersects(wordAndMask.mask())) {
                        optionsCount++;
                    }
                }

                if (optionsCount < minOptions) {
                    minOptions = optionsCount;
                    targetIndex = i;
                    if (optionsCount <= 1) {
                        break;
                    }
                }
            }
        }

        if (targetIndex != -1) {
            List<Future<?>> futures = new ArrayList<>();

            for (WordAndMask positionWordAndMask : matrix[targetIndex / cols][targetIndex % cols]) {
                if (mask.notIntersects(positionWordAndMask.mask())) {
                    List<WordAndMask> tempResult = new ArrayList<>(result.size() + 1);
                    tempResult.addAll(result);
                    tempResult.add(positionWordAndMask);

                    if (threadPool != null && depth < MAX_DEPTH) {
                        futures.add(threadPool.submit(() -> f2MT(mask.copy().or(positionWordAndMask.mask()), matrix, tempResult, depth + 1)));
                    } else {
                        f2(mask.copy().or(positionWordAndMask.mask()), matrix, tempResult);
                    }
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
