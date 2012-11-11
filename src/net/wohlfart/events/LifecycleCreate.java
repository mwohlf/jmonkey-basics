package net.wohlfart.events;

public class LifecycleCreate<T> {
    private final T element;

    public LifecycleCreate(final T element) {
        this.element = element;
    }

    public T get() {
        return element;
    }
}
