package com.epicdima.findwords;

import com.epicdima.findwords.mask.Mask;
import com.epicdima.findwords.mask.MaskType;
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
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused") // used because the benchmark
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(value = 1)
@Warmup(iterations = 1, time = 4)
@Measurement(iterations = 3, time = 4)
@State(Scope.Benchmark)
public class MaskBenchmark {
    @Param({"8", "128"})
    public int size;

    @Param({
            "BOOLEAN",
            "FLAT",
            "BITSET",
    })
    public String maskTypeName;

    private MaskType maskType;

    private int middleIndex;
    private int lastIndex;
    private Mask mask;
    private Mask mask2;

    @Setup
    public void setup() {
        middleIndex = size / 2;
        lastIndex = size - 1;

        maskType = MaskType.valueOf(maskTypeName);

        mask = maskType.createInstance(size, size);

        mask2 = mask.copy();
        mask2.set(lastIndex, lastIndex, true);
    }

    @Benchmark
    public Object init() {
        return maskType.createInstance(size, size);
    }

    @Benchmark
    public boolean getFirst1() {
        return mask.get(0);
    }

    @Benchmark
    public boolean getFirst2() {
        return mask.get(0, 0);
    }

    @Benchmark
    public boolean getMiddle1() {
        return mask.get(middleIndex * size + middleIndex);
    }

    @Benchmark
    public boolean getMiddle2() {
        return mask.get(middleIndex, middleIndex);
    }

    @Benchmark
    public boolean getLast1() {
        return mask.get(lastIndex * size + lastIndex);
    }

    @Benchmark
    public boolean getLast2() {
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
        return mask.and(mask2);
    }

    @Benchmark
    public Mask or() {
        return mask.or(mask2);
    }

    @Benchmark
    public Mask xor() {
        return mask.xor(mask2);
    }

    @Benchmark
    public Mask invert() {
        return mask.invert();
    }

    @Benchmark
    public boolean notIntersects() {
        return mask.notIntersects(mask2);
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
