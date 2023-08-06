package com.epicdima.findwords.mask;

public class BitSetMaskTest extends MaskTest {

    @Override
    protected Mask createMask() {
        return MaskType.BITSET.createInstance(rows, cols);
    }
}
