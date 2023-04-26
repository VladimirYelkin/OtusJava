package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        // You code here...
        Object configBean = instantiate(configClass);
        var declaredMethods = Arrays.stream(configClass.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparingInt(method -> method.getAnnotation(AppComponent.class).order()))
                .toList();

        for (var method : declaredMethods) {
            if (method.getParameters().length == 0) {
                var appComponent = callMethod(configBean, method);
                appComponents.add(appComponent);
                appComponentsByName.put(method.getAnnotation(AppComponent.class).name(), appComponent);
            } else {
                List<Object> paramArg = new ArrayList<>();
                for (Parameter param : method.getParameters()) {
                    System.out.println("param:" + param.getType());
                    paramArg.add(getAppComponent(param.getType()));
                }
                var appComponent = callMethod(configBean, method, paramArg.toArray());
                appComponents.add(appComponent);
                appComponentsByName.put(method.getAnnotation(AppComponent.class).name(), appComponent);
            }
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        for (var appComponent : appComponents) {
            if (componentClass.isAssignableFrom(appComponent.getClass())) {
                System.out.println("find: " + componentClass + " " + appComponent);
                return (C) appComponent;
            }
        }
        return null;
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return (C) appComponentsByName.get(componentName);
    }


    private static Object callMethod(Object object, Method method, Object... args) {
        try {
            method.setAccessible(true);
            return method.invoke(object, args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> T instantiate(Class<T> type) {
        try {
            return type.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Class<?>[] toClasses(Object[] args) {
        return Arrays.stream(args).map(Object::getClass).toArray(Class<?>[]::new);
    }
}
