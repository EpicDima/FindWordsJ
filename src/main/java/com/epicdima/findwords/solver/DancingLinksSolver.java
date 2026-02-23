package com.epicdima.findwords.solver;

import androidx.annotation.NonNull;
import com.epicdima.findwords.mask.MaskType;
import com.epicdima.findwords.trie.WordTrie;
import java.util.ArrayList;
import java.util.List;

public class DancingLinksSolver extends DefaultSolver {

    private int[] L, R, U, D, C, S, RowOf;
    private int[] solutionStack;
    private List<WordAndMask> wordsRef;

    public DancingLinksSolver(@NonNull String linesSeparator, @NonNull MaskType maskType, @NonNull WordTrie wordTrie) {
        super(linesSeparator, maskType, wordTrie);
    }

    @Override
    protected void findFullMatches(@NonNull List<WordAndMask> matchedWords) {
        int wordsCount = matchedWords.size();
        if (wordsCount == 0) return;

        int totalCells = rows * cols;
        int[] cellToCol = new int[totalCells];
        int numCols = 0;

        for (int c = 0; c < totalCells; c++) {
            if (!originalMask.get(c)) {
                cellToCol[c] = ++numCols;
            }
        }

        if (numCols == 0) {
            fullMatches.add(new ArrayList<>());
            return;
        }

        int totalNodes = numCols + 1;
        for (WordAndMask wam : matchedWords) {
            for (int c = 0; c < totalCells; c++) {
                if (wam.mask().get(c)) {
                    totalNodes++;
                }
            }
        }

        L = new int[totalNodes];
        R = new int[totalNodes];
        U = new int[totalNodes];
        D = new int[totalNodes];
        C = new int[totalNodes];
        RowOf = new int[totalNodes];
        S = new int[numCols + 1];
        solutionStack = new int[wordsCount];
        wordsRef = matchedWords;

        for (int i = 0; i <= numCols; i++) {
            L[i] = i - 1;
            R[i] = i + 1;
            U[i] = i;
            D[i] = i;
            C[i] = i;
        }
        L[0] = numCols;
        R[numCols] = 0;

        int size = numCols;
        for (int w = 0; w < wordsCount; w++) {
            WordAndMask wam = matchedWords.get(w);
            int firstNode = -1;

            for (int c = 0; c < totalCells; c++) {
                if (wam.mask().get(c)) {
                    int col = cellToCol[c];
                    if (col == 0) continue;

                    int node = ++size;
                    RowOf[node] = w;
                    C[node] = col;

                    U[node] = U[col];
                    D[node] = col;
                    D[U[col]] = node;
                    U[col] = node;
                    S[col]++;

                    if (firstNode == -1) {
                        L[node] = node;
                        R[node] = node;
                        firstNode = node;
                    } else {
                        L[node] = L[firstNode];
                        R[node] = firstNode;
                        R[L[firstNode]] = node;
                        L[firstNode] = node;
                    }
                }
            }
        }

        search(0);
    }

    private void search(int depth) {
        if (R[0] == 0) {
            List<WordAndMask> match = new ArrayList<>(depth);
            for (int i = 0; i < depth; i++) {
                match.add(wordsRef.get(RowOf[solutionStack[i]]));
            }
            fullMatches.add(match);
            return;
        }

        int c = R[0];
        int minS = S[c];
        for (int i = R[c]; i != 0; i = R[i]) {
            if (S[i] < minS) {
                minS = S[i];
                c = i;
                if (minS <= 1) break;
            }
        }

        if (minS == 0) {
            return;
        }

        cover(c);

        for (int r = D[c]; r != c; r = D[r]) {
            solutionStack[depth] = r;

            for (int j = R[r]; j != r; j = R[j]) {
                cover(C[j]);
            }

            search(depth + 1);

            for (int j = L[r]; j != r; j = L[j]) {
                uncover(C[j]);
            }
        }

        uncover(c);
    }

    private void cover(int c) {
        L[R[c]] = L[c];
        R[L[c]] = R[c];
        for (int i = D[c]; i != c; i = D[i]) {
            for (int j = R[i]; j != i; j = R[j]) {
                U[D[j]] = U[j];
                D[U[j]] = D[j];
                S[C[j]]--;
            }
        }
    }

    private void uncover(int c) {
        for (int i = U[c]; i != c; i = U[i]) {
            for (int j = L[i]; j != i; j = L[j]) {
                S[C[j]]++;
                U[D[j]] = j;
                D[U[j]] = j;
            }
        }
        L[R[c]] = c;
        R[L[c]] = c;
    }
}
