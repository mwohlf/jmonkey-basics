package net.wohlfart.events;

public class LifecycleCreate<T> {
    private final T element;

    public LifecycleCreate(T element) {
        this.element = element;
    }

    public T get() {
        return element;
    }
}
