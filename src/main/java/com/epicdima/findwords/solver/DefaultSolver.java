package com.epicdima.findwords.solver;

import com.epicdima.findwords.base.Mask;
import com.epicdima.findwords.base.Solver;
import com.epicdima.findwords.base.WordAndMask;
import com.epicdima.findwords.base.WordTrie;
import com.epicdima.findwords.mask.BooleanMask;

import java.util.*;
import java.util.stream.Collectors;

public class DefaultSolver implements Solver {
    private static final String LINE_SEPARATOR = "\n";
    private static final char BLOCKED = ' ';

    private final WordTrie trie;
    private final List<WordAndMask> words = new ArrayList<>();
    private final List<boolean[]> rawFullMatchesResults = new ArrayList<>();
    private final List<List<WordAndMask>> fullMatches = new ArrayList<>();
    private int minWordLength;
    private int maxWordLength;
    private int rows;
    private int cols;
    private char[][] matrix;
    private Mask originalMask;

    public DefaultSolver(WordTrie trie) {
        this.trie = trie;
    }

    public void solve(String matrixText, int minWordLength, int maxWordLength, boolean fullMatch) {
        preSolve(matrixText, minWordLength, maxWordLength);

        words.addAll(findWords());

        for (WordAndMask wordAndMask : words) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    wordAndMask.mask.set(i, j, wordAndMask.mask.get(i, j) && !originalMask.get(i, j));
                }
            }
        }

        if (fullMatch) {
            findFullMatches();
        }
    }

    @Override
    public List<WordAndMask> getWords() {
        return new ArrayList<>(words);
    }

    @Override
    public List<List<WordAndMask>> getFullMatches() {
        return new ArrayList<>(fullMatches);
    }

    private void preSolve(String matrixText, int minWordLength, int maxWordLength) {
        this.minWordLength = minWordLength;
        this.maxWordLength = maxWordLength;

        List<String> lines = Arrays
                .stream(matrixText.split(LINE_SEPARATOR))
                .collect(Collectors.toList());

        rows = lines.size();
        cols = lines.get(0).length();

        matrix = new char[rows][cols];
        originalMask = new BooleanMask(rows, cols);

        words.clear();
        rawFullMatchesResults.clear();
        fullMatches.clear();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                char ch = lines.get(i).charAt(j);
                matrix[i][j] = ch;
                originalMask.set(i, j, ch == BLOCKED);
            }
        }
    }

    private void findFullMatches() {
        ffff(words);

        for (boolean[] indexes : rawFullMatchesResults) {
            List<WordAndMask> fullMatch = new ArrayList<>();

            for (int i = 0; i < indexes.length; i++) {
                if (indexes[i]) {
                    fullMatch.add(words.get(i));
                }
            }

            fullMatches.add(fullMatch);
        }
    }

    private Set<WordAndMask> findWords() {
        Set<WordAndMask> wordsAndMasks = new HashSet<>();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (!originalMask.get(i, j)) {
                    originalMask.set(i, j, true);
                    f(String.valueOf(matrix[i][j]), i, j, originalMask, wordsAndMasks);
                    originalMask.set(i, j, false);
                }
            }
        }

        return wordsAndMasks;
    }

    private void f(String word, int x, int y, Mask mask, Set<WordAndMask> wordsAndMasks) {
        if (word.length() >= minWordLength && trie.containsWord(word)) {
            wordsAndMasks.add(new WordAndMask(word, mask.copy()));
        }

        if (word.length() > maxWordLength || !trie.containsSubstring(word)) {
            return;
        }

        int x2 = x + 1;

        if (x2 < rows && !mask.get(x2, y)) {
            mask.set(x2, y, true);
            f(word + matrix[x2][y], x2, y, mask, wordsAndMasks);
            mask.set(x2, y, false);
        }

        x2 = x - 1;

        if (x2 >= 0 && !mask.get(x2, y)) {
            mask.set(x2, y, true);
            f(word + matrix[x2][y], x2, y, mask, wordsAndMasks);
            mask.set(x2, y, false);
        }

        int y2 = y + 1;

        if (y2 < cols && !mask.get(x, y2)) {
            mask.set(x, y2, true);
            f(word + matrix[x][y2], x, y2, mask, wordsAndMasks);
            mask.set(x, y2, false);
        }

        y2 = y - 1;

        if (y2 >= 0 && !mask.get(x, y2)) {
            mask.set(x, y2, true);
            f(word + matrix[x][y2], x, y2, mask, wordsAndMasks);
            mask.set(x, y2, false);
        }
    }

    private void f2(Mask mask, boolean[] indexes, int start, List<Mask> masks) {
        if (mask.isAllTrue()) {
            rawFullMatchesResults.add(indexes);
            return;
        }

        for (int i = start; i < indexes.length; i++) {
            if (mask.copy().and(masks.get(i)).isAllFalse()) {
                Mask temp1 = mask.copy().or(masks.get(i));
                boolean[] temp2 = Arrays.copyOf(indexes, indexes.length);
                temp2[i] = true;
                f2(temp1, temp2, i, masks);
            }
        }
    }

    private void ffff(List<WordAndMask> matchedWords) {
        Mask invertedOriginalMask = originalMask.copy().invert();

        List<Mask> masks = new ArrayList<>(matchedWords.size());
        for (WordAndMask matchedWord : matchedWords) {
            masks.add(matchedWord.mask.copy().and(invertedOriginalMask));
        }

        int i = 0;
        for (Mask mask : masks) {
            if (mask.get(0, 0)) {
                Mask temp1 = masks.get(i).copy().or(originalMask);
                boolean[] temp2 = new boolean[masks.size()];
                temp2[i] = true;
                f2(temp1, temp2, 0, masks);
            }
            i++;
        }
    }
}
