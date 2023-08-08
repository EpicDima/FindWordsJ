package com.epicdima.findwords.solver;

import com.epicdima.findwords.mask.Mask;
import com.epicdima.findwords.mask.MaskType;
import com.epicdima.findwords.trie.WordTrie;
import com.epicdima.findwords.utils.Utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class DefaultSolver implements Solver {
    private final List<WordAndMask> words = new ArrayList<>();
    protected final List<List<WordAndMask>> fullMatches = Collections.synchronizedList(new ArrayList<>());

    private final String linesSeparator;
    private final MaskType maskType;
    private final WordTrie wordTrie;

    private int minWordLength;
    private int maxWordLength;

    protected int rows;
    protected int cols;

    private char[][] matrix;
    protected Mask originalMask;

    public DefaultSolver(String linesSeparator, MaskType maskType, WordTrie wordTrie) {
        this.linesSeparator = linesSeparator;
        this.maskType = maskType;
        this.wordTrie = wordTrie;
    }

    private Mask createOriginalMask(int rows, int cols) {
        return maskType.createInstance(rows, cols);
    }

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

    private void preSolve(String matrixText, int minWordLength, int maxWordLength) {
        this.minWordLength = minWordLength;
        this.maxWordLength = maxWordLength;

        String[] lines = matrixText.split(Pattern.quote(linesSeparator));

        rows = lines.length;
        cols = lines[0].length();

        matrix = new char[rows][cols];
        originalMask = createOriginalMask(rows, cols);

        words.clear();
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

    @SuppressWarnings("DuplicatedCode")
    private void f(String word, int x, int y, Mask mask, Set<WordAndMask> wordsAndMasks) {
        if (word.length() >= minWordLength && wordTrie.containsWord(word)) {
            wordsAndMasks.add(new WordAndMask(word, mask.copy()));
        }

        if (word.length() > maxWordLength || !wordTrie.containsSubstring(word)) {
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

    private void findFullMatches() {
        ffff(words);
    }

    protected void ffff(List<WordAndMask> matchedWords) {
        List<WordAndMask>[][] matrix = createWordAndMaskMatrix(matchedWords);
        f2(originalMask.copy(), matrix, 0, new ArrayList<>());
    }

    protected List<WordAndMask>[][] createWordAndMaskMatrix(List<WordAndMask> matchedWords) {
        WordAndMask[] wordAndMasks = getRawMasks(matchedWords);

        //noinspection unchecked
        List<WordAndMask>[][] matrix = new List[rows][cols];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = new ArrayList<>();
                for (WordAndMask wordAndMask : wordAndMasks) {
                    if (wordAndMask.mask().get(i, j)) {
                        matrix[i][j].add(wordAndMask);
                    }
                }
            }
        }

        return matrix;
    }

    private WordAndMask[] getRawMasks(List<WordAndMask> matchedWords) {
        Mask invertedOriginalMask = originalMask.copy().invert();
        return matchedWords.stream()
                .map(matchedWord -> new WordAndMask(matchedWord.word(), matchedWord.mask().copy().and(invertedOriginalMask)))
                .toArray(WordAndMask[]::new);
    }

    protected void f2(Mask mask, List<WordAndMask>[][] matrix, int startIndex, List<WordAndMask> result) {
        if (mask.isAllTrue()) {
            fullMatches.add(new ArrayList<>(result));
            return;
        }

        for (int i = startIndex; i < rows * cols; i++) {
            if (mask.get(i)) {
                continue;
            }
            for (WordAndMask positionWordAndMask : matrix[i / cols][i % cols]) {
                if (mask.notIntersects(positionWordAndMask.mask())) {
                    result.add(positionWordAndMask);
                    f2(mask.or(positionWordAndMask.mask()), matrix, i + 1, result);
                    mask.xor(positionWordAndMask.mask());
                    result.remove(positionWordAndMask);
                }
            }
            break;
        }
    }
}
