package ru.otus.config2;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.services.*;

@AppComponentsContainerConfig(order = 1)
public class AppConfig2 {

    @AppComponent(order = 2, name = "equationPreparer")
    public EquationPreparer equationPreparer(){
        return new EquationPreparerImpl();
    }

       @AppComponent(order = 2, name = "ioService")
    public IOService ioService() {
        return new IOServiceStreams(System.out, System.in);
    }

}
