package net.wohlfart.ui.components;

public class MouseEvent {
    enum Type {
        MOVE,
        CLICK,
        DRAG,
        BUTTON1,
        BUTTON2,
        BUTTON3;
    }

    final Type type;
    final int x;
    final int y;


    MouseEvent(Type type, int x, int y) {
        this.type = type;
        this.x = x;
        this.y = y;
    }


}
