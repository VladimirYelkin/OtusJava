package ru.otus;

import ru.otus.annotations.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class ProxyClassPassThrough {
    @SuppressWarnings("unchecked")
    public static <T> T proxingMyClass(T myClass, Class iface) {
        InvocationHandler handler = new ObjectInvocationHandler(myClass, iface);
        return (T) Proxy.newProxyInstance(iface.getClassLoader(), new Class<?>[]{iface}, handler);
    }

    static class ObjectInvocationHandler implements InvocationHandler {

        private final Object myClass;

        private final Set<Method> loggingMethods;

        public ObjectInvocationHandler(Object myClass, Class iface) {
            this.myClass = myClass;

            loggingMethods = Arrays.stream(iface.getDeclaredMethods())
                    .filter(method ->
                            Arrays.stream(myClass.getClass().getDeclaredMethods()).filter(m -> m.isAnnotationPresent(Log.class))
                                    .anyMatch(method1 -> method1.getName().equals(method.getName())
                                            && Arrays.deepEquals(method1.getParameterTypes(), method.getParameterTypes())))
                    .collect(Collectors.toSet());
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (loggingMethods.contains(method)) {
                System.out.print("executed method:" + method.getName() + " " + Arrays.stream(args).map(Object::toString).collect(Collectors.joining(",", "[", "]")) + " : ");
            }
            return method.invoke(myClass, args);
        }
    }
}

