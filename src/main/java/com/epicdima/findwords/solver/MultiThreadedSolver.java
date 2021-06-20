package com.epicdima.findwords.solver;

import com.epicdima.findwords.base.Mask;
import com.epicdima.findwords.base.WordAndMask;
import com.epicdima.findwords.base.WordTrie;

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
        ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        List<Mask> masks = getRawMasks(matchedWords);
        int[] minXY = getMinXAndMinY(masks);

        List<Future<?>> futures = new ArrayList<>();

        int length = masks.size();
        for (int i = 0; i < length; i++) {
            if (masks.get(i).get(minXY[0], minXY[1])) {
                Mask temp1 = masks.get(i).copy().or(originalMask);

                int finalI = i;
                futures.add(threadPool.submit(() -> {
                    boolean[] indexes = new boolean[length];
                    indexes[finalI] = true;
                    f2(temp1, indexes, 0, masks);
                }));
            }
        }

        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                future.cancel(true);
            }
        }

        threadPool.shutdown();
    }

    protected List<Mask> getRawMasks(List<WordAndMask> matchedWords) {
        Mask invertedOriginalMask = originalMask.copy().invert();

        List<Mask> masks = new ArrayList<>(matchedWords.size());
        for (WordAndMask matchedWord : matchedWords) {
            masks.add(matchedWord.mask.copy().and(invertedOriginalMask));
        }

        return masks;
    }

    protected int[] getMinXAndMinY(List<Mask> masks) {
        int[][] count = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                for (Mask mask : masks) {
                    if (mask.get(i, j)) {
                        count[i][j]++;
                    }
                }
            }
        }

        int min = Integer.MAX_VALUE;
        int minX = 0, minY = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (count[i][j] < min) {
                    min = count[i][j];
                    minX = i;
                    minY = j;
                }
            }
        }

        return new int[]{minX, minY};
    }
}
