package com.epicdima.findwords.utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

// для подсчёта вызовов и оптимизации наиболее частых
public final class InvokeCountInvokationHandler implements InvocationHandler {

    public static final Map<String, Long> METHODS_INVOCATIONS = new HashMap<>();
    private final Object object;
    private final Class<?> clazz;
    private final String canonicalName;
    private InvokeCountInvokationHandler(final Object object) {
        this.object = object;
        this.clazz = object.getClass();
        this.canonicalName = clazz.getCanonicalName();
    }

    @SuppressWarnings("unchecked")
    public static <T> T createProxy(T object) {
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

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        updateOnInvoke(canonicalName + "__" + method.getName());

        Object returned = method.invoke(object, args);
        if (returned != null && (returned == object || clazz.equals(returned.getClass()))) {
            returned = createProxy(returned);
        }

        return returned;
    }

    private void updateOnInvoke(String fullname) {
        if (METHODS_INVOCATIONS.containsKey(fullname)) {
            METHODS_INVOCATIONS.put(fullname, METHODS_INVOCATIONS.get(fullname) + 1L);
        } else {
            METHODS_INVOCATIONS.put(fullname, 1L);
        }
    }
}
