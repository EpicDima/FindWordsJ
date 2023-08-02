package com.epicdima.findwords.type;

import com.epicdima.findwords.solver.CoroutineSolver;
import com.epicdima.findwords.solver.DefaultSolver;
import com.epicdima.findwords.solver.FastSolver;
import com.epicdima.findwords.solver.ForkJoinSolver;
import com.epicdima.findwords.solver.MultiThreadedSolver;
import com.epicdima.findwords.solver.Solver;
import com.epicdima.findwords.solver.SolverInstantiator;
import com.epicdima.findwords.trie.WordTrie;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public enum SolverType implements SolverInstantiator {
    DEFAULT(DefaultSolver.class),
    FAST(FastSolver.class),
    MULTITHREADED(MultiThreadedSolver.class),
    FORKJOIN(ForkJoinSolver.class),
    COROUTINE(CoroutineSolver.class);

    private final MethodHandle createInstanceMH;

    SolverType(Class<?> solverClass) {
        try {
            createInstanceMH = MethodHandles.publicLookup()
                    .findConstructor(solverClass,
                            MethodType.methodType(void.class, String.class, WordTrie.class));
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Solver createInstance(String linesSeparator, WordTrie trie) {
        try {
            return (Solver) createInstanceMH.invoke(linesSeparator, trie);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }
}
