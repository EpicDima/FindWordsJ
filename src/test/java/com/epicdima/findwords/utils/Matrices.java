package com.epicdima.findwords.utils;

import androidx.annotation.NonNull;

public final class Matrices {
    @NonNull
    public static final String MATRIX_8_X_8 = """
            лгдосежб
            адимылуа
            отаогдрр
            рыртпода
            йшгоаьлс
            иривксоу
            баорпутг
            регакшак""";

    @NonNull
    public static final String MATRIX_19_X_4 = """
            ласт
            ласа
            ларь
            лава
            копа
            керн
            кант
            иена
            дюна
            дина
            вена
            блин
            банк
            аура
            арат
            анод
            анид
            алла
            авар""";

    @NonNull
    public static final String MATRIX_10_X_10 = """
            креплкшкпо
            дисдеамауд
            авлтунифшк
            нсобмка  а
            хтуочогинк
            оллакмрасд
            кодидоантв
            иньлелкиое
            огратьовлр
            боевваннаь""";

    private Matrices() {
        throw new AssertionError("Private constructor");
    }
}
