package net.wohlfart.events;

public class DoSelection<T> {
    private final T element;

    public DoSelection(final T element) {
        this.element = element;
    }

    public T get() {
        return element;
    }
}
