package net.wohlfart;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import net.wohlfart.model.Display;
import net.wohlfart.model.StellarSystem;
import net.wohlfart.user.IPlayerView;
import net.wohlfart.user.InputProcessor;
import net.wohlfart.user.PlayerViewImpl;

import org.bushe.swing.event.EventService;
import org.bushe.swing.event.EventServiceLocator;
import org.bushe.swing.event.EventTopicSubscriber;

import com.jme3.app.Application;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.system.AppSettings;

public class Game extends Application {
		
	protected final EventService eventBus;
	
	
    protected BitmapFont guiFont;
    protected BitmapText fpsText;
    
    
    
    protected IPlayerView playerView;
    protected InputProcessor inputProcessor;
    protected StellarSystem stellarSystem;
    protected Display display;


	Game() {
		setSettings(getFullscreenSetting());
		//setSettings(getWindowSetting());	
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
        
        // call user code
		initCam();

        guiFont = loadGuiFont();
        
        stellarSystem = new StellarSystem(getAssetManager());
        getViewPort().attachScene(stellarSystem.getNode());
        
        playerView = new PlayerViewImpl(stellarSystem, getCamera());

        display = new Display(getAssetManager(), playerView);        
        getGuiViewPort().attachScene(display.getNode());	    
        
     
        inputProcessor = new InputProcessor(getInputManager(), playerView);
        
        eventBus.subscribeStrongly(InputProcessor.BUS_TOPIC_EXIT, new EventTopicSubscriber<Object>() {
			@Override
			public void onEvent(String string, Object object) {
				stop(false); // don't wait, true hangs on exit	
			}
		});
        
        
//        inputProcessor.register(new IClickListener() {			
//			@Override
//			public void clickPerformed(final Ray ray) {
//				CollisionResults results = new CollisionResults();
//				stellarSystem.collideWithPlanets(ray, results);
//				display.showHits(results);
//			}
//		});

		initAmbient();
		initObjects();
		initControls();
		initHud();
    }

    


	@Override
    public void update() {
        super.update(); // makes sure to execute AppTasks
        if (speed == 0 || paused) {
            return;
        }

        float timePerFrame = timer.getTimePerFrame() * speed;

        // update states
        stateManager.update(timePerFrame);
 
        stellarSystem.updateLogicalState(timePerFrame);
        display.updateLogicalState(timePerFrame);
        
        stellarSystem.updateGeometricState();
        display.updateGeometricState();

        // render states
        stateManager.render(renderManager);
        renderManager.render(timePerFrame, context.isRenderable());
        stateManager.postRender();        
    }

    /**
     *  Creates the font that will be set to the guiFont field
     *  and subsequently set as the font for the stats text.
     */
    protected BitmapFont loadGuiFont() {
        return assetManager.loadFont("Interface/Fonts/Default.fnt");
    }


	protected void initCam() {
		final Vector3f lookAt = new Vector3f(0,0,-1);
		final Camera cam = getCamera();
		cam.setLocation(Vector3f.ZERO);
		cam.lookAt(lookAt, Vector3f.UNIT_Y /* world up */);
	}
	
	protected void initAmbient() {
		viewPort.setBackgroundColor(ColorRGBA.LightGray);
		
	}
	
	protected void initObjects() {
		
	}


	private void initControls() {
	//	inputManager.clearRawInputListeners();
	//	inputManager.addRawInputListener(listener)
		
	//	new InputProcessor(mouseInput, keyInput, joyInput, touchInput);
		
	}


	private void initHud() {
		// TODO Auto-generated method stub
		
	}

}
