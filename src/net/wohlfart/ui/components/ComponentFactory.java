package net.wohlfart.ui.components;

import com.jme3.asset.AssetManager;

public class ComponentFactory {

    private final ILookAndFeel lookAndFeel;
    private final AssetManager assetManager;

    public ComponentFactory(final ILookAndFeel lookAndFeel, final AssetManager assetManager) {
        this.lookAndFeel = lookAndFeel;
        this.assetManager = assetManager;
    }



    public Window createWindow() {
        return new Window(lookAndFeel, assetManager);
    }

}
