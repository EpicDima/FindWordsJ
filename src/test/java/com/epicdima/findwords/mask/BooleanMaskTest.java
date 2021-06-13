package com.epicdima.findwords.mask;

import com.epicdima.findwords.base.Mask;
import com.epicdima.findwords.base.MaskTest;

public class BooleanMaskTest extends MaskTest {

    @Override
    protected Mask createMask() {
        return new BooleanMask(MaskTest.ROWS, MaskTest.COLS);
    }
}
