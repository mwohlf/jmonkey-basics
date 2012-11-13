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
import com.jme3.scene.Spatial;

/**
 * this game state is where we show planets and starships and stuff
 */
public class StellarState extends AbstractAppState {

    protected InputProcessor inputProcessor;
    protected Spatial worldSpatial;
    protected Spatial guiSpatial;
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
        StellarSystem stellarSystem = new StellarSystem(context);
        context.getViewPort().attachScene(worldSpatial = stellarSystem.getNode());

        StellarScreenView stellarScreenView = new StellarScreenView(context);
        presenter = new StellarScreenPresenter(context);
        presenter.connectView(stellarScreenView);
        context.getGuiViewPort().attachScene(guiSpatial = stellarScreenView.getNode());

        avatar = new AvatarImpl(stellarSystem, context.getCamera());
        TypeReference<LifecycleCreate<IAvatar>> type = new TypeReference<LifecycleCreate<IAvatar>>() {};
        LifecycleCreate<IAvatar> event = new LifecycleCreate<IAvatar>(avatar);
        eventBus.publish(type.getType(), event);
        inputProcessor = new InputProcessor(context);
        inputProcessor.attachAvatar(avatar);
        inputProcessor.attachView(stellarScreenView);
    }

    // update state loop
    @Override
    public void update(final float timePerFrame) {
        super.update(timePerFrame); // makes sure to execute AppTasks

        worldSpatial.updateLogicalState(timePerFrame);
        guiSpatial.updateLogicalState(timePerFrame);

        worldSpatial.updateGeometricState();
        guiSpatial.updateGeometricState();
    }

    // destroy
    @Override
    public void cleanup() {
        super.cleanup();

        presenter.shutdown();
        inputProcessor.detachCurrentAvatar();
        inputProcessor.detachCurrentView();

        TypeReference<LifecycleDestroy<IAvatar>> type = new TypeReference<LifecycleDestroy<IAvatar>>() {};
        LifecycleDestroy<IAvatar> event = new LifecycleDestroy<IAvatar>(avatar);
        eventBus.publish(type.getType(), event);

        context.getViewPort().detachScene(worldSpatial);
        context.getGuiViewPort().detachScene(guiSpatial);
    }

}
