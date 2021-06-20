package com.epicdima.findwords.mask;

import com.epicdima.findwords.base.Mask;

import java.util.BitSet;
import java.util.Objects;

public class BitSetMask implements Mask {
    private final int rows;
    private final int cols;

    private final BitSet bitSet;

    private BitSetMask(int rows, int cols, BitSet bitSet) {
        this.rows = rows;
        this.cols = cols;
        this.bitSet = bitSet;
    }

    public BitSetMask(int rows, int cols) {
        this(rows, cols, new BitSet(rows * cols));
    }

    @Override
    public boolean get(int i, int j) {
        return bitSet.get(i * cols + j);
    }

    @Override
    public void set(int i, int j, boolean value) {
        bitSet.set(i * cols + j, value);
    }

    @Override
    public Mask copy() {
        return new BitSetMask(rows, cols, (BitSet) bitSet.clone());
    }

    @Override
    public boolean isAllTrue() {
        return bitSet.cardinality() == rows * cols;
    }

    @Override
    public boolean isAllFalse() {
        return bitSet.isEmpty();
    }

    @Override
    public Mask and(Mask another) {
        bitSet.and(((BitSetMask) another).bitSet);
        return this;
    }

    @Override
    public Mask or(Mask another) {
        bitSet.or(((BitSetMask) another).bitSet);
        return this;
    }

    @Override
    public Mask invert() {
        bitSet.flip(0, rows * cols);
        return this;
    }

    @Override
    public boolean notIntersects(Mask another) {
        return !bitSet.intersects(((BitSetMask) another).bitSet);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BitSetMask that = (BitSetMask) o;
        return rows == that.rows && cols == that.cols && bitSet.equals(that.bitSet);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(rows, cols);
        result = 31 * result + bitSet.hashCode();
        return result;
    }
}
