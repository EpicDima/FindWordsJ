package com.epicdima.findwords.mask;

import androidx.annotation.NonNull;
import java.util.Arrays;

public final class LongArrayMask implements Mask {
    private static final int ADDRESS_BITS_PER_WORD = 6;
    private static final int BITS_PER_WORD = 1 << ADDRESS_BITS_PER_WORD; // 64

    private final int rows;
    private final int cols;
    private final int size;
    
    @NonNull
    private final long[] words;

    private LongArrayMask(int rows, int cols, int size, @NonNull long[] words) {
        this.rows = rows;
        this.cols = cols;
        this.size = size;
        this.words = words;
    }

    @SuppressWarnings("unused") // used via MethodHandle
    public LongArrayMask(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.size = rows * cols;
        int wordCount = (size + BITS_PER_WORD - 1) >>> ADDRESS_BITS_PER_WORD;
        this.words = new long[wordCount];
    }

    @Override
    public boolean get(int index) {
        int wordIndex = index >>> ADDRESS_BITS_PER_WORD;
        return (words[wordIndex] & (1L << index)) != 0;
    }

    @Override
    public boolean get(int i, int j) {
        return get(i * cols + j);
    }

    @Override
    public void set(int i, int j, boolean value) {
        int index = i * cols + j;
        int wordIndex = index >>> ADDRESS_BITS_PER_WORD;
        if (value) {
            words[wordIndex] |= (1L << index);
        } else {
            words[wordIndex] &= ~(1L << index);
        }
    }

    @NonNull
    @Override
    public Mask copy() {
        return new LongArrayMask(rows, cols, size, Arrays.copyOf(words, words.length));
    }

    @Override
    public boolean isAllTrue() {
        int bitCount = 0;
        for (long word : words) {
            bitCount += Long.bitCount(word);
        }
        return bitCount == size;
    }

    @Override
    public boolean isAllFalse() {
        for (long word : words) {
            if (word != 0) return false;
        }
        return true;
    }

    @NonNull
    @Override
    public Mask and(@NonNull Mask another) {
        long[] anotherWords = ((LongArrayMask) another).words;
        for (int i = 0; i < words.length; i++) {
            words[i] &= anotherWords[i];
        }
        return this;
    }

    @NonNull
    @Override
    public Mask or(@NonNull Mask another) {
        long[] anotherWords = ((LongArrayMask) another).words;
        for (int i = 0; i < words.length; i++) {
            words[i] |= anotherWords[i];
        }
        return this;
    }

    @NonNull
    @Override
    public Mask xor(@NonNull Mask another) {
        long[] anotherWords = ((LongArrayMask) another).words;
        for (int i = 0; i < words.length; i++) {
            words[i] ^= anotherWords[i];
        }
        return this;
    }

    @NonNull
    @Override
    public Mask invert() {
        for (int i = 0; i < words.length; i++) {
            words[i] = ~words[i];
        }
        // Очищаем хвост, чтобы size bitCount совпадал с реальностью и notIntersects работал корректно
        int tailBits = size % BITS_PER_WORD;
        if (tailBits != 0) {
            words[words.length - 1] &= (1L << tailBits) - 1;
        }
        return this;
    }

    @Override
    public boolean notIntersects(@NonNull Mask another) {
        long[] anotherWords = ((LongArrayMask) another).words;
        for (int i = 0; i < words.length; i++) {
            if ((words[i] & anotherWords[i]) != 0) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LongArrayMask that = (LongArrayMask) o;
        return rows == that.rows && cols == that.cols && size == that.size && Arrays.equals(words, that.words);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(words);
    }
}