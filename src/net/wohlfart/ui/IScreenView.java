package net.wohlfart.ui;

import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.scene.Node;

public interface IScreenView {

    public abstract Node getNode();

    public abstract void mouseMotion(MouseMotionEvent evt);

    public abstract void mouseButton(MouseButtonEvent evt);

}
