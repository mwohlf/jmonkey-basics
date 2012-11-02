package net.wohlfart.states;

import net.wohlfart.Game;
import net.wohlfart.model.StellarSystem;
import net.wohlfart.model.hud.DisplayNode;
import net.wohlfart.user.AvatarImpl;
import net.wohlfart.user.InputProcessor;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

import de.lessvoid.nifty.controls.Console;
import de.lessvoid.nifty.elements.Element;


/**
 * this game state is where we show planets and starships and stuff
 */
public class StellarState extends AbstractAppState {
	private static final String CONSOLE_NAME = "console";
	
	protected Game application;

	protected StellarSystem stellarSystem;
	protected DisplayNode display;
	protected InputProcessor inputProcessor;

	
	// setup
	@Override
    public void initialize(AppStateManager stateManager, Application game) {
        super.initialize(stateManager, application);
        this.application = (Game) game;
        
        // initial setup camera for this state	
		final Camera cam = application.getCamera();
		cam.setLocation(Vector3f.ZERO);
		final Vector3f lookAt = new Vector3f(0,0,-1);
		cam.lookAt(lookAt, Vector3f.UNIT_Y /* world up */);
        
        // attach stellar view
        stellarSystem = new StellarSystem(application.getAssetManager(), application.getEventBus());
        application.getViewPort().attachScene(stellarSystem.getNode());

        AvatarImpl playerView = new AvatarImpl(stellarSystem, application.getCamera());
        inputProcessor = new InputProcessor(application.getInputManager());
        inputProcessor.attachAvatar(playerView);

        display = new DisplayNode(application.getAssetManager(), playerView, application.getEventBus());        
        application.getGuiViewPort().attachScene(display.getNode());	    
        
        
        Element elem = application.getCurrentNiftyScreen().findElementByName(CONSOLE_NAME);
        System.out.println("elem is: " + elem);

        
        Console console = application.getCurrentNiftyScreen().findNiftyControl(CONSOLE_NAME, Console.class);
        if (console != null) {
        	console.output("Hello :)");
        }
        
    }
	

    // update state
	@Override
    public void update(final float timePerFrame) {
        super.update(timePerFrame);  // makes sure to execute AppTasks
        


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
        application.getViewPort().detachScene(stellarSystem.getNode());
        application.getGuiViewPort().detachScene(display.getNode());       
    }

}
