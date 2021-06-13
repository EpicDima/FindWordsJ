package com.epicdima.findwords.base;

public interface Mask {
    boolean get(int i, int j);

    void set(int i, int j, boolean value);

    Mask copy();

    boolean isAllTrue();

    boolean isAllFalse();

    Mask and(Mask another);

    Mask or(Mask another);

    Mask invert();
}
