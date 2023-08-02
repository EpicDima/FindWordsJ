package com.epicdima.findwords.mask;

public class BitSetMaskTest extends MaskTest {

    @Override
    protected Mask createMask() {
        return new BitSetMask(rows, cols);
    }
}
