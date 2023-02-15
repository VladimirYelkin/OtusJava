package ru.otus;

import ru.otus.annotations.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.stream.Collectors;

class ProxyMyClass {


    private ProxyMyClass() {
    }

    static TestLoggingInterface createMyClass() {
        InvocationHandler handler = new DemoInvocationHandler(new TestLogging());
        return (TestLoggingInterface) Proxy.newProxyInstance(ProxyMyClass.class.getClassLoader(),
                new Class<?>[]{TestLoggingInterface.class}, handler);
    }

    static class DemoInvocationHandler implements InvocationHandler {
        private final TestLoggingInterface myClass;
        private final List<Method> loggingMethods ;

        DemoInvocationHandler(TestLoggingInterface myClass) {
            this.myClass = myClass;

            List<Method> loggingMethodsOfClassByAnnotation = Arrays.stream(myClass.getClass().getDeclaredMethods())
                    .filter(m -> m.isAnnotationPresent(Log.class))
                    .collect(Collectors.toList());
            List<Method> methodsOfLoggingInterface = Arrays.stream(TestLoggingInterface.class.getDeclaredMethods()).collect(Collectors.toList());

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
