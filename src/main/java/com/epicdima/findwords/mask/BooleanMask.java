package com.epicdima.findwords.mask;

import java.util.Arrays;

public class BooleanMask implements Mask {
    private final int rows;
    private final int cols;

    private final boolean[][] matrix;

    private BooleanMask(int rows, int cols, boolean[][] matrix) {
        this.rows = rows;
        this.cols = cols;
        this.matrix = matrix;
    }

    @SuppressWarnings("unused") // used through MethodHandle
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
    public boolean get(int i, int j) {
        return matrix[i][j];
    }

    @Override
    public void set(int i, int j, boolean value) {
        matrix[i][j] = value;
    }

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

    @Override
    public Mask and(Mask another) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] &= another.get(i, j);
            }
        }

        return this;
    }

    @Override
    public Mask or(Mask another) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] |= another.get(i, j);
            }
        }

        return this;
    }

    @Override
    public Mask xor(Mask another) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] ^= another.get(i, j);
            }
        }

        return this;
    }

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
    public boolean notIntersects(Mask another) {
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
