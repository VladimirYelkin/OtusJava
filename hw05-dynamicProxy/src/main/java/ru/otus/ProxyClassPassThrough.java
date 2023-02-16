package ru.otus;

import ru.otus.annotations.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProxyClassPassThrough {
    @SuppressWarnings("unchecked")
    public static <T> T proxingMyClass(T myClass, Class iface) {
        InvocationHandler handler = new ObjectInvocationHandler(myClass, iface);
        return (T) Proxy.newProxyInstance(
                iface.getClassLoader(),
                new Class<?>[]{iface},
                handler);
    }

    static class ObjectInvocationHandler implements InvocationHandler {

        private final Object myClass;

        private final List<Method> loggingMethods;

        public ObjectInvocationHandler(Object myClass, Class iface) {
            this.myClass = myClass;
            List<Method> loggingMethodsOfClassByAnnotation = Arrays.stream(myClass.getClass().getDeclaredMethods())
                    .filter(m -> m.isAnnotationPresent(Log.class))
                    .collect(Collectors.toList());
            List<Method> methodsOfLoggingInterface = Arrays.stream(iface.getDeclaredMethods()).collect(Collectors.toList());

            loggingMethods = getLoggingMethodsOfInterfaceByAnnotationInClass(methodsOfLoggingInterface, loggingMethodsOfClassByAnnotation);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (loggingMethods.contains(method)) {
                System.out.print("executed method:" + method.getName()+" "
                        +Arrays.stream(args).map(Object::toString).collect(Collectors.joining(",", "[", "]"))+" : ");
            }
            return method.invoke(myClass, args);
        }

        private List<Method> getLoggingMethodsOfInterfaceByAnnotationInClass(List<Method> listMethodsOfInterface, List<Method> listMethodsOfClass) {
            return listMethodsOfInterface.stream()
                    .filter(methodByInterface -> isMethodOfInterfaceInClassMethodsWithAnnotation(methodByInterface, listMethodsOfClass))
                    .collect(Collectors.toList());
        }

        private boolean isMethodOfInterfaceInClassMethodsWithAnnotation(Method methodByInterface, List<Method> listMethodsOfClass) {
            return listMethodsOfClass.stream()
                    .anyMatch(methodByClass ->
                            methodByInterface.getName().equals(methodByClass.getName())
                                    && Arrays.deepEquals(methodByInterface.getParameterTypes(), methodByClass.getParameterTypes()));
        }
    }
}

