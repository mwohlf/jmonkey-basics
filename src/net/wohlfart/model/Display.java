package net.wohlfart.model;

import net.wohlfart.model.planets.Planet;
import net.wohlfart.user.IPlayerView;

import org.bushe.swing.event.EventService;
import org.bushe.swing.event.EventServiceLocator;
import org.bushe.swing.event.EventTopicSubscriber;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial.CullHint;

public class Display {
	public static final String DISPLAY_NODE = "DISPLAY_NODE";
	
	protected final EventService eventBus;


	private Node delegatee = new Node();

	private final BitmapFont guiFont;

	private final BitmapText perfText;
	private final BitmapText viewText;
	private final BitmapText infoText;

	private final IPlayerView playerView;


	public Display(final AssetManager assetManager, final IPlayerView playerView) {
		delegatee = new Node(DISPLAY_NODE);

		this.playerView = playerView;

		delegatee.setQueueBucket(Bucket.Gui);
		delegatee.setCullHint(CullHint.Never);

		guiFont = assetManager.loadFont("Interface/Fonts/Console.fnt");

		perfText = new BitmapText(guiFont, false);      
		perfText.setSize(guiFont.getCharSet().getRenderedSize());        
		perfText.setColor(ColorRGBA.Blue);                                
		perfText.setLocalTranslation(10, perfText.getLineHeight() * 10, 0); 
		perfText.setText("");                                            
		delegatee.attachChild(perfText);

		viewText = new BitmapText(guiFont, false);      
		viewText.setSize(guiFont.getCharSet().getRenderedSize());       
		viewText.setColor(ColorRGBA.Green);                               
		viewText.setLocalTranslation(10, 200 + viewText.getLineHeight() * 10, 0);
		viewText.setText("");                                             
		delegatee.attachChild(viewText);

		infoText = new BitmapText(guiFont, false);      
		infoText.setSize(guiFont.getCharSet().getRenderedSize());       
		infoText.setColor(ColorRGBA.Red);                               
		infoText.setLocalTranslation(10, 500 + infoText.getLineHeight() * 10, 0);
		infoText.setText("");                                             
		delegatee.attachChild(infoText);
		
		eventBus = EventServiceLocator.getEventBusService();
		eventBus.subscribeStrongly(StellarNode.BUS_TOPIC_STELLAR_SELECT, new EventTopicSubscriber<Planet>() {
			@Override
			public void onEvent(String string, Planet planet) {			
				infoText.setText("\n----> " + planet);
			}
		});

	}

	public Node getNode() {
		return delegatee;
	}

	public void updateLogicalState(final float timePerFrame) {
		delegatee.updateLogicalState(timePerFrame);

		perfText.setText(""
				+ "seconds per frame: " + timePerFrame + "\n"
				+ "frames per second: " + 1f/timePerFrame + "\n"
				); 

		viewText.setText(""
				+ "direction: " + playerView.getDirection() + "\n"
				+ "up: " + playerView.getDirection() + "\n"
				+ "left: " + playerView.getLeft() + "\n"
				); 
	}


//	public void showHits(final CollisionResults results) {
//		
//		
//		
//		StringBuilder stringBuilder =  new StringBuilder();
//		for (int i = 0; i < results.size(); i++) {
//			// (For each “hit”, we know distance, impact point, geometry.)
//			float dist = results.getCollision(i).getDistance();
//			Vector3f pt = results.getCollision(i).getContactPoint();
//			String target = results.getCollision(i).getGeometry().getName();
//			stringBuilder.append("Selection #" + i + ": " + target + " at " + pt + ", " + dist + " WU away. \n");
//		}
//		
//		if (results.size() == 0) {
//			return;
//		}
//		
//		// thats what the user picked:
//		Geometry target = results.getClosestCollision().getGeometry();
//		
//	}


	public void updateGeometricState() {
		delegatee.updateGeometricState();
	}
}
