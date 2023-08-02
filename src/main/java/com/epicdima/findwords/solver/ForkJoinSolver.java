package com.epicdima.findwords.solver;

import com.epicdima.findwords.mask.Mask;
import com.epicdima.findwords.trie.WordTrie;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

public class ForkJoinSolver extends MultiThreadedSolver {
    private final ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();

    public ForkJoinSolver(String linesSeparator, WordTrie trie) {
        super(linesSeparator, trie);
    }

    @Override
    protected void ffff(List<WordAndMask> matchedWords) {
        Mask[] masks = getRawMasks(matchedWords);
        int[] minXY = getMinXAndMinY(masks);

        List<ForkJoinTask<Void>> tasks = new ArrayList<>(masks.length);

        for (int i = 0; i < masks.length; i++) {
            if (masks[i].get(minXY[0], minXY[1])) {
                boolean[] indexes = new boolean[masks.length];
                indexes[i] = true;
                tasks.add(forkJoinPool.submit(new F2Action(
                        masks[i].copy().or(originalMask),
                        indexes,
                        0,
                        masks)
                ));
            }
        }

        for (ForkJoinTask<Void> task : tasks) {
            task.join();
        }
    }


    private class F2Action extends RecursiveAction {
        private final Mask mask;
        private final boolean[] indexes;
        private final int start;
        private final Mask[] masks;

        public F2Action(Mask mask, boolean[] indexes, int start, Mask[] masks) {
            this.mask = mask;
            this.indexes = indexes;
            this.start = start;
            this.masks = masks;
        }

        @Override
        protected void compute() {
            if (mask.isAllTrue()) {
                rawFullMatchesResults.add(indexes);
                return;
            }

            List<ForkJoinTask<Void>> tasks = new ArrayList<>(indexes.length);

            for (int i = start; i < indexes.length; i++) {
                if (mask.notIntersects(masks[i])) {
                    boolean[] indexesCopy = Arrays.copyOf(indexes, indexes.length);
                    indexesCopy[i] = true;
                    tasks.add(new F2Action(mask.copy().or(masks[i]), indexesCopy, i, masks).fork());
                }
            }

            for (ForkJoinTask<Void> task : tasks) {
                task.join();
            }
        }
    }
}
