package net.wohlfart.ui;

import net.wohlfart.IStateContext;

import org.bushe.swing.event.EventService;

public abstract class AbstractScreenPresenter<V extends AbstractScreenView<P, V>, P extends AbstractScreenPresenter<V, P>>
    implements IScreenPresenter<V,P> {

    protected final EventService eventBus;
    protected final V view;

    AbstractScreenPresenter(final V view, final IStateContext stateContext) {
        this.view = view;
        this.eventBus = stateContext.getEventBus();
    }

    @Override
    public String getId() {
        return view.getId();
    }

    @Override
    public V getView() {
        return view;
    }

    @Override
    public void startup() {
        view.setupPresenter(this);
    }

    @Override
    public void shutdown() {
        view.shutdown();
    }
}
