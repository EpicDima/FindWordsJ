package com.epicdima.findwords.mask;

public class BooleanMaskTest extends MaskTest {

    @Override
    protected Mask createMask() {
        return MaskType.BOOLEAN.createInstance(rows, cols);
    }
}
