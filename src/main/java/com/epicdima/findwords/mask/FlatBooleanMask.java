package com.epicdima.findwords.mask;

import com.epicdima.findwords.base.Mask;

import java.util.Arrays;
import java.util.Objects;

public class FlatBooleanMask implements Mask {
    private final int rows;
    private final int cols;

    private final boolean[] flatMatrix;

    private FlatBooleanMask(int rows, int cols, boolean[] flatMatrix) {
        this.rows = rows;
        this.cols = cols;
        this.flatMatrix = flatMatrix;
    }

    public FlatBooleanMask(int rows, int cols) {
        this(rows, cols, new boolean[rows * cols]);
    }

    @Override
    public boolean get(int i, int j) {
        return flatMatrix[i * cols + j];
    }

    @Override
    public void set(int i, int j, boolean value) {
        flatMatrix[i * cols + j] = value;
    }

    @Override
    public Mask copy() {
        return new FlatBooleanMask(rows, cols, Arrays.copyOf(flatMatrix, flatMatrix.length));
    }

    @Override
    public boolean isAllTrue() {
        for (boolean value : flatMatrix) {
            if (!value) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean isAllFalse() {
        for (boolean value : flatMatrix) {
            if (value) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Mask and(Mask another) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                flatMatrix[i * cols + j] &= another.get(i, j);
            }
        }

        return this;
    }

    @Override
    public Mask or(Mask another) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                flatMatrix[i * cols + j] |= another.get(i, j);
            }
        }

        return this;
    }

    @Override
    public Mask invert() {
        for (int i = 0; i < rows * cols; i++) {
            flatMatrix[i] = !flatMatrix[i];
        }

        return this;
    }

    @Override
    public boolean notIntersects(Mask another) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (flatMatrix[i * cols + j] && another.get(i, j)) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlatBooleanMask that = (FlatBooleanMask) o;
        return rows == that.rows && cols == that.cols && Arrays.equals(flatMatrix, that.flatMatrix);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(rows, cols);
        result = 31 * result + Arrays.hashCode(flatMatrix);
        return result;
    }
}
