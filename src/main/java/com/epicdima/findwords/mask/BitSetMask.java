package com.epicdima.findwords.mask;

import java.util.BitSet;

public class BitSetMask implements Mask {
    private final int rows;
    private final int cols;
    private final int size;

    private final BitSet bitSet;

    private BitSetMask(int rows, int cols, int size, BitSet bitSet) {
        this.rows = rows;
        this.cols = cols;
        this.size = size;
        this.bitSet = bitSet;
    }

    private BitSetMask(int rows, int cols, int size) {
        this(rows, cols, size, new BitSet(size));
    }

    public BitSetMask(int rows, int cols) {
        this(rows, cols, rows * cols);
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
        return new BitSetMask(rows, cols, size, (BitSet) bitSet.clone());
    }

    @Override
    public boolean isAllTrue() {
        return bitSet.cardinality() == size;
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
    public Mask xor(Mask another) {
        bitSet.xor(((BitSetMask) another).bitSet);
        return this;
    }

    @Override
    public Mask invert() {
        bitSet.flip(0, size);
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
        return rows == that.rows && cols == that.cols && size == that.size && bitSet.equals(that.bitSet);
    }

    @Override
    public int hashCode() {
        return bitSet.hashCode();
    }
}
