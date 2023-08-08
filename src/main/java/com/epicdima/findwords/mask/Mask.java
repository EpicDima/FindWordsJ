package com.epicdima.findwords.mask;

public interface Mask {

    boolean get(int index);

    boolean get(int i, int j);

    void set(int i, int j, boolean value);

    Mask copy();

    boolean isAllTrue();

    boolean isAllFalse();

    Mask and(Mask another);

    Mask or(Mask another);

    Mask xor(Mask another);

    Mask invert();

    boolean notIntersects(Mask another);
}
