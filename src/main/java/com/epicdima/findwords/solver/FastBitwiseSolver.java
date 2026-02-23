package com.epicdima.findwords.solver;

import androidx.annotation.NonNull;
import com.epicdima.findwords.mask.MaskType;
import com.epicdima.findwords.trie.WordTrie;
import java.util.ArrayList;
import java.util.List;

public class FastBitwiseSolver extends DefaultSolver {

    public FastBitwiseSolver(@NonNull String linesSeparator, @NonNull MaskType maskType, @NonNull WordTrie wordTrie) {
        super(linesSeparator, maskType, wordTrie);
    }

    @Override
    protected void findFullMatches(@NonNull List<WordAndMask> matchedWords) {
        int wordsCount = matchedWords.size();
        if (wordsCount == 0) return;

        int totalCells = rows * cols;
        int longsPerMask = (totalCells + 63) >> 6;

        long[][] wordMasks = new long[wordsCount][longsPerMask];
        for (int w = 0; w < wordsCount; w++) {
            WordAndMask wordAndMask = matchedWords.get(w);
            for (int c = 0; c < totalCells; c++) {
                if (wordAndMask.mask().get(c)) {
                    wordMasks[w][c >> 6] |= (1L << (c & 63));
                }
            }
        }

        long[] baseMask = new long[longsPerMask];
        for (int c = 0; c < totalCells; c++) {
            if (originalMask.get(c)) {
                baseMask[c >> 6] |= (1L << (c & 63));
            }
        }

        int[][] cellOptions = new int[totalCells][];
        for (int c = 0; c < totalCells; c++) {
            if ((baseMask[c >> 6] & (1L << (c & 63))) != 0) {
                cellOptions[c] = new int[0];
                continue;
            }
            int count = 0;
            for (int w = 0; w < wordsCount; w++) {
                if ((wordMasks[w][c >> 6] & (1L << (c & 63))) != 0) {
                    count++;
                }
            }
            cellOptions[c] = new int[count];
            int idx = 0;
            for (int w = 0; w < wordsCount; w++) {
                if ((wordMasks[w][c >> 6] & (1L << (c & 63))) != 0) {
                    cellOptions[c][idx++] = w;
                }
            }
        }

        int[] stateStack = new int[wordsCount + 1];
        int[] cellStack = new int[wordsCount + 1];
        int[] wordStack = new int[wordsCount + 1];
        long[][] maskStack = new long[wordsCount + 1][longsPerMask];

        int depth = 0;
        System.arraycopy(baseMask, 0, maskStack[0], 0, longsPerMask);
        stateStack[0] = 0;

        cellStack[0] = findBestCell(maskStack[0], cellOptions, totalCells);

        if (cellStack[0] == -1) {
            if (isAllTrue(maskStack[0], totalCells)) {
                fullMatches.add(new ArrayList<>());
            }
            return;
        }

        while (depth >= 0) {
            int currentCell = cellStack[depth];
            int currentOptionIndex = stateStack[depth];

            if (currentOptionIndex >= cellOptions[currentCell].length) {
                depth--;
                if (depth >= 0) {
                    stateStack[depth]++;
                }
                continue;
            }

            int wordIdx = cellOptions[currentCell][currentOptionIndex];

            if (!intersects(maskStack[depth], wordMasks[wordIdx], longsPerMask)) {
                wordStack[depth] = wordIdx;
                
                for (int i = 0; i < longsPerMask; i++) {
                    maskStack[depth + 1][i] = maskStack[depth][i] | wordMasks[wordIdx][i];
                }

                if (isAllTrue(maskStack[depth + 1], totalCells)) {
                    List<WordAndMask> match = new ArrayList<>(depth + 1);
                    for (int i = 0; i <= depth; i++) {
                        match.add(matchedWords.get(wordStack[i]));
                    }
                    fullMatches.add(match);
                    
                    stateStack[depth]++;
                    continue;
                }

                int nextCell = findBestCell(maskStack[depth + 1], cellOptions, totalCells);
                
                if (nextCell != -1) {
                    depth++;
                    cellStack[depth] = nextCell;
                    stateStack[depth] = 0;
                } else {
                    stateStack[depth]++;
                }
            } else {
                stateStack[depth]++;
            }
        }
    }

    private int findBestCell(long[] mask, int[][] cellOptions, int totalCells) {
        int targetIndex = -1;
        int minOptions = Integer.MAX_VALUE;

        for (int c = 0; c < totalCells; c++) {
            if ((mask[c >> 6] & (1L << (c & 63))) == 0) {
                int optionsCount = cellOptions[c].length;

                if (optionsCount < minOptions) {
                    minOptions = optionsCount;
                    targetIndex = c;
                    if (optionsCount <= 1) {
                        break;
                    }
                }
            }
        }
        return targetIndex;
    }

    private boolean intersects(long[] m1, long[] m2, int len) {
        for (int i = 0; i < len; i++) {
            if ((m1[i] & m2[i]) != 0) return true;
        }
        return false;
    }

    private boolean isAllTrue(long[] mask, int totalCells) {
        int fullLongs = totalCells / 64;
        for (int i = 0; i < fullLongs; i++) {
            if (mask[i] != -1L) return false;
        }
        int remainder = totalCells % 64;
        if (remainder > 0) {
            long expected = (1L << remainder) - 1L;
            return mask[fullLongs] == expected;
        }
        return true;
    }
}
