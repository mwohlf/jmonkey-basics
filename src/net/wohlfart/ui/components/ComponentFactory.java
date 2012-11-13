package net.wohlfart.ui.components;

import java.util.HashSet;
import java.util.Set;

import com.jme3.asset.AssetManager;

public class ComponentFactory {

    private final ILookAndFeel lookAndFeel;
    private final AssetManager assetManager;
    private final Set<Window> windowBucket = new HashSet<Window>();

    public ComponentFactory(final ILookAndFeel lookAndFeel,
                            final AssetManager assetManager) {
        this.lookAndFeel = lookAndFeel;
        this.assetManager = assetManager;
    }



    public Window createWindow() {
        Window window =  new Window(lookAndFeel, assetManager);
        windowBucket.add(window);
        return window;
    }

    public Window[] getWindows() {
        return windowBucket.toArray(new Window[windowBucket.size()]);
    }

}
