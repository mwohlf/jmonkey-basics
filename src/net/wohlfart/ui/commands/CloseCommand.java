package net.wohlfart.ui.commands;

import net.wohlfart.ui.components.Window;

import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;

public class CloseCommand implements ICommand {

    private Window window;

    public CloseCommand(final Window window) {
        this.window = window;
    }

    @Override
    public void updateCommand(MouseMotionEvent evt) {
        // no update needed
    }

    @Override
    public void execute(MouseButtonEvent evt) {
        window.close();
    }

}
