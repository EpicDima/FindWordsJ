package com.epicdima.findwords.mask;

public class FlatBooleanMaskTest extends MaskTest {

    @Override
    protected Mask createMask() {
        return new FlatBooleanMask(rows, cols);
    }
}
