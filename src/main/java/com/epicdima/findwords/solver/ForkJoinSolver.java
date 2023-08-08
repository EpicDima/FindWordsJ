package com.epicdima.findwords.solver;

import com.epicdima.findwords.mask.Mask;
import com.epicdima.findwords.mask.MaskType;
import com.epicdima.findwords.trie.WordTrie;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

public class ForkJoinSolver extends MultiThreadedSolver {
    private final ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();

    public ForkJoinSolver(String linesSeparator, MaskType maskType, WordTrie wordTrie) {
        super(linesSeparator, maskType, wordTrie);
    }

    @Override
    protected void ffff(List<WordAndMask> matchedWords) {
        List<WordAndMask>[][] matrix = createWordAndMaskMatrix(matchedWords);
        forkJoinPool.submit(new F2Action(originalMask.copy(), matrix, 0, new ArrayList<>())).join();
    }

    private class F2Action extends RecursiveAction {
        private final Mask mask;
        private final List<WordAndMask>[][] matrix;
        private final int startIndex;
        private final List<WordAndMask> result;

        public F2Action(Mask mask, List<WordAndMask>[][] matrix, int startIndex, List<WordAndMask> result) {
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
