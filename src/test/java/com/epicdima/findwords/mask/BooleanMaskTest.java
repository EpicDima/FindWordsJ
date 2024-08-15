package com.epicdima.findwords.mask;

import androidx.annotation.NonNull;

public final class BooleanMaskTest extends MaskTest {

    @NonNull
    @Override
    protected Mask createMask() {
        return MaskType.BOOLEAN.createInstance(rows, cols);
    }
}
