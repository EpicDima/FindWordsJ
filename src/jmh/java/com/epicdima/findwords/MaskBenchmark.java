package com.epicdima.findwords;

import com.epicdima.findwords.base.Mask;
import com.epicdima.findwords.mask.BitSetMask;
import com.epicdima.findwords.mask.BooleanMask;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(value = 1)
@Warmup(iterations = 1, time = 5)
@Measurement(iterations = 3, time = 5)
@State(Scope.Benchmark)
public class MaskBenchmark {
    @Param({"2", "4", "8", "16", "32", "64", "128"})
    public int size;

    private final Mask booleanMask = new BooleanMask(size, size);
    private final Mask bitSetMask = new BitSetMask(size, size);

    @Benchmark
    public Mask initBoolean() {
        return new BooleanMask(size, size);
    }

    @Benchmark
    public Mask initBitSet() {
        return new BitSetMask(size, size);
    }

    @Benchmark
    public boolean getFirstBoolean() {
        return booleanMask.get(0, 0);
    }

    @Benchmark
    public boolean getFirstBitSet() {
        return bitSetMask.get(0, 0);
    }

    @Benchmark
    public boolean getLastBoolean() {
        return booleanMask.get(size - 1, size - 1);
    }

    @Benchmark
    public boolean getLastBitSet() {
        return bitSetMask.get(size - 1, size - 1);
    }

    @Benchmark
    public boolean getMiddleBoolean() {
        return booleanMask.get(size / 2, size / 2);
    }

    @Benchmark
    public boolean getMiddleBitSet() {
        return bitSetMask.get(size / 2, size / 2);
    }

    @Benchmark
    public void setFirstBoolean() {
        booleanMask.set(0, 0, true);
    }

    @Benchmark
    public void setFirstBitSet() {
        bitSetMask.set(0, 0, true);
    }

    @Benchmark
    public void setLastBoolean() {
        booleanMask.set(size - 1, size - 1, true);
    }

    @Benchmark
    public void setLastBitSet() {
        bitSetMask.set(size - 1, size - 1, true);
    }

    @Benchmark
    public void setMiddleBoolean() {
        booleanMask.set(size / 2, size / 2, true);
    }

    @Benchmark
    public void setMiddleBitSet() {
        bitSetMask.set(size / 2, size / 2, true);
    }

    @Benchmark
    public Mask copyBoolean() {
        return booleanMask.copy();
    }

    @Benchmark
    public Mask copyBitSet() {
        return bitSetMask.copy();
    }

    @Benchmark
    public boolean isAllTrueBoolean() {
        return booleanMask.isAllTrue();
    }

    @Benchmark
    public boolean isAllTrueBitSet() {
        return bitSetMask.isAllTrue();
    }

    @Benchmark
    public boolean isAllFalseBoolean() {
        return booleanMask.isAllFalse();
    }

    @Benchmark
    public boolean isAllFalseBitSet() {
        return bitSetMask.isAllFalse();
    }

    @Benchmark
    public Mask andBoolean() {
        return booleanMask.and(booleanMask);
    }

    @Benchmark
    public Mask andBitSet() {
        return bitSetMask.and(bitSetMask);
    }

    @Benchmark
    public Mask orBoolean() {
        return booleanMask.or(booleanMask);
    }

    @Benchmark
    public Mask orBitSet() {
        return bitSetMask.or(bitSetMask);
    }

    @Benchmark
    public Mask invertBoolean() {
        return booleanMask.invert();
    }

    @Benchmark
    public Mask invertBitSet() {
        return bitSetMask.invert();
    }
}
