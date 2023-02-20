package ru.otus;

import ru.otus.annotations.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.stream.Collectors;

class ProxyOnlyTestLoggingClass {


    private ProxyOnlyTestLoggingClass() {
    }

    static LoggingSummaryInterface createMyClass() {
        InvocationHandler handler = new DemoInvocationHandler(new TestLogging());
        return (LoggingSummaryInterface) Proxy.newProxyInstance(ProxyOnlyTestLoggingClass.class.getClassLoader(),
                new Class<?>[]{LoggingSummaryInterface.class}, handler);
    }

    static class DemoInvocationHandler implements InvocationHandler {
        private final LoggingSummaryInterface myClass;
        private final List<Method> loggingMethods ;

        DemoInvocationHandler(LoggingSummaryInterface myClass) {
            this.myClass = myClass;

            List<Method> loggingMethodsOfClassByAnnotation = Arrays.stream(myClass.getClass().getDeclaredMethods())
                    .filter(m -> m.isAnnotationPresent(Log.class))
                    .collect(Collectors.toList());
            List<Method> methodsOfLoggingInterface = Arrays.stream(LoggingSummaryInterface.class.getDeclaredMethods()).collect(Collectors.toList());

            loggingMethods = getLoggingMethodsOfInterface(methodsOfLoggingInterface, loggingMethodsOfClassByAnnotation);

        }


        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (loggingMethods.contains(method)) {
                System.out.print("executed method:" + method.getName());
                System.out.print(Arrays.stream(args).map(Object::toString).collect(Collectors.joining(",", "[", "]\n")));
            }
            return method.invoke(myClass, args);
        }

        @Override
        public String toString() {
            return "DemoInvocationHandler{" +
                    "myClass=" + myClass +
                    ", loggingMethods=" + loggingMethods.stream().map(Method::toString).collect(Collectors.joining(",","(",")")) +
                    '}';
        }

        private List<Method> getLoggingMethodsOfInterface(List<Method> listMethodsOfInterface, List<Method> listMethodsOfClass) {
            List<Method> result = new ArrayList<>();
            for (Method methodByInterface : listMethodsOfInterface) {
                for (Method methodByClass : listMethodsOfClass) {
                    if (methodByInterface.getName().equals(methodByClass.getName()) && Arrays.deepEquals(methodByInterface.getParameterTypes(), methodByClass.getParameterTypes())) {
                        result.add(methodByInterface);
                    }
                }
            }
            return result;
        }
    }
}
