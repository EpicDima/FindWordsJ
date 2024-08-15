package com.epicdima.findwords.solver;

import androidx.annotation.NonNull;
import java.util.List;

public interface Solver {

    void solve(@NonNull String matrixText, int minWordLength, int maxWordLength, boolean fullMatch);

    @NonNull
    List<WordAndMask> getWords();

    @NonNull
    List<List<WordAndMask>> getFullMatches();
}
