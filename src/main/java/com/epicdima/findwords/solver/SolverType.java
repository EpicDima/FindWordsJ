package com.epicdima.findwords.solver;

import androidx.annotation.NonNull;
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
    DEEP_RECURSION(DeepRecursionSolver.class);

    @NonNull
    private final Class<? extends Solver> solverClass;

    @NonNull
    private final MethodHandle createInstanceMH;

    SolverType(@NonNull Class<? extends Solver> solverClass) {
        this.solverClass = solverClass;
        try {
            createInstanceMH = MethodHandles.publicLookup()
                    .findConstructor(solverClass, MethodType.methodType(void.class, String.class, MaskType.class, WordTrie.class));
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @NonNull
    public final Solver createInstance(@NonNull String linesSeparator, @NonNull MaskType maskType, @NonNull WordTrie wordTrie) {
        try {
            return (Solver) createInstanceMH.invoke(linesSeparator, maskType, wordTrie);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    @NonNull
    public final Class<? extends Solver> getSolverClass() {
        return solverClass;
    }
}
