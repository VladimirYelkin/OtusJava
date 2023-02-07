package ru.otus.reflection;

import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StarterTests {
    public static void main(String[] args) {
        if (args.length > 0) {
            Arrays.stream(args).forEach(clName -> checkAllTestMethodsOfClass(clName, Test.class, Before.class, After.class));
        } else {
            checkAllTestMethodsOfClass("ru.otus.reflection.TestClassHomeWork03", Test.class, Before.class, After.class);
        }
    }

    private static void checkAllTestMethodsOfClass(String className,
                                                   Class<? extends Annotation> testAnnotation,
                                                   Class<? extends Annotation> beforeAnnotation,
                                                   Class<? extends Annotation> afterAnnotation) {
        Object object = checkedClassByName(className);
        int allTest = 0;
        int faultTest = 0;
        int notRunningTest = 0;
        int checkedTest = 0;
        for (Method testMethod : getMethods(object, testAnnotation)) {
            allTest++;
            switch (runTestMethod(object, testMethod, beforeAnnotation, afterAnnotation)) {
                case CHECKED -> checkedTest++;
                case FAULT -> faultTest++;
                case NOTRUNNED -> notRunningTest++;
            }
        }
        System.out.printf("Class %s. All test %d,  passed test %d, fault test %d, not runned %d\n", className, allTest, checkedTest, faultTest, notRunningTest);

    }

    private static Object checkedClassByName(String className) {
        try {
            return instantiate(Class.forName(className));
        } catch (ClassNotFoundException e) {
            System.out.printf("!!!Class %s not found\n", className);
            throw new RuntimeException(e);
        }
    }

    private static ResultTest runTestMethod(Object object,
                                            Method method,
                                            Class<? extends Annotation> beforeAnnotation,
                                            Class<? extends Annotation> afterAnnotation) {
        System.out.printf("\nMethod=%s test run\n", method.getName());
        Object testObject = instantiate(object.getClass());
        try {
            runTestsAnnotationsBeforeOrAfter(testObject, beforeAnnotation);
            try {
                callMethod(testObject, method.getName());
                return ResultTest.CHECKED;
            } catch (Exception e) {
                System.err.println("Method " + method.getName() + " fault");
                e.printStackTrace();
                return ResultTest.FAULT;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultTest.NOTRUNNED;
        } finally {
            try {
                runTestsAnnotationsBeforeOrAfter(testObject, afterAnnotation);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void runTestsAnnotationsBeforeOrAfter(Object object, Class<? extends Annotation> annotation) throws Exception {
        Exception cumulativeException = new RuntimeException();
        for (Method method : getMethods(object, annotation)) {
            try {
                callMethod(object, method.getName());
            } catch (Exception e) {
                cumulativeException.addSuppressed(e);
            }
        }
        if (cumulativeException.getSuppressed().length != 0) {
            throw cumulativeException;
        }
    }

    private static List<Method> getMethods(Object object, Class<? extends Annotation> annotation) {

        return Arrays.stream(object.getClass().getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(annotation))
                .collect(Collectors.toList());
    }

    private static Object callMethod(Object object, String name, Object... args) {
        try {
            var method = object.getClass().getDeclaredMethod(name, toClasses(args));
            method.setAccessible(true);
            return method.invoke(object, args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> T instantiate(Class<T> type, Object... args) {
        try {
            if (args.length == 0) {
                return type.getDeclaredConstructor().newInstance();
            } else {
                Class<?>[] classes = toClasses(args);
                return type.getDeclaredConstructor(classes).newInstance(args);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Class<?>[] toClasses(Object[] args) {
        return Arrays.stream(args).map(Object::getClass).toArray(Class<?>[]::new);
    }

}

enum ResultTest {
    CHECKED,
    FAULT,
    NOTRUNNED
}
