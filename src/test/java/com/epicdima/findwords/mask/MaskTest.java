package com.epicdima.findwords.mask;

import androidx.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public abstract class MaskTest {
    protected final int rows = 16;
    protected final int cols = 16;

    @NonNull
    protected abstract Mask createMask();

    @Test
    public final void setAndGet1() {
        Mask mask = createMask();

        mask.set(0, 0, true);
        mask.set(0, 1, true);
        mask.set(1, 0, true);
        mask.set(1, 1, true);
        mask.set(rows - 1, cols - 1, true);

        Assertions.assertTrue(mask.get(0));
        Assertions.assertTrue(mask.get(1));
        Assertions.assertTrue(mask.get(cols));
        Assertions.assertTrue(mask.get(cols + 1));
        Assertions.assertTrue(mask.get(rows * cols - 1));

        mask.set(0, 0, false);
        mask.set(0, 1, false);
        mask.set(1, 0, false);
        mask.set(1, 1, false);
        mask.set(rows - 1, cols - 1, false);

        Assertions.assertFalse(mask.get(0));
        Assertions.assertFalse(mask.get(1));
        Assertions.assertFalse(mask.get(cols));
        Assertions.assertFalse(mask.get(cols + 1));
        Assertions.assertFalse(mask.get(rows * cols - 1));
    }

    @Test
    public final void setAndGet2() {
        Mask mask = createMask();

        mask.set(0, 0, true);
        mask.set(0, 1, true);
        mask.set(1, 0, true);
        mask.set(1, 1, true);
        mask.set(rows - 1, cols - 1, true);

        Assertions.assertTrue(mask.get(0, 0));
        Assertions.assertTrue(mask.get(0, 1));
        Assertions.assertTrue(mask.get(1, 0));
        Assertions.assertTrue(mask.get(1, 1));
        Assertions.assertTrue(mask.get(rows - 1, cols - 1));

        mask.set(0, 0, false);
        mask.set(0, 1, false);
        mask.set(1, 0, false);
        mask.set(1, 1, false);
        mask.set(rows - 1, cols - 1, false);

        Assertions.assertFalse(mask.get(0, 0));
        Assertions.assertFalse(mask.get(0, 1));
        Assertions.assertFalse(mask.get(1, 0));
        Assertions.assertFalse(mask.get(1, 1));
        Assertions.assertFalse(mask.get(rows - 1, cols - 1));
    }

    @Test
    public final void copy() {
        Mask source = createMask();

        source.set(0, 0, true);
        source.set(0, 1, true);
        source.set(1, 0, true);
        source.set(1, 1, true);
        source.set(rows - 1, cols - 1, true);

        Mask mask = source.copy();

        Assertions.assertTrue(mask.get(0, 0));
        Assertions.assertTrue(mask.get(0, 1));
        Assertions.assertTrue(mask.get(1, 0));
        Assertions.assertTrue(mask.get(1, 1));
        Assertions.assertTrue(mask.get(rows - 1, cols - 1));
    }

    @Test
    public final void isAllTrue() {
        Mask mask = createMask();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Assertions.assertFalse(mask.isAllTrue());
                mask.set(i, j, true);
            }
        }

        Assertions.assertTrue(mask.isAllTrue());
    }

    @Test
    public final void isAllFalse() {
        Mask mask = createMask();

        Assertions.assertTrue(mask.isAllFalse());

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                mask.set(i, j, true);
                Assertions.assertFalse(mask.isAllFalse());
            }
        }
    }

    @Test
    public final void and() {
        Mask mask1 = createMask();
        Mask mask2 = createMask();

        mask1.and(mask2);
        Assertions.assertTrue(mask1.isAllFalse());
        Assertions.assertTrue(mask2.isAllFalse());

        mask1.set(0, 0, true);
        mask1.and(mask2);
        Assertions.assertTrue(mask1.isAllFalse());
        Assertions.assertTrue(mask2.isAllFalse());

        mask1.set(0, 0, true);
        mask2.set(0, 0, true);
        mask1.and(mask2);
        Assertions.assertFalse(mask1.isAllFalse());
        Assertions.assertFalse(mask2.isAllFalse());
        Assertions.assertTrue(mask1.get(0, 0));
        Assertions.assertTrue(mask2.get(0, 0));

        mask1.set(0, 0, false);
        mask1.and(mask2);
        Assertions.assertTrue(mask1.isAllFalse());
        Assertions.assertFalse(mask2.isAllFalse());
        Assertions.assertFalse(mask1.get(0, 0));
        Assertions.assertTrue(mask2.get(0, 0));
    }

    @Test
    public final void or() {
        Mask mask1 = createMask();
        Mask mask2 = createMask();

        mask1.or(mask2);
        Assertions.assertTrue(mask1.isAllFalse());
        Assertions.assertTrue(mask2.isAllFalse());

        mask1.set(0, 0, true);
        mask1.or(mask2);
        Assertions.assertFalse(mask1.isAllFalse());
        Assertions.assertTrue(mask2.isAllFalse());
        Assertions.assertTrue(mask1.get(0, 0));

        mask2.set(0, 0, true);
        mask1.or(mask2);
        Assertions.assertFalse(mask1.isAllFalse());
        Assertions.assertFalse(mask2.isAllFalse());
        Assertions.assertTrue(mask1.get(0, 0));
        Assertions.assertTrue(mask2.get(0, 0));

        mask2.set(0, 0, false);
        mask1.or(mask2);
        Assertions.assertFalse(mask1.isAllFalse());
        Assertions.assertTrue(mask2.isAllFalse());
        Assertions.assertTrue(mask1.get(0, 0));
        Assertions.assertFalse(mask2.get(0, 0));
    }

    @Test
    public final void xor() {
        Mask mask1 = createMask();
        Mask mask2 = createMask();

        mask1.xor(mask2);
        Assertions.assertTrue(mask1.isAllFalse());
        Assertions.assertTrue(mask2.isAllFalse());

        mask1.set(0, 0, true);
        mask1.xor(mask2);
        Assertions.assertFalse(mask1.isAllFalse());
        Assertions.assertTrue(mask2.isAllFalse());
        Assertions.assertTrue(mask1.get(0, 0));

        mask2.set(0, 0, true);
        mask1.xor(mask2);
        Assertions.assertTrue(mask1.isAllFalse());
        Assertions.assertFalse(mask2.isAllFalse());
        Assertions.assertFalse(mask1.get(0, 0));
        Assertions.assertTrue(mask2.get(0, 0));
    }

    @Test
    public final void invert() {
        Mask mask = createMask();

        Assertions.assertTrue(mask.isAllFalse());

        mask.invert();
        Assertions.assertTrue(mask.isAllTrue());

        mask.invert();
        Assertions.assertTrue(mask.isAllFalse());

        mask.set(0, 0, true);
        mask.invert();
        Assertions.assertFalse(mask.isAllFalse());
        Assertions.assertFalse(mask.isAllTrue());
        Assertions.assertFalse(mask.get(0, 0));
    }

    @Test
    public final void notIntersects() {
        Mask mask1 = createMask();
        Mask mask2 = createMask();

        Assertions.assertTrue(mask1.notIntersects(mask2));

        mask1.set(0, 0, true);
        Assertions.assertTrue(mask1.notIntersects(mask2));

        mask1.set(0, 0, false);
        mask2.set(0, 0, true);
        Assertions.assertTrue(mask1.notIntersects(mask2));

        mask1.set(0, 0, true);
        Assertions.assertFalse(mask1.notIntersects(mask2));

        mask1.set(0, 0, false);
        mask1.set(1, 1, true);
        Assertions.assertTrue(mask1.notIntersects(mask2));

        mask2.set(1, 1, true);
        Assertions.assertFalse(mask1.notIntersects(mask2));
    }

    @Test
    public final void testEqualsAndHashCode() {
        Mask mask1 = createMask();
        Mask mask2 = createMask();

        Assertions.assertEquals(mask1, mask2);
        Assertions.assertEquals(mask1, mask1.copy());
        Assertions.assertEquals(mask2, mask2.copy());

        mask1.set(0, 0, true);
        mask2.set(rows - 1, cols - 1, true);
        Assertions.assertEquals(mask1, mask1.copy());
        Assertions.assertEquals(mask2, mask2.copy());

        Assertions.assertNotEquals(mask1, mask2);
    }
}