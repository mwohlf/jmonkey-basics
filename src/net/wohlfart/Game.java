package net.wohlfart;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import net.wohlfart.states.StellarState;

import org.bushe.swing.event.EventService;
import org.bushe.swing.event.EventServiceLocator;

import com.jme3.app.Application;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.system.AppSettings;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

/**
 * hosting application states
 */
public class Game extends Application {
	private static final String PATH_TO_NIFTY_UI = "interface/nifty.xml";
	private static final String INITIAL_UI_SCREEN_ID = "intro";  
	private static final String INPUT_MAPPING_EXIT = "INPUT_MAPPING_EXIT";  // event key
	
	public static final String TEXT_MESSAGE = "TEXT_MESSAGE";  // event bus message channel
	

	private final EventService eventBus;

	private Nifty nifty;



	Game() {
		//setSettings(getFullscreenSetting());
		setSettings(getWindowSetting());	
		eventBus = EventServiceLocator.getEventBusService();
	}
	
	
	protected AppSettings getFullscreenSetting() {
		GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		DisplayMode[] modes = device.getDisplayModes();
		int i=0; // note: there are usually several, let's pick the first
		AppSettings settings = new AppSettings(true);
		settings.setResolution(modes[i].getWidth(),modes[i].getHeight());
		settings.setFrequency(modes[i].getRefreshRate());
		settings.setDepthBits(modes[i].getBitDepth());
		settings.setFullscreen(device.isFullScreenSupported());
		// slower but looks better
		settings.setVSync(true); 
		// anti-aliasing can be a big performance problem...
		settings.setSamples(8); 
		return settings;
	}
	
	protected AppSettings getWindowSetting() {
		GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		DisplayMode[] modes = device.getDisplayModes();
		int i=0; // note: there are usually several, let's pick the first
		AppSettings settings = new AppSettings(true);
		settings.setResolution(modes[i].getWidth()/2,modes[i].getHeight()/2);
		settings.setFrequency(modes[i].getRefreshRate());
		settings.setDepthBits(modes[i].getBitDepth());
		settings.setFullscreen(false);
		settings.setVSync(true); 
		settings.setSamples(8); 
		return settings;
	}

	
	
	
    @Override
    public void initialize() {
        super.initialize();
        
        // hook an exit event
		getInputManager().addMapping(INPUT_MAPPING_EXIT, new KeyTrigger(KeyInput.KEY_ESCAPE));
		getInputManager().addListener(new ActionListener() {
			@Override
			public void onAction(final String name, boolean isPressed, float tpf) {
				stop(false); // don't wait, true hangs on exit			
			}  
		}, INPUT_MAPPING_EXIT);
  
		getViewPort().setBackgroundColor(ColorRGBA.LightGray);
		
		// init a UI
	    NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager,
                   inputManager,
                   audioRenderer,
                   guiViewPort);	    
	    nifty = niftyDisplay.getNifty();
	    nifty.fromXml(PATH_TO_NIFTY_UI, INITIAL_UI_SCREEN_ID);

	    // attach the nifty display to the gui view port as a processor
	    guiViewPort.addProcessor(niftyDisplay);

		getStateManager().attach(new StellarState());          
    }

    
    private Element getElement(final String id) {
    	return nifty.getCurrentScreen().findElementByName(id);
    }
    

    // event loop
	@Override
    public void update() {
        super.update(); // makes sure to execute AppTasks

        float timePerFrame = timer.getTimePerFrame() * speed;
        // update states
        stateManager.update(timePerFrame);
        // render states
        stateManager.render(getRenderManager());
        // no idea what this is needed for
        renderManager.render(timePerFrame, context.isRenderable());
        // finish update
        stateManager.postRender();        
    }
	
	
	// exit the game
	@Override
	public void stop(boolean waitFor) {
		// do some cleanup on exit
		
		super.stop(waitFor);
	};


	public EventService getEventBus() {
		return eventBus; 
	}
	
	public Screen getCurrentNiftyScreen() {
		return nifty.getCurrentScreen();
	}


}
