package net.wohlfart.events;

public class DoSelection<T> {
    private final T element;

    public DoSelection(T element) {
        this.element = element;
    }

    public T get() {
        return element;
    }
}
