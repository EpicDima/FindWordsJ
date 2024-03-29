package com.epicdima.findwords.solver;

import java.util.List;

public interface Solver {

    void solve(String matrixText, int minWordLength, int maxWordLength, boolean fullMatch);

    List<WordAndMask> getWords();

    List<List<WordAndMask>> getFullMatches();
}
