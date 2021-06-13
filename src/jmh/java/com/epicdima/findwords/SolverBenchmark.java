package com.epicdima.findwords;

import com.epicdima.findwords.base.Solver;
import com.epicdima.findwords.base.WordTrie;
import com.epicdima.findwords.solver.DefaultSolver;
import com.epicdima.findwords.solver.FastSolver;
import com.epicdima.findwords.solver.ForkJoinSolver;
import com.epicdima.findwords.solver.MultiThreadedSolver;
import com.epicdima.findwords.trie.ArrayWordTrie;
import com.epicdima.findwords.trie.HashWordTrie;
import com.epicdima.findwords.utils.Matrices;
import com.epicdima.findwords.utils.Utils;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(value = 1)
@Warmup(iterations = 1, time = 5)
@Measurement(iterations = 3, time = 5)
@State(Scope.Benchmark)
public class SolverBenchmark {
    private final WordTrie hashWordTrie = HashWordTrie.createInstance(Utils.DEFAULT_DICTIONARY);
    private final WordTrie arrayWordTrie = ArrayWordTrie.createInstance(Utils.DEFAULT_DICTIONARY);

    private final Solver defaultSolverHash = new DefaultSolver(hashWordTrie);
    private final Solver defaultSolverArray = new DefaultSolver(arrayWordTrie);
    private final Solver fastSolverHash = new FastSolver(hashWordTrie);
    private final Solver fastSolverArray = new FastSolver(arrayWordTrie);
    private final Solver multiThreadedSolverHash = new MultiThreadedSolver(hashWordTrie);
    private final Solver multiThreadedSolverArray = new MultiThreadedSolver(arrayWordTrie);
    private final Solver forkJoinSolverHash = new ForkJoinSolver(hashWordTrie);
    private final Solver forkJoinSolverArray = new ForkJoinSolver(arrayWordTrie);

    @Benchmark
    public void defaultSolverHash_false_1() {
        defaultSolverHash.solve(Matrices.matrix8x8, 1, 100, false);
    }

    @Benchmark
    public void defaultSolverArray_false_1() {
        defaultSolverArray.solve(Matrices.matrix8x8, 1, 100, false);
    }

    @Benchmark
    public void fastSolverHash_false_1() {
        fastSolverHash.solve(Matrices.matrix8x8, 1, 100, false);
    }

    @Benchmark
    public void fastSolverArray_false_1() {
        fastSolverArray.solve(Matrices.matrix8x8, 1, 100, false);
    }

    @Benchmark
    public void multiThreadedSolverHash_false_1() {
        multiThreadedSolverHash.solve(Matrices.matrix8x8, 1, 100, false);
    }

    @Benchmark
    public void multiThreadedSolverArray_false_1() {
        multiThreadedSolverArray.solve(Matrices.matrix8x8, 1, 100, false);
    }

    @Benchmark
    public void forkJoinSolverHash_false_1() {
        forkJoinSolverHash.solve(Matrices.matrix8x8, 1, 100, false);
    }

    @Benchmark
    public void forkJoinSolverArray_false_1() {
        forkJoinSolverArray.solve(Matrices.matrix8x8, 1, 100, false);
    }

    @Benchmark
    public void defaultSolverHash_false_2() {
        defaultSolverHash.solve(Matrices.matrix23x26, 1, 100, false);
    }

    @Benchmark
    public void defaultSolverArray_false_2() {
        defaultSolverArray.solve(Matrices.matrix23x26, 1, 100, false);
    }

    @Benchmark
    public void fastSolverHash_false_2() {
        fastSolverHash.solve(Matrices.matrix23x26, 1, 100, false);
    }

    @Benchmark
    public void fastSolverArray_false_2() {
        fastSolverArray.solve(Matrices.matrix23x26, 1, 100, false);
    }

    @Benchmark
    public void multiThreadedSolverHash_false_2() {
        multiThreadedSolverHash.solve(Matrices.matrix23x26, 1, 100, false);
    }

    @Benchmark
    public void multiThreadedSolverArray_false_2() {
        multiThreadedSolverArray.solve(Matrices.matrix23x26, 1, 100, false);
    }

    @Benchmark
    public void forkJoinSolverHash_false_2() {
        forkJoinSolverHash.solve(Matrices.matrix23x26, 1, 100, false);
    }

    @Benchmark
    public void forkJoinSolverArray_false_2() {
        forkJoinSolverArray.solve(Matrices.matrix23x26, 1, 100, false);
    }

    @Benchmark
    public void defaultSolverHash_true_1() {
        defaultSolverHash.solve(Matrices.matrix8x8, 4, 100, true);
    }

    @Benchmark
    public void defaultSolverArray_true_1() {
        defaultSolverArray.solve(Matrices.matrix8x8, 4, 100, true);
    }

    @Benchmark
    public void fastSolverHash_true_1() {
        fastSolverHash.solve(Matrices.matrix8x8, 4, 100, true);
    }

    @Benchmark
    public void fastSolverArray_true_1() {
        fastSolverArray.solve(Matrices.matrix8x8, 4, 100, true);
    }

    @Benchmark
    public void multiThreadedSolverHash_true_1() {
        multiThreadedSolverHash.solve(Matrices.matrix8x8, 4, 100, true);
    }

    @Benchmark
    public void multiThreadedSolverArray_true_1() {
        multiThreadedSolverArray.solve(Matrices.matrix8x8, 4, 100, true);
    }

    @Benchmark
    public void forkJoinSolverHash_true_1() {
        forkJoinSolverHash.solve(Matrices.matrix8x8, 4, 100, true);
    }

    @Benchmark
    public void forkJoinSolverArray_true_1() {
        forkJoinSolverArray.solve(Matrices.matrix8x8, 4, 100, true);
    }

    @Benchmark
    public void defaultSolverHash_true_2() {
        defaultSolverHash.solve(Matrices.matrix11x8, 5, 100, true);
    }

    @Benchmark
    public void defaultSolverArray_true_2() {
        defaultSolverArray.solve(Matrices.matrix11x8, 5, 100, true);
    }

    @Benchmark
    public void fastSolverHash_true_2() {
        fastSolverHash.solve(Matrices.matrix11x8, 5, 100, true);
    }

    @Benchmark
    public void fastSolverArray_true_2() {
        fastSolverArray.solve(Matrices.matrix11x8, 5, 100, true);
    }

    @Benchmark
    public void multiThreadedSolverHash_true_2() {
        multiThreadedSolverHash.solve(Matrices.matrix11x8, 5, 100, true);
    }

    @Benchmark
    public void multiThreadedSolverArray_true_2() {
        multiThreadedSolverArray.solve(Matrices.matrix11x8, 5, 100, true);
    }

    @Benchmark
    public void forkJoinSolverHash_true_2() {
        forkJoinSolverHash.solve(Matrices.matrix11x8, 5, 100, true);
    }

    @Benchmark
    public void forkJoinSolverArray_true_2() {
        forkJoinSolverArray.solve(Matrices.matrix11x8, 5, 100, true);
    }
}
