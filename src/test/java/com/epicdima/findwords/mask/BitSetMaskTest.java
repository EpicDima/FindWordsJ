package com.epicdima.findwords.mask;

import androidx.annotation.NonNull;

public final class BitSetMaskTest extends MaskTest {

    @NonNull
    @Override
    protected Mask createMask() {
        return MaskType.BITSET.createInstance(rows, cols);
    }
}
