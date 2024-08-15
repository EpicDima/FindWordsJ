package com.epicdima.findwords.mask;

import androidx.annotation.NonNull;
import java.util.Arrays;

public final class FlatBooleanMask implements Mask {
    private final int rows;
    private final int cols;
    private final int size;

    @NonNull
    private final boolean[] flatMatrix;

    private FlatBooleanMask(int rows, int cols, int size, @NonNull boolean[] flatMatrix) {
        this.rows = rows;
        this.cols = cols;
        this.size = size;
        this.flatMatrix = flatMatrix;
    }

    private FlatBooleanMask(int rows, int cols, int size) {
        this(rows, cols, size, new boolean[size]);
    }

    @SuppressWarnings("unused") // used via MethodHandle
    public FlatBooleanMask(int rows, int cols) {
        this(rows, cols, rows * cols);
    }

    @Override
    public boolean get(int index) {
        return flatMatrix[index];
    }

    @Override
    public boolean get(int i, int j) {
        return flatMatrix[i * cols + j];
    }

    @Override
    public void set(int i, int j, boolean value) {
        flatMatrix[i * cols + j] = value;
    }

    @NonNull
    @Override
    public Mask copy() {
        return new FlatBooleanMask(rows, cols, size, Arrays.copyOf(flatMatrix, size));
    }

    @Override
    public boolean isAllTrue() {
        for (int i = 0; i < size; i++) {
            if (!flatMatrix[i]) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean isAllFalse() {
        for (int i = 0; i < size; i++) {
            if (flatMatrix[i]) {
                return false;
            }
        }

        return true;
    }

    @NonNull
    @Override
    public Mask and(@NonNull Mask another) {
        for (int i = 0; i < size; i++) {
            flatMatrix[i] &= ((FlatBooleanMask) another).flatMatrix[i];
        }

        return this;
    }

    @NonNull
    @Override
    public Mask or(@NonNull Mask another) {
        for (int i = 0; i < size; i++) {
            flatMatrix[i] |= ((FlatBooleanMask) another).flatMatrix[i];
        }

        return this;
    }

    @NonNull
    @Override
    public Mask xor(@NonNull Mask another) {
        for (int i = 0; i < size; i++) {
            flatMatrix[i] ^= ((FlatBooleanMask) another).flatMatrix[i];
        }

        return this;
    }

    @NonNull
    @Override
    public Mask invert() {
        for (int i = 0; i < size; i++) {
            flatMatrix[i] = !flatMatrix[i];
        }

        return this;
    }

    @Override
    public boolean notIntersects(@NonNull Mask another) {
        for (int i = 0; i < size; i++) {
            if (flatMatrix[i] && ((FlatBooleanMask) another).flatMatrix[i]) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlatBooleanMask that = (FlatBooleanMask) o;
        return rows == that.rows && cols == that.cols && size == that.size && Arrays.equals(flatMatrix, that.flatMatrix);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(flatMatrix);
    }
}
