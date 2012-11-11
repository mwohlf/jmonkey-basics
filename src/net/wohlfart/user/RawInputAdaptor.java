package net.wohlfart.user;

import java.util.logging.Logger;

import com.jme3.input.RawInputListener;
import com.jme3.input.event.JoyAxisEvent;
import com.jme3.input.event.JoyButtonEvent;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.input.event.TouchEvent;


/**
 * empty implementation
 */
public class RawInputAdaptor implements RawInputListener {
    private static final Logger LOGGER = Logger.getLogger(RawInputAdaptor.class.getName());


    @Override
    public void onMouseMotionEvent(MouseMotionEvent evt) {
        LOGGER.info("onMouseMotionEvent: + evt");
    }

    @Override
    public void onMouseButtonEvent(MouseButtonEvent evt) {
        LOGGER.info("onMouseButtonEvent: " + evt);
    }

    @Override
    public void onKeyEvent(KeyInputEvent evt) {
        LOGGER.info("onKeyEvent: " + evt);
    }

    @Override
    public void onTouchEvent(TouchEvent evt) {
        LOGGER.info("onTouchEvent: " + evt);
    }

    @Override
    public void onJoyAxisEvent(JoyAxisEvent evt) {
        LOGGER.info("onJoyAxisEvent: " + evt);
    }

    @Override
    public void onJoyButtonEvent(JoyButtonEvent evt) {
        LOGGER.info("onJoyButtonEvent: " + evt);
    }

    @Override
    public void beginInput() {
        //LOGGER.info("beginInput()");
    }

    @Override
    public void endInput() {
       // LOGGER.info("beginInput()");
    }


}
