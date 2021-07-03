package com.epicdima.findwords;

import com.epicdima.findwords.base.Mask;
import org.openjdk.jmh.annotations.*;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(value = 1)
@Warmup(iterations = 2, time = 5)
@Measurement(iterations = 5, time = 5)
@State(Scope.Benchmark)
public class MaskBenchmark {
    @Param({"2", "4", "8", "16", "32", "64", "128", "256"})
    public int size;

    @Param({
            "com.epicdima.findwords.mask.BooleanMask",
            "com.epicdima.findwords.mask.BitSetMask",
            "com.epicdima.findwords.mask.FlatBooleanMask",
    })
    public String maskClass;

    private int middleIndex;
    private int lastIndex;
    private MethodHandle constructor;
    private Mask mask;
    private Mask mask2;

    @Setup
    public void setup() throws Throwable {
        middleIndex = size / 2;
        lastIndex = size - 1;

        constructor = MethodHandles.publicLookup()
                .findConstructor(Class.forName(maskClass),
                        MethodType.methodType(void.class, int.class, int.class));

        mask = (Mask) constructor.invokeExact(size, size);

        mask2 = mask2.copy();
        mask2.set(lastIndex, lastIndex, true);
    }

    @Benchmark
    public Object init() throws Throwable {
        return constructor.invokeExact(size, size);
    }

    @Benchmark
    public boolean getFirst() {
        return mask.get(0, 0);
    }

    @Benchmark
    public boolean getMiddle() {
        return mask.get(middleIndex, middleIndex);
    }

    @Benchmark
    public boolean getLast() {
        return mask.get(lastIndex, lastIndex);
    }

    @Benchmark
    public void setFirst() {
        mask.set(0, 0, true);
    }

    @Benchmark
    public void setMiddle() {
        mask.set(middleIndex, middleIndex, true);
    }

    @Benchmark
    public void setLast() {
        mask.set(lastIndex, lastIndex, true);
    }

    @Benchmark
    public Mask copy() {
        return mask.copy();
    }

    @Benchmark
    public boolean isAllTrue() {
        return mask.isAllTrue();
    }

    @Benchmark
    public boolean isAllFalse() {
        return mask.isAllFalse();
    }

    @Benchmark
    public Mask and() {
        return mask.and(mask);
    }

    @Benchmark
    public Mask or() {
        return mask.or(mask);
    }

    @Benchmark
    public Mask invert() {
        return mask.invert();
    }

    @Benchmark
    public int hashCodeBenchmark() {
        return mask.hashCode();
    }

    @Benchmark
    public boolean equalsBenchmark() {
        return mask.equals(mask2);
    }
}
