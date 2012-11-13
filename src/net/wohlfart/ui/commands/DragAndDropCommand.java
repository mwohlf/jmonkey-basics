package net.wohlfart.ui.commands;

import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.math.Vector2f;


public class DragAndDropCommand implements ICommand {

    private Vector2f startComponent;
    private Vector2f startMouse;
    private IDragAndDropable dragAndDropable;


    public interface IDragAndDropable {
        Vector2f getLocation();
        void setLocation(Vector2f v);
    }


    public DragAndDropCommand(final IDragAndDropable dragAndDropable, final Vector2f startMouse) {
        this.dragAndDropable = dragAndDropable;
        this.startComponent = dragAndDropable.getLocation();
        this.startMouse = startMouse;
    }

    @Override
    public void updateCommand(final MouseMotionEvent evt) {
        Vector2f newLoc = new Vector2f(evt.getX(), evt.getY())
            .subtractLocal(startMouse.subtract(startComponent));
        dragAndDropable.setLocation(newLoc);
    }

    @Override
    public void execute(final MouseButtonEvent evt) {
        Vector2f newLoc = new Vector2f(evt.getX(), evt.getY())
            .addLocal(startComponent.subtract(startMouse));
        dragAndDropable.setLocation(newLoc);
    }


}
