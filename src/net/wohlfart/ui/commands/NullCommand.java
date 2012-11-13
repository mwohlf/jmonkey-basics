package net.wohlfart.ui.commands;

import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;

public class NullCommand implements ICommand {
    public static final ICommand INSTANCE = new NullCommand();

    private NullCommand() {

    };

    @Override
    public void execute(MouseButtonEvent evt) {
        // do nothing
    }

    @Override
    public void updateCommand(MouseMotionEvent evt) {
        // do nothing
    }

}
