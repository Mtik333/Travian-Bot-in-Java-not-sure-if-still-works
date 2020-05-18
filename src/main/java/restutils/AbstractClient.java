package restutils;

import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import java.util.function.Supplier;

public abstract class AbstractClient<T> {

    protected T service;

    public AbstractClient(ResteasyWebTarget stub, Class<T> serviceClass) {
        service = stub.proxy(serviceClass);
    }

    protected <R> R execute(Supplier<R> func) {
        return func.get();
    }
}
