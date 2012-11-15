package net.wohlfart.ui.commands;

import net.wohlfart.ui.components.Window;

import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.math.Vector2f;

public class ResizeCommand implements ICommand {

    private Vector2f sizeComponent;
    private Vector2f posComponent;
    private Vector2f startMouse;
    private Window resizable;

    private final Vector2f mouseDelta = new Vector2f();


    public interface IResizable {
        Vector2f getSize();
        void setSize(Vector2f v);
    }


    public ResizeCommand(final Window resizable, final Vector2f startMouse) {
        this.resizable = resizable;
        this.sizeComponent = resizable.getSize();
        this.posComponent = resizable.getLocation();
        this.startMouse = startMouse;
     }


    @Override
    public void execute(MouseButtonEvent evt) {
        mouseDelta.x = evt.getX();
        mouseDelta.y = evt.getY();
        mouseDelta.subtractLocal(startMouse); // delta from the start

        Vector2f newSize = new Vector2f(sizeComponent.getX() + mouseDelta.x, sizeComponent.getY() - mouseDelta.y);
        resizable.setSize(newSize);

        Vector2f newLoc = new Vector2f(posComponent.getX(), posComponent.getY() + mouseDelta.y);
        resizable.setLocation(newLoc);
    }

    @Override
    public void updateCommand(MouseMotionEvent evt) {
        mouseDelta.x = evt.getX();
        mouseDelta.y = evt.getY();
        mouseDelta.subtractLocal(startMouse); // delta from the start

        Vector2f newSize = new Vector2f(sizeComponent.getX() + mouseDelta.x, sizeComponent.getY() - mouseDelta.y);
        resizable.setSize(newSize);

        Vector2f newLoc = new Vector2f(posComponent.getX(), posComponent.getY() + mouseDelta.y);
        resizable.setLocation(newLoc);
    }

}
