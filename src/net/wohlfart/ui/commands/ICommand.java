package net.wohlfart.ui.commands;

import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;

public interface ICommand {

    public abstract void updateCommand(MouseMotionEvent evt);

    public abstract void execute(MouseButtonEvent evt);

}
