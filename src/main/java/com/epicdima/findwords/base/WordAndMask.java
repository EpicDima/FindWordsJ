package com.epicdima.findwords.base;

import java.util.Objects;

public final class WordAndMask {
    public final String word;
    public final Mask mask;

    public WordAndMask(String word, Mask mask) {
        this.word = word;
        this.mask = mask;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WordAndMask that = (WordAndMask) o;
        return word.equals(that.word) && mask.equals(that.mask);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word, mask);
    }
}
