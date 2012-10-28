package net.wohlfart;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.Iterator;

import net.wohlfart.model.Lights;
import net.wohlfart.model.StellarSystem;
import net.wohlfart.user.InputProcessor;
import net.wohlfart.user.PlayerViewImpl;

import com.jme3.app.Application;
import com.jme3.app.StatsAppState;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.controls.ActionListener;
import com.jme3.light.Light;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;

public class Game extends Application {
	
	
    
    protected final Node guiNode = new Node("Gui Node") {{
    	setQueueBucket(Bucket.Gui);
    	setCullHint(CullHint.Never);
    }};
    protected BitmapFont guiFont;
    protected BitmapText fpsText;
    protected InputProcessor inputProcessor;
    protected StellarSystem stellarSystem;


	Game() {
		setSettings(getFullscreenSetting());
		//setSettings(getWindowSetting());
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
        getGuiViewPort().attachScene(guiNode);	        
        inputProcessor = new InputProcessor(getInputManager(), new PlayerViewImpl(stellarSystem, getCamera()));
        inputProcessor.addListener(new ActionListener() {
				@Override
				public void onAction(String name, boolean isPressed, float tpf) {
					stop(false); // don't wait, true hangs on exit						
				}  
			}, InputProcessor.INPUT_MAPPING_EXIT);


        if (stateManager.getState(StatsAppState.class) != null) {
            // Some of the tests rely on having access to fpsText
            // for quick display.  Maybe a different way would be better.
            //stateManager.getState(StatsAppState.class).setFont(guiFont);
            fpsText = stateManager.getState(StatsAppState.class).getFpsText();
        }


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
        guiNode.updateLogicalState(timePerFrame);
        
        stellarSystem.updateGeometricState();
        guiNode.updateGeometricState();

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
		Camera cam = getCamera();
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
