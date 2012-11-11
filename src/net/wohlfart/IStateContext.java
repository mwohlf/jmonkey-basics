package net.wohlfart;

import org.bushe.swing.event.EventService;

import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;


// interface for the state objects to avoid cyclic dependencies...
public interface IStateContext {

    ViewPort getViewPort();

    ViewPort getGuiViewPort();

    EventService getEventBus();

    Camera getCamera();

    AssetManager getAssetManager();

    InputManager getInputManager();

}
