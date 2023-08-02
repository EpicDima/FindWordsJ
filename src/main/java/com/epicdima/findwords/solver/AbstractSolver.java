package com.epicdima.findwords.solver;

import com.epicdima.findwords.mask.Mask;
import com.epicdima.findwords.trie.WordTrie;
import com.epicdima.findwords.utils.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public abstract class AbstractSolver implements Solver {
    protected final List<WordAndMask> words = new ArrayList<>();
    protected final List<boolean[]> rawFullMatchesResults = new ArrayList<>();
    protected final List<List<WordAndMask>> fullMatches = new ArrayList<>();

    protected final String linesSeparator;
    protected final WordTrie trie;

    protected int minWordLength;
    protected int maxWordLength;

    protected int rows;
    protected int cols;

    protected char[][] matrix;
    protected Mask originalMask;

    protected AbstractSolver(String linesSeparator, WordTrie trie) {
        this.linesSeparator = linesSeparator;
        this.trie = trie;
    }

    protected abstract Mask createOriginalMask(int rows, int cols);

    @Override
    public void solve(String matrixText, int minWordLength, int maxWordLength, boolean fullMatch) {
        preSolve(matrixText, minWordLength, maxWordLength);

        words.addAll(findWords());

        for (WordAndMask wordAndMask : words) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    wordAndMask.mask().set(i, j, wordAndMask.mask().get(i, j) && !originalMask.get(i, j));
                }
            }
        }

        if (fullMatch) {
            findFullMatches();
        }
    }

    protected void preSolve(String matrixText, int minWordLength, int maxWordLength) {
        this.minWordLength = minWordLength;
        this.maxWordLength = maxWordLength;

        String[] lines = matrixText.split(Pattern.quote(linesSeparator));

        rows = lines.length;
        cols = lines[0].length();

        matrix = new char[rows][cols];
        originalMask = createOriginalMask(rows, cols);

        words.clear();
        rawFullMatchesResults.clear();
        fullMatches.clear();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                char ch = lines[i].charAt(j);
                matrix[i][j] = ch;
                originalMask.set(i, j, ch == Utils.BLOCKED);
            }
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

    protected Set<WordAndMask> findWords() {
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

    @SuppressWarnings("DuplicatedCode")
    protected void f(String word, int x, int y, Mask mask, Set<WordAndMask> wordsAndMasks) {
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

    protected void findFullMatches() {
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

    protected void ffff(List<WordAndMask> matchedWords) {
        Mask invertedOriginalMask = originalMask.copy().invert();
        Mask[] masks = matchedWords.stream()
                .map(matchedWord -> matchedWord.mask().copy().and(invertedOriginalMask))
                .toArray(Mask[]::new);

        boolean[] indexes = new boolean[masks.length];
        int i = 0;
        for (Mask mask : masks) {
            if (mask.get(0, 0)) {
                indexes[i] = true;
                f2(mask.or(originalMask), indexes, 0, masks);
                indexes[i] = false;
                mask.xor(originalMask);
            }
            i++;
        }
    }

    protected void f2(Mask mask, boolean[] indexes, int start, Mask[] masks) {
        if (mask.isAllTrue()) {
            rawFullMatchesResults.add(Arrays.copyOf(indexes, indexes.length));
            return;
        }

        for (int i = start; i < indexes.length; i++) {
            if (mask.notIntersects(masks[i])) {
                indexes[i] = true;
                f2(mask.or(masks[i]), indexes, i, masks);
                indexes[i] = false;
                mask.xor(masks[i]);
            }
        }
    }
}
