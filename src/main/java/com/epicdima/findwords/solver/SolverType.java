package com.epicdima.findwords.solver;

import com.epicdima.findwords.mask.MaskType;
import com.epicdima.findwords.trie.WordTrie;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public enum SolverType {
    DEFAULT(DefaultSolver.class),
    MULTI_THREADED(MultiThreadedSolver.class),
    VIRTUAL_MULTI_THREADED(VirtualMultiThreadedSolver.class),
    FORKJOIN(ForkJoinSolver.class),
    COROUTINE(CoroutineSolver.class),
    RECURSIVE_COROUTINE(RecursiveCoroutineSolver.class);

    private final Class<? extends Solver> solverClass;

    private final MethodHandle createInstanceMH;

    SolverType(Class<? extends Solver> solverClass) {
        this.solverClass = solverClass;
        try {
            createInstanceMH = MethodHandles.publicLookup()
                    .findConstructor(solverClass, MethodType.methodType(void.class, String.class, MaskType.class, WordTrie.class));
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Solver createInstance(String linesSeparator, MaskType maskType, WordTrie wordTrie) {
        try {
            return (Solver) createInstanceMH.invoke(linesSeparator, maskType, wordTrie);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    public Class<? extends Solver> getSolverClass() {
        return solverClass;
    }
}
