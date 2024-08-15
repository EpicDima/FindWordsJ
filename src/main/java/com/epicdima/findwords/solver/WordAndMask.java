package com.epicdima.findwords.solver;

import androidx.annotation.NonNull;
import com.epicdima.findwords.mask.Mask;

public record WordAndMask(@NonNull String word, @NonNull Mask mask) {
}
