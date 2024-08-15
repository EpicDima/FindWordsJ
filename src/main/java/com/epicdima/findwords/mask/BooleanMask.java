package com.epicdima.findwords.mask;

import androidx.annotation.NonNull;
import java.util.Arrays;

public final class BooleanMask implements Mask {
    private final int rows;
    private final int cols;

    @NonNull
    private final boolean[][] matrix;

    private BooleanMask(int rows, int cols, @NonNull boolean[][] matrix) {
        this.rows = rows;
        this.cols = cols;
        this.matrix = matrix;
    }

    @SuppressWarnings("unused") // used via MethodHandle
    public BooleanMask(int rows, int cols) {
        this(rows, cols, new boolean[rows][cols]);
    }

    private static boolean[][] deepCopy(boolean[][] original) {
        final boolean[][] result = new boolean[original.length][];
        for (int i = 0; i < original.length; i++) {
            result[i] = Arrays.copyOf(original[i], original[i].length);
        }

        return result;
    }

    @Override
    public boolean get(int index) {
        return matrix[index / cols][index % cols];
    }

    @Override
    public boolean get(int i, int j) {
        return matrix[i][j];
    }

    @Override
    public void set(int i, int j, boolean value) {
        matrix[i][j] = value;
    }

    @NonNull
    @Override
    public Mask copy() {
        return new BooleanMask(rows, cols, deepCopy(matrix));
    }

    @Override
    public boolean isAllTrue() {
        for (boolean[] array : matrix) {
            for (int i = 0; i < cols; i++) {
                if (!array[i]) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public boolean isAllFalse() {
        for (boolean[] array : matrix) {
            for (int i = 0; i < cols; i++) {
                if (array[i]) {
                    return false;
                }
            }
        }

        return true;
    }

    @NonNull
    @Override
    public Mask and(@NonNull Mask another) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] &= another.get(i, j);
            }
        }

        return this;
    }

    @NonNull
    @Override
    public Mask or(@NonNull Mask another) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] |= another.get(i, j);
            }
        }

        return this;
    }

    @NonNull
    @Override
    public Mask xor(@NonNull Mask another) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] ^= another.get(i, j);
            }
        }

        return this;
    }

    @NonNull
    @Override
    public Mask invert() {
        for (boolean[] array : matrix) {
            for (int i = 0; i < array.length; i++) {
                array[i] = !array[i];
            }
        }

        return this;
    }

    @Override
    public boolean notIntersects(@NonNull Mask another) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (matrix[i][j] && another.get(i, j)) {
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
        BooleanMask that = (BooleanMask) o;
        return rows == that.rows && cols == that.cols && Arrays.deepEquals(matrix, that.matrix);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(matrix);
    }
}
