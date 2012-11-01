package net.wohlfart.user;

import java.util.logging.Logger;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.InputListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;


/**
 * this object encapsulates the user input and acts on a avatar object
 * 
 * 
 * 
 */
public class InputProcessor {

	private static final Logger LOGGER = Logger.getLogger(InputProcessor.class.getName());


	enum Key {
		MOVE_LEFT,
		MOVE_RIGHT,
		MOVE_UP,
		MOVE_DOWN,
		MOVE_FORWARD,
		MOVE_BACKWARD,
		MOVE_FORWARD_WHEEL,
		MOVE_BACKWARD_WHEEL,

		ROTATE_LEFT,
		ROTATE_RIGHT,
		ROTATE_UP,
		ROTATE_DOWN,
		ROTATE_CLOCKWISE,
		ROTATE_COUNTER_CLOCKWISE,

		ROTATE_UP_MOUSE,
		ROTATE_DOWN_MOUSE,
		ROTATE_LEFT_MOUSE,
		ROTATE_RIGHT_MOUSE,

		MOUSE_BUTTON_LEFT,
		MOUSE_BUTTON_MIDDLE,
		MOUSE_BUTTON_RIGHT;
	}


	// user input source
	protected final InputManager inputManager;

	// target object to move:
	protected IAvatar avatar;

	// some state information
	public volatile boolean rotationModeEnabled;

	// action processors
	protected ActionMoveDigital actionMoveDigital = new ActionMoveDigital();
	protected ActionWheelAnalog actionWheelAnalog = new ActionWheelAnalog();
	protected ActionRotateDigital actionRotateDigital = new ActionRotateDigital();
	protected ActionRotateAnalog actionRotateAnalog = new ActionRotateAnalog();
	protected ActionMouseButton actionMouseButton = new ActionMouseButton();



	public InputProcessor(final InputManager inputManager) {		
		this.inputManager = inputManager;
	}


	public void attachAvatar(final IAvatar avatar) {
		this.avatar = avatar;
		initMappings();
		initListener();
	}


	public void detachCurrentAvatar() {
		this.avatar = null;
		for (Key key : Key.values()) {
			inputManager.deleteMapping(key.name());
		}

		inputManager.removeListener(actionMoveDigital);
		inputManager.removeListener(actionWheelAnalog);
		inputManager.removeListener(actionRotateDigital);
		inputManager.removeListener(actionRotateAnalog);
		inputManager.removeListener(actionMouseButton);
	}


	protected void initMappings() {
		// keys for moving along the axis
		inputManager.addMapping(Key.MOVE_UP.name(), new KeyTrigger(KeyInput.KEY_Q));
		inputManager.addMapping(Key.MOVE_LEFT.name(), new KeyTrigger(KeyInput.KEY_A));
		inputManager.addMapping(Key.MOVE_RIGHT.name(), new KeyTrigger(KeyInput.KEY_S));
		inputManager.addMapping(Key.MOVE_DOWN.name(), new KeyTrigger(KeyInput.KEY_X));
		inputManager.addMapping(Key.MOVE_FORWARD.name(), new KeyTrigger(KeyInput.KEY_W));
		inputManager.addMapping(Key.MOVE_BACKWARD.name(), new KeyTrigger(KeyInput.KEY_Y));
		//
		inputManager.addMapping(Key.MOVE_FORWARD_WHEEL.name(), new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
		inputManager.addMapping(Key.MOVE_BACKWARD_WHEEL.name(), new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));		
		// 
		inputManager.addMapping(Key.ROTATE_UP_MOUSE.name(), new MouseAxisTrigger(MouseInput.AXIS_Y, false));
		inputManager.addMapping(Key.ROTATE_LEFT_MOUSE.name(), new MouseAxisTrigger(MouseInput.AXIS_X, false));
		inputManager.addMapping(Key.ROTATE_RIGHT_MOUSE.name(), new MouseAxisTrigger(MouseInput.AXIS_X, true));
		inputManager.addMapping(Key.ROTATE_DOWN_MOUSE.name(), new MouseAxisTrigger(MouseInput.AXIS_Y, true));
		//
		inputManager.addMapping(Key.ROTATE_UP.name(), new KeyTrigger(KeyInput.KEY_UP));
		inputManager.addMapping(Key.ROTATE_LEFT.name(), new KeyTrigger(KeyInput.KEY_LEFT));
		inputManager.addMapping(Key.ROTATE_RIGHT.name(), new KeyTrigger(KeyInput.KEY_RIGHT));
		inputManager.addMapping(Key.ROTATE_DOWN.name(), new KeyTrigger(KeyInput.KEY_DOWN));
		inputManager.addMapping(Key.ROTATE_CLOCKWISE.name(), new KeyTrigger(KeyInput.KEY_PGUP));
		inputManager.addMapping(Key.ROTATE_COUNTER_CLOCKWISE.name(), new KeyTrigger(KeyInput.KEY_PGDN));
		//
		inputManager.addMapping(Key.MOUSE_BUTTON_LEFT.name(), new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		inputManager.addMapping(Key.MOUSE_BUTTON_MIDDLE.name(), new MouseButtonTrigger(MouseInput.BUTTON_MIDDLE));	
		inputManager.addMapping(Key.MOUSE_BUTTON_RIGHT.name(), new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));	
	}


	protected void initListener() {
		addListener(actionMoveDigital, 
				Key.MOVE_LEFT.name(), 
				Key.MOVE_RIGHT.name(), 
				Key.MOVE_UP.name(), 
				Key.MOVE_DOWN.name(), 
				Key.MOVE_FORWARD.name(), 
				Key.MOVE_BACKWARD.name());

		addListener(actionWheelAnalog, 
				Key.MOVE_FORWARD_WHEEL.name(), 
				Key.MOVE_BACKWARD_WHEEL.name());

		addListener(actionRotateDigital, 
				Key.ROTATE_LEFT.name(), 
				Key.ROTATE_RIGHT.name(), 
				Key.ROTATE_UP.name(), 
				Key.ROTATE_DOWN.name(), 
				Key.ROTATE_CLOCKWISE.name(), 
				Key.ROTATE_COUNTER_CLOCKWISE.name());

		addListener(actionRotateAnalog, 
				Key.ROTATE_UP_MOUSE.name(), 
				Key.ROTATE_LEFT_MOUSE.name(), 
				Key.ROTATE_RIGHT_MOUSE.name(), 
				Key.ROTATE_DOWN_MOUSE.name());

		addListener(actionMouseButton, 
				Key.MOUSE_BUTTON_LEFT.name(), 
				Key.MOUSE_BUTTON_MIDDLE.name(), 
				Key.MOUSE_BUTTON_RIGHT.name());
	}


	// add external listeners,
	protected void addListener(InputListener inputListener, String... mappingNames) {
		inputManager.addListener(inputListener, mappingNames);
	}






	/*********
	 * moving the world around the cam relative to the cam itself e.g. moving right means 
	 * moving towards whatever is right for the cam
	 */
	private class ActionMoveDigital implements AnalogListener {
		private final float SPEED = 3f;

		/**
		 * seems like this method is called once per rendering run
		 * value == timePerFrame for digital events
		 */
		@Override
		public void onAnalog(final String event, final float value, final float timePerFrame) {
			if (avatar == null) {
				return;
			}
			final Vector3f direction;
			// since we move the world and not the cam things are inverted here
			switch (Key.valueOf(event)) {
			case MOVE_FORWARD:
				direction = avatar.getDirection().negate();
				break;
			case MOVE_BACKWARD:
				direction = avatar.getDirection();
				break;
			case MOVE_LEFT:
				direction = avatar.getLeft().negate();
				break;
			case MOVE_RIGHT:
				direction = avatar.getLeft();
				break;
			case MOVE_UP:
				direction = avatar.getUp().negate();
				break;
			case MOVE_DOWN:
				direction = avatar.getUp();
				break;	
			default:
				LOGGER.severe("unknown event: '" + event + "' exiting eventhandler");
				return; // bail out
			}
			direction.multLocal(timePerFrame * SPEED);
			avatar.translationForce(direction.x, direction.y, direction.z);
		}
	}


	/*********
	 * moving the world around the cam relative to the cam itself e.g. moving right means 
	 * moving towards whatever is right for the cam
	 */
	private class ActionWheelAnalog implements AnalogListener {
		private final float SPEED = 8f;

		/**
		 * seems like this method is called once per rendering run
		 * value == timePerFrame for digital events
		 */
		@Override
		public void onAnalog(final String event, final float value, final float timePerFrame) {
			if (avatar == null) {
				return;
			}
			final Vector3f direction;
			// since we move the world and not the cam things are inverted here
			switch (Key.valueOf(event)) {
			case MOVE_FORWARD_WHEEL:
				direction = avatar.getDirection().negate();
				break;
			case MOVE_BACKWARD_WHEEL:
				direction = avatar.getDirection();
				break;
			default:
				LOGGER.severe("unknown event: '" + event + "' exiting eventhandler");
				return; // bail out
			}
			direction.multLocal(timePerFrame * SPEED);
			avatar.translationForce(direction.x, direction.y, direction.z);
		}
	}


	private class ActionRotateDigital implements AnalogListener {
		private final float SPEED = 0.5f;

		@Override
		public void onAnalog(final String event, final float value, final float timePerFrame) {
			if (avatar == null) {
				return;
			}
			final float angle = timePerFrame * SPEED;
			switch (Key.valueOf(event)) {
			case ROTATE_CLOCKWISE:
				avatar.rotationForce(0,0,+angle);
				break;
			case ROTATE_COUNTER_CLOCKWISE:
				avatar.rotationForce(0,0,-angle);
				break;
			case ROTATE_LEFT:
				avatar.rotationForce(0,+angle,0);
				break;
			case ROTATE_RIGHT:
				avatar.rotationForce(0,-angle,0);
				break;
			case ROTATE_UP:
				avatar.rotationForce(+angle,0,0);
				break;
			case ROTATE_DOWN:
				avatar.rotationForce(-angle,0,0);
				break;	
			default:
				LOGGER.severe("unknown event: '" + event + "' exiting eventhandler");
				return; // bail out
			}							

		}
	}

	/*********
	 * moving the world around the cam relative to the cam itself e.g. moving right means 
	 * moving towards whatever is right for the cam
	 */
	private class ActionRotateAnalog implements AnalogListener {
		private final float SPEED = 100f;
		/**
		 * seems like this method is called once per rendering run value == timePerFrame for digital events
		 * for initial calls the value seems to be pretty high, later calls have much lower values...
		 */
		@Override
		public void onAnalog(final String event, final float value, final float timePerFrame) {	
			if (avatar == null) {
				return;
			}
			if (!rotationModeEnabled) {
				return;
			}						
			final float angle =   SPEED * value * timePerFrame;
			switch (Key.valueOf(event)) {
			case ROTATE_LEFT_MOUSE:
				avatar.rotationForce(0,-angle,0);
				break;
			case ROTATE_RIGHT_MOUSE:
				avatar.rotationForce(0,+angle,0);
				break;
			case ROTATE_UP_MOUSE:
				avatar.rotationForce(+angle,0,0);
				break;
			case ROTATE_DOWN_MOUSE:
				avatar.rotationForce(-angle,0,0);
				break;	
			default:
				LOGGER.severe("unknown event: '" + event + "' exiting eventhandler");
				return; // bail out
			}						
		}
	}


	private class ActionMouseButton implements ActionListener {
		@Override
		public void onAction(final String event, final boolean isPressed, final float timePerFrame) {
			if (avatar == null) {
				return;
			}
			inputManager.setCursorVisible(!isPressed); // hide the cursor when the button is pressed

			switch (Key.valueOf(event)) {
			case MOUSE_BUTTON_MIDDLE:
			case MOUSE_BUTTON_RIGHT:
				synchronized (InputProcessor.this) {
					InputProcessor.this.rotationModeEnabled = isPressed;
				}
				break;	
			case MOUSE_BUTTON_LEFT:
				if (isPressed) {
					Vector2f click2d = inputManager.getCursorPosition();
					avatar.actionClick(click2d);
				}
				break;	
			default:
				LOGGER.severe("unknown event: '" + event + "' exiting eventhandler");
				return; // bail out
			}					
		}	
	}


}
