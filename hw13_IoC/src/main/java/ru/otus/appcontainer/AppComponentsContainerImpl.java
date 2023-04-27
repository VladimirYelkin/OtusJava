package ru.otus.appcontainer;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import static org.reflections.scanners.Scanners.SubTypes;
import static org.reflections.scanners.Scanners.TypesAnnotated;

public class AppComponentsContainerImpl implements AppComponentsContainer {
    private static final Logger log = LoggerFactory.getLogger(AppComponentsContainerImpl.class);

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();


    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        this(Set.of(initialConfigClass));
    }

    public AppComponentsContainerImpl(Set<Class<?>> initialConfigClasses) {
        initialConfigClasses.stream()
                .sorted(Comparator.comparingInt(classConfig -> classConfig.getAnnotation(AppComponentsContainerConfig.class).order()))
                .forEach(configClass -> {
                    log.debug("order config class={}", configClass.getAnnotation(AppComponentsContainerConfig.class).order());
                    processConfig(configClass);
                });
    }

    public AppComponentsContainerImpl(Class<?>... additiveConfigClasses) {
        this(Arrays.stream(additiveConfigClasses).collect(Collectors.toSet()));
    }

    public AppComponentsContainerImpl(String pathToConfig) {
        this(new Reflections(pathToConfig)
                .get(SubTypes.of(TypesAnnotated
                        .with(AppComponentsContainerConfig.class)).asClass()));
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        var components = appComponents.stream()
                .filter(appComponent -> componentClass.isAssignableFrom(appComponent.getClass()))
                .peek(appComponent -> log.debug("find: {} {} ", componentClass, appComponent))
                .toList();

        return switch (components.size()) {
            case 1 -> (C) components.get(0);
            case 0 -> throw new RuntimeException("Not Found component " + componentClass);
            default -> throw new RuntimeException("More than one found" + componentClass);
        };
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        if (appComponentsByName.containsKey(componentName)) {
            return (C) appComponentsByName.get(componentName);
        } else {
            throw new RuntimeException("Not Found Component " + componentName);
        }
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        // You code here...
        Object configBean = instantiate(configClass);

        Arrays.stream(configClass.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparingInt(method -> method.getAnnotation(AppComponent.class).order()))
                .forEach(method -> {
                    log.debug("configClass= {} method={}", configClass, method);
                    var paramArg = Arrays.stream(method.getParameters())
                            .peek(param -> log.debug("param: {}", param.getType()))
                            .map(param -> getAppComponent(param.getType()))
                            .toList();
                    if (appComponentsByName.containsKey(method.getAnnotation(AppComponent.class).name())) {
                        throw new RuntimeException("Component name already exist");
                    } else {
                        var appComponent = callMethod(configBean, method, paramArg.toArray());
                        appComponents.add(appComponent);
                        appComponentsByName.put(method.getAnnotation(AppComponent.class).name(), appComponent);
                    }
                });
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
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
