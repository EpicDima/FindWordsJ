package com.epicdima.findwords;

import com.epicdima.findwords.solver.Solver;
import com.epicdima.findwords.solver.SolverType;
import com.epicdima.findwords.trie.WordTrie;
import com.epicdima.findwords.trie.WordTrieType;
import com.epicdima.findwords.utils.BenchmarkUtils;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import static com.epicdima.findwords.utils.Matrices.MATRIX_10_X_10;
import static com.epicdima.findwords.utils.Matrices.MATRIX_11_X_8;
import static com.epicdima.findwords.utils.Matrices.MATRIX_19_X_4;
import static com.epicdima.findwords.utils.Matrices.MATRIX_23_X_26;
import static com.epicdima.findwords.utils.Matrices.MATRIX_8_X_8;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(value = 1)
@Warmup(iterations = 1, time = 8)
@Measurement(iterations = 3, time = 8)
@State(Scope.Benchmark)
public class SolverBenchmark {
    private static final String LINES_SEPARATOR = "\n";

    @Param({
            "HASH",
            "ARRAY",
            "SET",
    })
    public String wordTrieTypeName;

    @Param({
            "DEFAULT",
            "FAST",
            "MULTITHREADED",
            "FORKJOIN",
            "COROUTINE",
    })
    public String solverTypeName;

    private Solver solver;

    @Setup
    public void setup() {
        WordTrie wordTrie = WordTrieType.valueOf(wordTrieTypeName).createInstance(BenchmarkUtils.DEFAULT_DICTIONARY);
        solver = SolverType.valueOf(solverTypeName).createInstance(LINES_SEPARATOR, wordTrie);
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
