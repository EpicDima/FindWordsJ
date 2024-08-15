package com.epicdima.findwords.mask;

import androidx.annotation.NonNull;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public enum MaskType {
    BOOLEAN(BooleanMask.class),
    FLAT(FlatBooleanMask.class),
    BITSET(BitSetMask.class);

    @NonNull
    private final Class<? extends Mask> maskClass;

    @NonNull
    private final MethodHandle createInstanceMH;

    MaskType(@NonNull Class<? extends Mask> maskClass) {
        this.maskClass = maskClass;
        try {
            createInstanceMH = MethodHandles.publicLookup()
                    .findConstructor(maskClass, MethodType.methodType(void.class, int.class, int.class));
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @NonNull
    public final Mask createInstance(int rows, int cols) {
        try {
            return (Mask) createInstanceMH.invoke(rows, cols);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    @NonNull
    public final Class<? extends Mask> getMaskClass() {
        return maskClass;
    }
}
