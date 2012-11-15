package net.wohlfart.user;

import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;

// implement if you want to be notified by the InputProcessor about any mouse events
public interface IMouseAware {

    public abstract void mouseMotion(final MouseMotionEvent evt);

    public abstract void mouseButton(final MouseButtonEvent evt);
}
