package com.epicdima.findwords.solver;

import androidx.annotation.NonNull;
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
    @NonNull
    private final List<WordAndMask> words = new ArrayList<>();
    @NonNull
    protected final List<List<WordAndMask>> fullMatches = Collections.synchronizedList(new ArrayList<>());

    @NonNull
    private final String linesSeparator;
    @NonNull
    private final MaskType maskType;
    @NonNull
    protected final WordTrie wordTrie;

    protected int minWordLength;
    protected int maxWordLength;

    protected int rows;
    protected int cols;

    @NonNull
    protected char[][] matrix = new char[0][0];
    @NonNull
    protected Mask originalMask;

    public DefaultSolver(@NonNull String linesSeparator, @NonNull MaskType maskType, @NonNull WordTrie wordTrie) {
        this.linesSeparator = linesSeparator;
        this.maskType = maskType;
        this.wordTrie = wordTrie;
        this.originalMask = createOriginalMask(0, 0);
    }

    @NonNull
    private Mask createOriginalMask(int rows, int cols) {
        return maskType.createInstance(rows, cols);
    }

    @Override
    public void solve(@NonNull String matrixText, int minWordLength, int maxWordLength, boolean fullMatch) {
        preSolve(matrixText, minWordLength, maxWordLength);

        words.addAll(findWords());

        Mask invertedOriginalMask = originalMask.copy().invert();
        for (WordAndMask wordAndMask : words) {
            wordAndMask.mask().and(invertedOriginalMask);
        }

        if (fullMatch) {
            findFullMatches(words);
        }
    }

    private void preSolve(@NonNull String matrixText, int minWordLength, int maxWordLength) {
        this.minWordLength = minWordLength;
        this.maxWordLength = maxWordLength;

        String[] lines = matrixText.split(Pattern.quote(linesSeparator));

        rows = lines.length;
        if (rows == 0) {
            throw new IllegalArgumentException("No rows in entered text");
        }
        cols = lines[0].length();
        if (cols == 0) {
            throw new IllegalArgumentException("No cols in entered text");
        }
        for (String line : lines) {
            if (line.length() != cols) {
                throw new IllegalArgumentException("The text is not a grid");
            }
        }

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

    @NonNull
    @Override
    public List<WordAndMask> getWords() {
        return new ArrayList<>(words);
    }

    @NonNull
    @Override
    public List<List<WordAndMask>> getFullMatches() {
        return new ArrayList<>(fullMatches);
    }

    @NonNull
    protected Set<WordAndMask> findWords() {
        Set<WordAndMask> wordsAndMasks = new HashSet<>();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (!originalMask.get(i, j)) {
                    WordTrie.Cursor cursor = wordTrie.cursor();
                    if (cursor != null) {
                        originalMask.set(i, j, true);
                        char ch = matrix[i][j];
                        if (cursor.push(ch)) {
                            sb.append(ch);
                            f(sb, i, j, originalMask, wordsAndMasks, cursor);
                            sb.setLength(0);
                            cursor.pop();
                        }
                        originalMask.set(i, j, false);
                    }
                }
            }
        }

        return wordsAndMasks;
    }

    private void f(@NonNull StringBuilder sb, int x, int y, @NonNull Mask mask, @NonNull Set<WordAndMask> wordsAndMasks, @NonNull WordTrie.Cursor cursor) {
        if (sb.length() >= minWordLength && cursor.isWord()) {
            wordsAndMasks.add(new WordAndMask(sb.toString(), mask.copy()));
        }

        if (sb.length() >= maxWordLength) {
            return;
        }

        int x2 = x + 1;
        if (x2 < rows && !mask.get(x2, y)) {
            char ch = matrix[x2][y];
            if (cursor.push(ch)) {
                mask.set(x2, y, true);
                sb.append(ch);
                f(sb, x2, y, mask, wordsAndMasks, cursor);
                sb.setLength(sb.length() - 1);
                mask.set(x2, y, false);
                cursor.pop();
            }
        }

        x2 = x - 1;
        if (x2 >= 0 && !mask.get(x2, y)) {
            char ch = matrix[x2][y];
            if (cursor.push(ch)) {
                mask.set(x2, y, true);
                sb.append(ch);
                f(sb, x2, y, mask, wordsAndMasks, cursor);
                sb.setLength(sb.length() - 1);
                mask.set(x2, y, false);
                cursor.pop();
            }
        }

        int y2 = y + 1;
        if (y2 < cols && !mask.get(x, y2)) {
            char ch = matrix[x][y2];
            if (cursor.push(ch)) {
                mask.set(x, y2, true);
                sb.append(ch);
                f(sb, x, y2, mask, wordsAndMasks, cursor);
                sb.setLength(sb.length() - 1);
                mask.set(x, y2, false);
                cursor.pop();
            }
        }

        y2 = y - 1;
        if (y2 >= 0 && !mask.get(x, y2)) {
            char ch = matrix[x][y2];
            if (cursor.push(ch)) {
                mask.set(x, y2, true);
                sb.append(ch);
                f(sb, x, y2, mask, wordsAndMasks, cursor);
                sb.setLength(sb.length() - 1);
                mask.set(x, y2, false);
                cursor.pop();
            }
        }
    }

    protected void findFullMatches(@NonNull List<WordAndMask> matchedWords) {
        List<WordAndMask>[][] matrix = createWordAndMaskMatrix(matchedWords);
        f2(originalMask.copy(), matrix, new ArrayList<>());
    }

    @NonNull
    protected List<WordAndMask>[][] createWordAndMaskMatrix(@NonNull List<WordAndMask> matchedWords) {
        @SuppressWarnings("unchecked")
        List<WordAndMask>[][] matrix = new List[rows][cols];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = new ArrayList<>();
                for (WordAndMask wordAndMask : matchedWords) {
                    if (wordAndMask.mask().get(i, j)) {
                        matrix[i][j].add(wordAndMask);
                    }
                }
            }
        }

        return matrix;
    }

    protected void f2(@NonNull Mask mask, @NonNull List<WordAndMask>[][] matrix, @NonNull List<WordAndMask> result) {
        if (mask.isAllTrue()) {
            fullMatches.add(new ArrayList<>(result));
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
            for (WordAndMask positionWordAndMask : matrix[targetIndex / cols][targetIndex % cols]) {
                if (mask.notIntersects(positionWordAndMask.mask())) {
                    result.add(positionWordAndMask);
                    f2(mask.or(positionWordAndMask.mask()), matrix, result);
                    mask.xor(positionWordAndMask.mask());
                    result.removeLast();
                }
            }
        }
    }
}
