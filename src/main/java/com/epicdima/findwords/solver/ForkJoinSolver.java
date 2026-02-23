package com.epicdima.findwords.solver;

import androidx.annotation.NonNull;
import com.epicdima.findwords.mask.Mask;
import com.epicdima.findwords.mask.MaskType;
import com.epicdima.findwords.trie.WordTrie;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

public final class ForkJoinSolver extends MultiThreadedSolver {
    @NonNull
    private final ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();

    public ForkJoinSolver(@NonNull String linesSeparator, @NonNull MaskType maskType, @NonNull WordTrie wordTrie) {
        super(linesSeparator, maskType, wordTrie);
    }

    @Override
    protected void findFullMatches(@NonNull List<WordAndMask> matchedWords) {
        List<WordAndMask>[][] matrix = createWordAndMaskMatrix(matchedWords);
        forkJoinPool.submit(new F2Action(originalMask.copy(), matrix, new ArrayList<>(), 0)).join();
    }

    private class F2Action extends RecursiveAction {
        private static final int MAX_DEPTH = 3;

        @NonNull
        private final Mask mask;
        @NonNull
        private final List<WordAndMask>[][] matrix;
        @NonNull
        private final List<WordAndMask> result;
        private final int depth;

        public F2Action(@NonNull Mask mask, @NonNull List<WordAndMask>[][] matrix, @NonNull List<WordAndMask> result, int depth) {
            this.mask = mask;
            this.matrix = matrix;
            this.result = result;
            this.depth = depth;
        }

        @Override
        protected void compute() {
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
                List<ForkJoinTask<Void>> tasks = new ArrayList<>();

                for (WordAndMask positionWordAndMask : matrix[targetIndex / cols][targetIndex % cols]) {
                    if (mask.notIntersects(positionWordAndMask.mask())) {
                        List<WordAndMask> tempResult = new ArrayList<>(result.size() + 1);
                        tempResult.addAll(result);
                        tempResult.add(positionWordAndMask);

                        if (depth < MAX_DEPTH) {
                            tasks.add(new F2Action(mask.copy().or(positionWordAndMask.mask()), matrix, tempResult, depth + 1).fork());
                        } else {
                            f2(mask.copy().or(positionWordAndMask.mask()), matrix, tempResult);
                        }
                    }
                }

                for (ForkJoinTask<Void> task : tasks) {
                    task.join();
                }
            }
        }
    }
}
