package net.wohlfart.events;

public class LifecycleDestroy<T> {
    private final T element;

    public LifecycleDestroy(T element) {
        this.element = element;
    }

    public T get() {
        return element;
    }
}
