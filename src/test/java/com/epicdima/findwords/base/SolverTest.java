package com.epicdima.findwords.base;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

public abstract class SolverTest {

    private final Solver solver = createSolver();

    protected abstract Solver createSolver();

    @Test
    public void test1() {
        String text = "при\nтев\nдуб";

        solver.solve(text, 1, 10, false);

        List<CharSequence> words = solver.getWords().stream().map(wordAndMask -> wordAndMask.word).collect(Collectors.toList());

        Assertions.assertTrue(words.contains("привет"));
        Assertions.assertTrue(words.contains("дуб"));
    }
}
