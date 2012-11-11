package net.wohlfart.states;

import net.wohlfart.IStateContext;
import net.wohlfart.events.LifecycleCreate;
import net.wohlfart.events.LifecycleDestroy;
import net.wohlfart.model.StellarSystem;
import net.wohlfart.ui.StellarScreenPresenter;
import net.wohlfart.ui.StellarScreenView;
import net.wohlfart.user.AvatarImpl;
import net.wohlfart.user.IAvatar;
import net.wohlfart.user.InputProcessor;

import org.bushe.swing.event.EventService;
import org.bushe.swing.event.generics.TypeReference;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

/**
 * this game state is where we show planets and starships and stuff
 */
public class StellarState extends AbstractAppState {

    protected InputProcessor inputProcessor;
    protected StellarSystem stellarSystem;
    protected StellarScreenView stellarScreenView;
    protected StellarScreenPresenter presenter;
    protected EventService eventBus;
    protected IAvatar avatar;

    protected IStateContext context; // the game class

    // setup
    @Override
    public void initialize(final AppStateManager stateManager, final Application application) {
        super.initialize(stateManager, application);
        context = (IStateContext) application;

        eventBus = context.getEventBus();

        // initial setup camera for this state
        final Camera cam = context.getCamera();
        cam.setLocation(Vector3f.ZERO);
        final Vector3f lookAt = new Vector3f(0, 0, -1);
        cam.lookAt(lookAt, Vector3f.UNIT_Y /* world up */);

        // attach stellar view
        stellarSystem = new StellarSystem(context);
        context.getViewPort().attachScene(stellarSystem.getNode());

        stellarScreenView = new StellarScreenView(context);
        presenter = new StellarScreenPresenter(context);
        presenter.connectView(stellarScreenView);
        context.getGuiViewPort().attachScene(stellarScreenView.getNode());

        avatar = new AvatarImpl(stellarSystem, context.getCamera());
        TypeReference<LifecycleCreate<IAvatar>> type = new TypeReference<LifecycleCreate<IAvatar>>() {};
        LifecycleCreate<IAvatar> event = new LifecycleCreate<IAvatar>(avatar);
        eventBus.publish(type.getType(), event);
        inputProcessor = new InputProcessor(context);
        inputProcessor.attachAvatar(avatar);
        inputProcessor.attachScreen(stellarScreenView);
    }

    // update state loop
    @Override
    public void update(final float timePerFrame) {
        super.update(timePerFrame); // makes sure to execute AppTasks

        stellarSystem.updateLogicalState(timePerFrame);
        presenter.updateLogicalState(timePerFrame);

        stellarSystem.updateGeometricState();
        presenter.updateGeometricState();
    }

    // destroy
    @Override
    public void cleanup() {
        super.cleanup();

        presenter.shutdown();
        inputProcessor.detachCurrentAvatar();
        inputProcessor.detachCurrentScreen();

        TypeReference<LifecycleDestroy<IAvatar>> type = new TypeReference<LifecycleDestroy<IAvatar>>() {};
        LifecycleDestroy<IAvatar> event = new LifecycleDestroy<IAvatar>(avatar);
        eventBus.publish(type.getType(), event);

        context.getViewPort().detachScene(stellarSystem.getNode());
        context.getGuiViewPort().detachScene(stellarScreenView.getNode());
    }

}
