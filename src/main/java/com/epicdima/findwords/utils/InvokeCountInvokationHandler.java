package com.epicdima.findwords.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused") // for counting calls and optimizing the most frequent ones
public final class InvokeCountInvokationHandler implements InvocationHandler {

    @NonNull
    public static final Map<String, Long> METHODS_INVOCATIONS = new HashMap<>();

    @NonNull
    private final Object object;

    @NonNull
    private final Class<?> clazz;

    @NonNull
    private final String canonicalName;

    private InvokeCountInvokationHandler(final Object object) {
        this.object = object;
        this.clazz = object.getClass();
        this.canonicalName = clazz.getCanonicalName();
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> T createProxy(@NonNull T object) {
        ClassLoader classLoader = object.getClass().getClassLoader();
        Class<?>[] interfaces = object.getClass().getInterfaces();
        return (T) Proxy.newProxyInstance(classLoader, interfaces, new InvokeCountInvokationHandler(object));
    }

    public static void printMethodsInvocations() {
        METHODS_INVOCATIONS
                .entrySet()
                .stream()
                .sorted((o1, o2) -> (int) (o2.getValue() - o1.getValue()))
                .map(entry -> String.format("%-80s  %15d", entry.getKey(), entry.getValue()))
                .forEachOrdered(System.out::println);
    }

    @Nullable
    @Override
    public Object invoke(@NonNull Object proxy, @NonNull Method method, @Nullable Object[] args) throws Throwable {
        updateOnInvoke(canonicalName + "__" + method.getName());

        Object returned = method.invoke(object, args);
        if (returned != null && (returned == object || clazz.equals(returned.getClass()))) {
            returned = createProxy(returned);
        }

        return returned;
    }

    private void updateOnInvoke(@NonNull String fullname) {
        if (METHODS_INVOCATIONS.containsKey(fullname)) {
            METHODS_INVOCATIONS.put(fullname, METHODS_INVOCATIONS.get(fullname) + 1L);
        } else {
            METHODS_INVOCATIONS.put(fullname, 1L);
        }
    }
}
