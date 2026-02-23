package com.epicdima.findwords.mask;

import androidx.annotation.NonNull;

public final class LongArrayMaskTest extends MaskTest {

    @NonNull
    @Override
    protected Mask createMask() {
        return MaskType.LONG_ARRAY.createInstance(rows, cols);
    }
}