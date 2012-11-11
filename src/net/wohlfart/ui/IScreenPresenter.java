package net.wohlfart.ui;

public interface IScreenPresenter<V extends AbstractScreenView<P, V>, P extends IScreenPresenter<V, P>> {


    public abstract String getId();

    public abstract V getView();

    public abstract void startup();

    public abstract void shutdown();

}
