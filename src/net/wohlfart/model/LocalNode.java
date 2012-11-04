package net.wohlfart.model;

import org.bushe.swing.event.EventService;
import org.bushe.swing.event.EventServiceLocator;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;

public class LocalNode extends Node {

    protected final EventService eventBus;

    public LocalNode(AssetManager assetManager) {
        eventBus = EventServiceLocator.getEventBusService();
    }

}
