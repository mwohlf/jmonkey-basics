package net.wohlfart.ui;

import com.jme3.scene.Node;

/**
 * view knows only the presenter to access data and not the model
 * presenter sends DTOs to setup the view
 *
 * @param <P>
 */
public abstract class AbstractScreenView<P extends IScreenPresenter<V,P>, V extends AbstractScreenView<P, V>> {

    private final Node delegatee = new Node();

    private final String id;
    private IScreenPresenter<V,P> presenter;


    public AbstractScreenView(final String id) {
        this.id = id;
    }



    public String getId() {
        return id;
    }


    public Node getDelegatee() {
        return delegatee;
    }


    public void setupPresenter(IScreenPresenter<V,P> presenter) {
        this.presenter = presenter;
        //TODO: init the view and setup the connection to the presenter here
    }

    public void shutdown() {
        presenter = null;
    }


}
