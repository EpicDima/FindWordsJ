package com.epicdima.findwords.utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// для подсчёта вызовов и оптимизации наиболее частых
public final class InvokeCountInvokationHandler implements InvocationHandler {

    @SuppressWarnings("unchecked")
    public static <T> T createProxy(T object) {
        ClassLoader classLoader = object.getClass().getClassLoader();
        Class<?>[] interfaces = object.getClass().getInterfaces();
        return (T) Proxy.newProxyInstance(classLoader, interfaces, new InvokeCountInvokationHandler(object));
    }

    public static final Map<String, Long> METHODS_INVOCATIONS = new ConcurrentHashMap<>();

    private final Object object;

    private InvokeCountInvokationHandler(final Object object) {
        this.object = object;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        updateOnInvoke(object.getClass().getCanonicalName() + "__" + method.getName());

        return method.invoke(object, args);
    }

    private void updateOnInvoke(String fullname) {
        if (METHODS_INVOCATIONS.containsKey(fullname)) {
            METHODS_INVOCATIONS.put(fullname, METHODS_INVOCATIONS.get(fullname) + 1L);
        } else {
            METHODS_INVOCATIONS.put(fullname, 1L);
        }
    }
}
