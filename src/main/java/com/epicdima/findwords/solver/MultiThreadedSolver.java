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

    @Override
    protected void findFullMatches(@NonNull List<WordAndMask> matchedWords) {
        List<WordAndMask>[][] matrix = createWordAndMaskMatrix(matchedWords);

        threadPool = Executors.newCachedThreadPool();
        try {
            f2(originalMask.copy(), matrix, 0, new ArrayList<>());
        } finally {
            threadPool.close();
        }
    }

    @Override
    protected void f2(@NonNull Mask mask, @NonNull List<WordAndMask>[][] matrix, int startIndex, @NonNull List<WordAndMask> result) {
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
                    if (threadPool != null) {
                        final int finalI = i;
                        futures.add(threadPool.submit(() -> {
                            List<WordAndMask> tempResult = new ArrayList<>(result.size() + 1);
                            tempResult.addAll(result);
                            tempResult.add(positionWordAndMask);
                            f2(mask.copy().or(positionWordAndMask.mask()), matrix, finalI + 1, tempResult);
                        }));
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
            break;
        }
    }
}
