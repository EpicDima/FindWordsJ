package com.epicdima.findwords.mask;

import androidx.annotation.NonNull;
import java.util.BitSet;

public final class BitSetMask implements Mask {
    private final int rows;
    private final int cols;
    private final int size;

    @NonNull
    private final BitSet bitSet;

    private BitSetMask(int rows, int cols, int size, @NonNull BitSet bitSet) {
        this.rows = rows;
        this.cols = cols;
        this.size = size;
        this.bitSet = bitSet;
    }

    private BitSetMask(int rows, int cols, int size) {
        this(rows, cols, size, new BitSet(size));
    }

    @SuppressWarnings("unused") // used via MethodHandle
    public BitSetMask(int rows, int cols) {
        this(rows, cols, rows * cols);
    }

    @Override
    public boolean get(int index) {
        return bitSet.get(index);
    }

    @Override
    public boolean get(int i, int j) {
        return bitSet.get(i * cols + j);
    }

    @Override
    public void set(int i, int j, boolean value) {
        bitSet.set(i * cols + j, value);
    }

    @NonNull
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

    @NonNull
    @Override
    public Mask and(@NonNull Mask another) {
        bitSet.and(((BitSetMask) another).bitSet);
        return this;
    }

    @NonNull
    @Override
    public Mask or(@NonNull Mask another) {
        bitSet.or(((BitSetMask) another).bitSet);
        return this;
    }

    @NonNull
    @Override
    public Mask xor(@NonNull Mask another) {
        bitSet.xor(((BitSetMask) another).bitSet);
        return this;
    }

    @NonNull
    @Override
    public Mask invert() {
        bitSet.flip(0, size);
        return this;
    }

    @Override
    public boolean notIntersects(@NonNull Mask another) {
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
