package net.wohlfart.ui.components;

import com.jme3.math.ColorRGBA;

public interface ILookAndFeel {
    
    public static final String WINDOW_BACKGROUND = "WINDOW_BACKGROUND";

    ColorRGBA getColorRGBA(String key);

}
