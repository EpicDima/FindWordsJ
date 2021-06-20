package com.epicdima.findwords.solver;

import com.epicdima.findwords.base.Mask;
import com.epicdima.findwords.base.Solver;
import com.epicdima.findwords.base.WordAndMask;
import com.epicdima.findwords.base.WordTrie;
import com.epicdima.findwords.utils.Utils;

import java.util.*;
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

    public AbstractSolver(String linesSeparator, WordTrie trie) {
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
                    wordAndMask.mask.set(i, j, wordAndMask.mask.get(i, j) & !originalMask.get(i, j));
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

        List<Mask> masks = new ArrayList<>(matchedWords.size());
        for (WordAndMask matchedWord : matchedWords) {
            masks.add(matchedWord.mask.copy().and(invertedOriginalMask));
        }

        int i = 0;
        for (Mask mask : masks) {
            if (mask.get(0, 0)) {
                boolean[] indexes = new boolean[masks.size()];
                indexes[i] = true;
                f2(mask.copy().or(originalMask), indexes, 0, masks);
            }
            i++;
        }
    }

    protected void f2(Mask mask, boolean[] indexes, int start, List<Mask> masks) {
        if (mask.isAllTrue()) {
            rawFullMatchesResults.add(indexes);
            return;
        }

        for (int i = start; i < indexes.length; i++) {
            if (mask.notIntersects(masks.get(i))) {
                boolean[] indexesCopy = Arrays.copyOf(indexes, indexes.length);
                indexesCopy[i] = true;
                f2(mask.copy().or(masks.get(i)), indexesCopy, i, masks);
            }
        }
    }
}
