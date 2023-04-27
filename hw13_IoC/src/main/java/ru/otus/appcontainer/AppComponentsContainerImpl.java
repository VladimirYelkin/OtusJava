package ru.otus.appcontainer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class AppComponentsContainerImpl implements AppComponentsContainer {
    private static final Logger log = LoggerFactory.getLogger(AppComponentsContainerImpl.class);

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    private List<InfoMethod> declaredInfoMethods = new ArrayList<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        this(initialConfigClass, new Class<?>[]{});
//        processConfig(initialConfigClass);
    }

    public AppComponentsContainerImpl(Class<?> initialConfigClass, Class<?>... additiveConfigClasses) {
        List<Class<?>> allInitialConfigClass = Arrays.stream(additiveConfigClasses).collect(Collectors.toList());
        allInitialConfigClass.add(initialConfigClass);
        allInitialConfigClass.stream()
                .sorted(Comparator.comparingInt(classConfig -> classConfig.getAnnotation(AppComponentsContainerConfig.class).order()));
        processConfig(initialConfigClass);
        for (var configClass : additiveConfigClasses) {
            processConfig(configClass);
            log.info("order config class={}", configClass.getAnnotation(AppComponentsContainerConfig.class).order());
        }
        createBeans();
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        // You code here...
        Object configBean = instantiate(configClass);

        this.declaredInfoMethods.addAll(getMethodsCreateComponent(configClass, configBean));

//        var declaredMethods = Arrays.stream(configClass.getDeclaredMethods())
//                .filter(m -> m.isAnnotationPresent(AppComponent.class))
//                .sorted(Comparator.comparingInt(method -> method.getAnnotation(AppComponent.class).order()))
//                .map(method -> {
//                    return new InfoMethod(method, method.getAnnotation(AppComponent.class).order(), configBean);
//                })
//                .collect(Collectors.toList());

//        declaredInfoMethods.forEach(methodInfo -> {
//            var method = methodInfo.getMethod();
//            List<Object> paramArg = new ArrayList<>();
//            Arrays.stream(method.getParameters()).forEachOrdered(param -> {
//                log.debug("param: {}", param.getType());
//                paramArg.add(getAppComponent(param.getType()));
//            });
//            var appComponent = callMethod(methodInfo.getBean(), method, paramArg.toArray());
//            appComponents.add(appComponent);
//            if (appComponentsByName.containsKey(method.getAnnotation(AppComponent.class).name())) {
//                throw new RuntimeException();
//            } else {
//                appComponentsByName.put(method.getAnnotation(AppComponent.class).name(), appComponent);
//            }
//        });
    }

    private void createBeans() {
        declaredInfoMethods.stream()
                .sorted(Comparator.comparingInt(methodInfo -> methodInfo.getMethod().getAnnotation(AppComponent.class).order()))
                .sorted(Comparator.comparingInt(methodInfo -> methodInfo.getMethod().getDeclaringClass().getAnnotation(AppComponentsContainerConfig.class).order()))
                .peek(infoMethod -> {log.info("method inf get class {}",infoMethod.getMethod().getDeclaringClass().getAnnotation(AppComponentsContainerConfig.class).order());})
                .forEach(methodInfo -> {
                    var method = methodInfo.getMethod();
                    List<Object> paramArg = new ArrayList<>();
                    Arrays.stream(method.getParameters()).forEachOrdered(param -> {
                        log.debug("param: {}", param.getType());
                        paramArg.add(getAppComponent(param.getType()));
                    });
                    var appComponent = callMethod(methodInfo.getBean(), method, paramArg.toArray());
                    appComponents.add(appComponent);
                    if (appComponentsByName.containsKey(method.getAnnotation(AppComponent.class).name())) {
                        throw new RuntimeException();
                    } else {
                        appComponentsByName.put(method.getAnnotation(AppComponent.class).name(), appComponent);
                    }
                });
    }

    private List<InfoMethod> getMethodsCreateComponent(Class<?> configClass,
                                                       Object beanConfiguration
    ) {
        return Arrays.stream(configClass.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparingInt(method -> method.getAnnotation(AppComponent.class).order()))
                .map(method -> {
                    return new InfoMethod(method, method.getAnnotation(AppComponent.class).order(), beanConfiguration);
                })
                .collect(Collectors.toList());

    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        var components = appComponents.stream()
                .filter(appComponent -> componentClass.isAssignableFrom(appComponent.getClass()))
                .peek(appComponent -> log.debug("find: {} {} ", componentClass, appComponent))
                .toList();
        if (components.size() == 1) {
            return (C) components.get(0);
        } else {
            throw new RuntimeException("");
        }

    }

    @Override
    public <C> C getAppComponent(String componentName) {
        if (appComponentsByName.containsKey(componentName)) {
            return (C) appComponentsByName.get(componentName);
        } else {
            throw new RuntimeException("Not Found Component");
        }
    }

    private class InfoMethod {
        private final Method method;
        private final int order;
        private final Object bean;

        public InfoMethod(Method method, int order, Object bean) {
            this.method = method;
            this.order = order;
            this.bean = bean;
        }

        public Method getMethod() {
            return method;
        }

        public int getOrder() {
            return order;
        }

        public Object getBean() {
            return bean;
        }
    }


    private Object callMethod(Object object, Method method, Object... args) {
        try {
            method.setAccessible(true);
            return method.invoke(object, args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <T> T instantiate(Class<T> type) {
        try {
            return type.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
