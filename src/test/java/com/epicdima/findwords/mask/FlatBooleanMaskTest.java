package com.epicdima.findwords.mask;

import androidx.annotation.NonNull;

public final class FlatBooleanMaskTest extends MaskTest {

    @NonNull
    @Override
    protected Mask createMask() {
        return MaskType.FLAT.createInstance(rows, cols);
    }
}
