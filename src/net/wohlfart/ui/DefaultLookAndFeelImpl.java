package net.wohlfart.ui;

import java.util.HashMap;

import net.wohlfart.ui.components.ILookAndFeel;

import com.jme3.math.ColorRGBA;


public class DefaultLookAndFeelImpl implements ILookAndFeel {

    @SuppressWarnings("serial")
    private HashMap<String, ColorRGBA> colors = new HashMap<String, ColorRGBA>() {{
        put(WINDOW_BACKGROUND, new ColorRGBA(0.3f, 0.3f, 0.7f, 0.5f));
    }};





    public ColorRGBA getColorRGBA(String key) {
        return colors.get(key);
    }

}
