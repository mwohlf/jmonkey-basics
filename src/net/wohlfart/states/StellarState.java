package net.wohlfart.states;

import net.wohlfart.IStateContext;
import net.wohlfart.events.LifecycleCreate;
import net.wohlfart.events.LifecycleDestroy;
import net.wohlfart.model.StellarSystem;
import net.wohlfart.model.ui.DisplayNode;
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

import de.lessvoid.nifty.Nifty;

/**
 * this game state is where we show planets and starships and stuff
 */
public class StellarState extends AbstractAppState {
    private static final String SCREEN_ID = "stellarScreen"; // used in nifty.xml to identify the hud stuff

    protected InputProcessor inputProcessor;
    protected StellarSystem stellarSystem;
    protected DisplayNode display;
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
        stellarSystem = new StellarSystem(context.getAssetManager(), eventBus);
        context.getViewPort().attachScene(stellarSystem.getNode());

        avatar = new AvatarImpl(stellarSystem, context.getCamera());
        TypeReference<LifecycleCreate<IAvatar>> type = new TypeReference<LifecycleCreate<IAvatar>>() {
        };
        LifecycleCreate<IAvatar> event = new LifecycleCreate<IAvatar>(avatar);
        eventBus.publish(type.getType(), event);
        inputProcessor = new InputProcessor(context.getInputManager());
        inputProcessor.attachAvatar(avatar);

        display = new DisplayNode(context.getAssetManager(), avatar, eventBus);
        context.getGuiViewPort().attachScene(display.getNode());

        // TODO: remove this and implement this in a screen controller:
        Nifty nifty = context.getNifty();
        nifty.gotoScreen(SCREEN_ID);
    }

    // update state loop
    @Override
    public void update(final float timePerFrame) {
        super.update(timePerFrame); // makes sure to execute AppTasks

        stellarSystem.updateLogicalState(timePerFrame);
        display.updateLogicalState(timePerFrame);

        stellarSystem.updateGeometricState();
        display.updateGeometricState();
    }

    // destroy
    @Override
    public void cleanup() {
        super.cleanup();

        inputProcessor.detachCurrentAvatar();

        TypeReference<LifecycleDestroy<IAvatar>> type = new TypeReference<LifecycleDestroy<IAvatar>>() {};
        LifecycleDestroy<IAvatar> event = new LifecycleDestroy<IAvatar>(avatar);
        eventBus.publish(type.getType(), event);

        context.getViewPort().detachScene(stellarSystem.getNode());
        context.getGuiViewPort().detachScene(display.getNode());
    }

}
