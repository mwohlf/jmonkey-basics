package net.wohlfart.user;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import org.bushe.swing.event.EventService;
import org.bushe.swing.event.EventServiceLocator;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.InputListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;


/**
 * this object encapsulates the user input 
 * 
 * 
 * 
 * @author michael
 *
 */
public class InputProcessor {
	
	private static final Logger LOGGER = Logger.getLogger(InputProcessor.class.getName());
	
	public static final String BUS_TOPIC_USERSELECT = "BUS_TOPIC_USERSELECT";
	public static final String BUS_TOPIC_EXIT = "BUS_TOPIC_EXIT";
	
	
	
	
	public static final String INPUT_MAPPING_EXIT = "INPUT_MAPPING_EXIT";

	public static final String MOVE_LEFT = "MOVE_LEFT";
	public static final String MOVE_RIGHT = "MOVE_RIGHT";
	public static final String MOVE_UP = "MOVE_UP";
	public static final String MOVE_DOWN = "MOVE_DOWN";
	public static final String MOVE_FORWARD = "MOVE_FORWARD";
	public static final String MOVE_BACKWARD = "MOVE_BACKWARD";
	public static final String MOVE_FORWARD_WHEEL = "MOVE_FORWARD_WHEEL";
	public static final String MOVE_BACKWARD_WHEEL = "MOVE_BACKWARD_WHEEL";

	public static final String ROTATE_LEFT = "ROTATE_LEFT";
	public static final String ROTATE_RIGHT = "ROTATE_RIGHT";
	public static final String ROTATE_UP = "ROTATE_UP";
	public static final String ROTATE_DOWN = "ROTATE_DOWN";
	public static final String ROTATE_CLOCKWISE = "ROTATE_CLOCKWISE";
	public static final String ROTATE_COUNTER_CLOCKWISE = "ROTATE_COUNTER_CLOCKWISE";
	
	public static final String ROTATE_UP_MOUSE = "ROTATE_UP_MOUSE";
	public static final String ROTATE_DOWN_MOUSE = "ROTATE_DOWN_MOUSE";
	public static final String ROTATE_LEFT_MOUSE = "ROTATE_LEFT_MOUSE";
	public static final String ROTATE_RIGHT_MOUSE = "ROTATE_RIGHT_MOUSE";
	
	public static final String MOUSE_BUTTON_LEFT = "MOUSE_BUTTON_LEFT";
	public static final String MOUSE_BUTTON_MIDDLE = "MOUSE_BUTTON_MIDDLE";
	public static final String MOUSE_BUTTON_RIGHT = "MOUSE_BUTTON_RIGHT";
	


	protected final InputManager inputManager;
	protected final IPlayerView playerView;
	protected final EventService eventBus;

	

	// states of the mouse buttons:
	public boolean middleMouseButtonPressed;
	
	public volatile boolean rotationModeEnabled;
	private final Set<IClickListener> clickListeners = new HashSet<IClickListener>();

	
	
	public InputProcessor(
			final InputManager inputManager, 
			final IPlayerView playerView) {
		
		this.inputManager = inputManager;
		this.playerView = playerView;
		initMappings();
		eventBus = EventServiceLocator.getEventBusService();
		initListener();
	}


	protected void initMappings() {
		inputManager.addMapping(INPUT_MAPPING_EXIT, new KeyTrigger(KeyInput.KEY_ESCAPE));

		// keys for moving along the axis
		inputManager.addMapping(MOVE_UP, new KeyTrigger(KeyInput.KEY_Q));
		inputManager.addMapping(MOVE_LEFT, new KeyTrigger(KeyInput.KEY_A));
		inputManager.addMapping(MOVE_RIGHT, new KeyTrigger(KeyInput.KEY_S));
		inputManager.addMapping(MOVE_DOWN, new KeyTrigger(KeyInput.KEY_X));
		inputManager.addMapping(MOVE_FORWARD, new KeyTrigger(KeyInput.KEY_W));
		inputManager.addMapping(MOVE_BACKWARD, new KeyTrigger(KeyInput.KEY_Y));
		//
		inputManager.addMapping(MOVE_FORWARD_WHEEL, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
		inputManager.addMapping(MOVE_BACKWARD_WHEEL, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));		
		// 
		inputManager.addMapping(ROTATE_UP_MOUSE, new MouseAxisTrigger(MouseInput.AXIS_Y, false));
		inputManager.addMapping(ROTATE_LEFT_MOUSE, new MouseAxisTrigger(MouseInput.AXIS_X, false));
		inputManager.addMapping(ROTATE_RIGHT_MOUSE, new MouseAxisTrigger(MouseInput.AXIS_X, true));
		inputManager.addMapping(ROTATE_DOWN_MOUSE, new MouseAxisTrigger(MouseInput.AXIS_Y, true));
		//
		inputManager.addMapping(ROTATE_UP, new KeyTrigger(KeyInput.KEY_UP));
		inputManager.addMapping(ROTATE_LEFT, new KeyTrigger(KeyInput.KEY_LEFT));
		inputManager.addMapping(ROTATE_RIGHT, new KeyTrigger(KeyInput.KEY_RIGHT));
		inputManager.addMapping(ROTATE_DOWN, new KeyTrigger(KeyInput.KEY_DOWN));
		inputManager.addMapping(ROTATE_CLOCKWISE, new KeyTrigger(KeyInput.KEY_PGUP));
		inputManager.addMapping(ROTATE_COUNTER_CLOCKWISE, new KeyTrigger(KeyInput.KEY_PGDN));
		//
		inputManager.addMapping(MOUSE_BUTTON_LEFT, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		inputManager.addMapping(MOUSE_BUTTON_MIDDLE, new MouseButtonTrigger(MouseInput.BUTTON_MIDDLE));	
		inputManager.addMapping(MOUSE_BUTTON_RIGHT, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));	
	}
	

	protected void initListener() {
		addListener(new ActionMoveDigital(), MOVE_LEFT, MOVE_RIGHT, MOVE_UP, MOVE_DOWN, MOVE_FORWARD, MOVE_BACKWARD);
		addListener(new ActionWheelAnalog(), MOVE_FORWARD_WHEEL, MOVE_BACKWARD_WHEEL);
		addListener(new ActionRotateDigital(), ROTATE_LEFT, ROTATE_RIGHT, ROTATE_UP, ROTATE_DOWN, ROTATE_CLOCKWISE, ROTATE_COUNTER_CLOCKWISE);
		addListener(new ActionRotateAnalog(), ROTATE_UP_MOUSE, ROTATE_LEFT_MOUSE, ROTATE_RIGHT_MOUSE, ROTATE_DOWN_MOUSE);
		addListener(new ActionMouseButton(), MOUSE_BUTTON_LEFT, MOUSE_BUTTON_MIDDLE, MOUSE_BUTTON_RIGHT);
		addListener(new ActionListener() {
			@Override
			public void onAction(final String name, boolean isPressed, float tpf) {
				eventBus.publish(BUS_TOPIC_EXIT, name);					
			}  
		}, InputProcessor.INPUT_MAPPING_EXIT);
	}


	// add external listeners,
	public void addListener(InputListener inputListener, String... mappingNames) {
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
			final Vector3f direction;
			// since we move the world and not the cam things are inverted here
			switch (event) {
			case MOVE_FORWARD:
				direction = playerView.getDirection().negate();
				break;
			case MOVE_BACKWARD:
				direction = playerView.getDirection();
				break;
			case MOVE_LEFT:
				direction = playerView.getLeft().negate();
				break;
			case MOVE_RIGHT:
				direction = playerView.getLeft();
				break;
			case MOVE_UP:
				direction = playerView.getUp().negate();
				break;
			case MOVE_DOWN:
				direction = playerView.getUp();
				break;	
			default:
				LOGGER.severe("unknown event: '" + event + "' exiting eventhandler");
				return; // bail out
			}
			direction.multLocal(timePerFrame * SPEED);
			playerView.translationForce(direction.x, direction.y, direction.z);
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
			final Vector3f direction;
			// since we move the world and not the cam things are inverted here
			switch (event) {
			case MOVE_FORWARD_WHEEL:
				direction = playerView.getDirection().negate();
				break;
			case MOVE_BACKWARD_WHEEL:
				direction = playerView.getDirection();
				break;
			default:
				LOGGER.severe("unknown event: '" + event + "' exiting eventhandler");
				return; // bail out
			}
			direction.multLocal(timePerFrame * SPEED);
			playerView.translationForce(direction.x, direction.y, direction.z);
		}
	}

	
	private class ActionRotateDigital implements AnalogListener {
		private final float SPEED = 0.5f;

		@Override
		public void onAnalog(final String event, final float value, final float timePerFrame) {
			final float angle = timePerFrame * SPEED;
			switch (event) {
			case ROTATE_CLOCKWISE:
				playerView.rotationForce(0,0,+angle);
				break;
			case ROTATE_COUNTER_CLOCKWISE:
				playerView.rotationForce(0,0,-angle);
				break;
			case ROTATE_LEFT:
				playerView.rotationForce(0,+angle,0);
				break;
			case ROTATE_RIGHT:
				playerView.rotationForce(0,-angle,0);
				break;
			case ROTATE_UP:
				playerView.rotationForce(+angle,0,0);
				break;
			case ROTATE_DOWN:
				playerView.rotationForce(-angle,0,0);
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
			if (!rotationModeEnabled) {
				return;
			}						
			final float angle =   SPEED * value * timePerFrame;
			switch (event) {
			case ROTATE_LEFT_MOUSE:
				playerView.rotationForce(0,-angle,0);
				break;
			case ROTATE_RIGHT_MOUSE:
				playerView.rotationForce(0,+angle,0);
				break;
			case ROTATE_UP_MOUSE:
				playerView.rotationForce(+angle,0,0);
				break;
			case ROTATE_DOWN_MOUSE:
				playerView.rotationForce(-angle,0,0);
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
			inputManager.setCursorVisible(!isPressed); // hide the cursor when the button is pressed
			switch (event) {
			case MOUSE_BUTTON_LEFT:
		        Vector2f click2d = inputManager.getCursorPosition();
		        Vector3f click3d = playerView.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 0f).clone();
		        Vector3f dir = playerView.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
		        // Aim the ray from the clicked spot forwards.
		        Ray ray = new Ray(click3d, dir);
		        eventBus.publish(BUS_TOPIC_USERSELECT, ray);
		        //clickPerformed(ray);
				break;	
			case MOUSE_BUTTON_MIDDLE:
				synchronized (InputProcessor.this) {
					InputProcessor.this.middleMouseButtonPressed = isPressed;
				}
				break;	
			case MOUSE_BUTTON_RIGHT:
				synchronized (InputProcessor.this) {
					InputProcessor.this.rotationModeEnabled = isPressed;
				}
				break;	
			default:
				LOGGER.severe("unknown event: '" + event + "' exiting eventhandler");
				return; // bail out
			}					
		}	
	}
	

	// FIXME: use the bus here
	
	public void register(IClickListener listener) {
		clickListeners.add(listener);
	}
	
	public void unregister(IClickListener listener) {
		clickListeners.remove(listener);
	}
	
	private void clickPerformed(final Ray ray) {
		for (IClickListener listener : clickListeners) {
			listener.clickPerformed(ray);
		}
	}	
	

}
