package facundofederico.main.services;

import com.google.inject.Injector;
import picocli.CommandLine.IFactory;

public class GuiceFactory implements IFactory {
    private final Injector injector;

    public GuiceFactory(Injector injector) {
        this.injector = injector;
    }

    @Override
    public <K> K create(Class<K> cls) throws Exception {
        return injector.getInstance(cls);
    }
}
