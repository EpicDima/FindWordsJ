package com.epicdima.findwords;

import com.epicdima.findwords.base.Solver;
import com.epicdima.findwords.base.WordTrie;
import com.epicdima.findwords.utils.Utils;
import org.openjdk.jmh.annotations.*;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.concurrent.TimeUnit;

import static com.epicdima.findwords.Matrices.*;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(value = 1)
@Warmup(iterations = 2, time = 10)
@Measurement(iterations = 5, time = 10)
@State(Scope.Benchmark)
public class SolverBenchmark {
    private static final String LINES_SEPARATOR = "\n";

    @Param({
            "com.epicdima.findwords.trie.HashWordTrie",
            "com.epicdima.findwords.trie.ArrayWordTrie",
    })
    public String wordTrieClass;

    @Param({
            "com.epicdima.findwords.solver.DefaultSolver",
            "com.epicdima.findwords.solver.FastSolver",
            "com.epicdima.findwords.solver.MultiThreadedSolver",
            "com.epicdima.findwords.solver.ForkJoinSolver",
    })
    public String solverClass;

    private Solver solver;

    @Setup
    public void setup() throws Throwable {
        WordTrie wordTrie = (WordTrie) MethodHandles.publicLookup()
                .findStatic(Class.forName(wordTrieClass), "createInstance",
                        MethodType.methodType(WordTrie.class, String.class))
                .invokeExact(Utils.DEFAULT_DICTIONARY);

        solver = (Solver) MethodHandles.publicLookup()
                .findConstructor(Class.forName(solverClass),
                        MethodType.methodType(void.class, String.class, WordTrie.class))
                .invokeExact(LINES_SEPARATOR, wordTrie);
    }

    @Benchmark
    public void matrix8x8_1_100() {
        solver.solve(MATRIX_8_X_8, 1, 100, false);
    }

    @Benchmark
    public void matrix10x10_1_100() {
        solver.solve(MATRIX_10_X_10, 1, 100, false);
    }

    @Benchmark
    public void matrix23x26_1_100() {
        solver.solve(MATRIX_23_X_26, 1, 100, false);
    }

    @Benchmark
    public void matrix19x4_1_100() {
        solver.solve(MATRIX_19_X_4, 1, 100, false);
    }

    @Benchmark
    public void matrix11x8_1_100() {
        solver.solve(MATRIX_11_X_8, 1, 100, false);
    }

    @Benchmark
    public void matrix8x8_4_100_fullMatch() {
        solver.solve(MATRIX_8_X_8, 4, 100, true);
    }

    @Benchmark
    public void matrix11x8_5_100_fullMatch() {
        solver.solve(MATRIX_11_X_8, 5, 100, true);
    }
}
