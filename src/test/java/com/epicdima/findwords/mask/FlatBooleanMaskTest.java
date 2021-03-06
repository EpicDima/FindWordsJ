package com.epicdima.findwords.mask;

import com.epicdima.findwords.base.Mask;
import com.epicdima.findwords.base.MaskTest;

public class FlatBooleanMaskTest extends MaskTest {

    @Override
    protected Mask createMask() {
        return new FlatBooleanMask(rows, cols);
    }
}
