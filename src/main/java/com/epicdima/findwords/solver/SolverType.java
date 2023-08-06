package com.epicdima.findwords.solver;

import com.epicdima.findwords.trie.WordTrie;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public enum SolverType {
    DEFAULT(DefaultSolver.class),
    FAST(FastSolver.class),
    MULTITHREADED(MultiThreadedSolver.class),
    FORKJOIN(ForkJoinSolver.class),
    COROUTINE(CoroutineSolver.class);

    private final Class<? extends Solver> solverClass;

    private final MethodHandle createInstanceMH;

    SolverType(Class<? extends Solver> solverClass) {
        this.solverClass = solverClass;
        try {
            createInstanceMH = MethodHandles.publicLookup()
                    .findConstructor(solverClass, MethodType.methodType(void.class, String.class, WordTrie.class));
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Solver createInstance(String linesSeparator, WordTrie trie) {
        try {
            return (Solver) createInstanceMH.invoke(linesSeparator, trie);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    public Class<? extends Solver> getSolverClass() {
        return solverClass;
    }
}
