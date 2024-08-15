package com.epicdima.findwords.mask;

import androidx.annotation.NonNull;

public interface Mask {

    boolean get(int index);

    boolean get(int i, int j);

    void set(int i, int j, boolean value);

    @NonNull
    Mask copy();

    boolean isAllTrue();

    boolean isAllFalse();

    @NonNull
    Mask and(@NonNull Mask another);

    @NonNull
    Mask or(@NonNull Mask another);

    @NonNull
    Mask xor(@NonNull Mask another);

    @NonNull
    Mask invert();

    boolean notIntersects(@NonNull Mask another);

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();
}
