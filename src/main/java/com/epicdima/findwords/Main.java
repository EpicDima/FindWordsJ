package com.epicdima.findwords;

import com.epicdima.findwords.base.Solver;
import com.epicdima.findwords.base.WordTrie;
import com.epicdima.findwords.solver.MultiThreadedSolver;
import com.epicdima.findwords.trie.ArrayWordTrie;
import com.epicdima.findwords.utils.Utils;

import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
//        String text = Matrices.matrix8x8;
        String text = "при\nтев\nдуб";

        WordTrie trie = ArrayWordTrie.createInstance(Utils.DEFAULT_DICTIONARY);

        Solver solver = new MultiThreadedSolver(trie);
        for (int i = 0; i < 10; i++) {
            solver.solve(text, 2, 100, true);
        }

        System.out.println("\n");
        System.out.println(solver.getWords()
                .stream()
                .map(wordAndMask -> wordAndMask.word.toString())
                .collect(Collectors.joining("\n")));
        System.out.println("\n\n");
        System.out.println(solver.getFullMatches()
                .stream()
                .map(array -> array
                        .stream()
                        .map(wordAndMask -> wordAndMask.word.toString())
                        .collect(Collectors.joining(" ")))
                .collect(Collectors.joining("\n")));
    }
}
