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
        forkJoinPool.submit(new F2Action(originalMask.copy(), matrix, 0, new ArrayList<>())).join();
    }

    private class F2Action extends RecursiveAction {
        @NonNull
        private final Mask mask;
        @NonNull
        private final List<WordAndMask>[][] matrix;
        private final int startIndex;
        @NonNull
        private final List<WordAndMask> result;

        public F2Action(@NonNull Mask mask, @NonNull List<WordAndMask>[][] matrix, int startIndex, @NonNull List<WordAndMask> result) {
            this.mask = mask;
            this.matrix = matrix;
            this.startIndex = startIndex;
            this.result = result;
        }

        @Override
        protected void compute() {
            if (mask.isAllTrue()) {
                fullMatches.add(result);
                return;
            }

            for (int i = startIndex; i < rows * cols; i++) {
                if (mask.get(i)) {
                    continue;
                }
                List<ForkJoinTask<Void>> tasks = new ArrayList<>();

                for (WordAndMask positionWordAndMask : matrix[i / cols][i % cols]) {
                    if (mask.notIntersects(positionWordAndMask.mask())) {
                        List<WordAndMask> tempResult = new ArrayList<>(result.size() + 1);
                        tempResult.addAll(result);
                        tempResult.add(positionWordAndMask);
                        tasks.add(new F2Action(mask.copy().or(positionWordAndMask.mask()), matrix, i + 1, tempResult).fork());
                    }
                }

                for (ForkJoinTask<Void> task : tasks) {
                    task.join();
                }
                break;
            }
        }
    }
}
