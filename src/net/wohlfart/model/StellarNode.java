package net.wohlfart.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import net.wohlfart.model.planets.AbstractPlanet;
import net.wohlfart.model.planets.Planet;
import net.wohlfart.user.InputProcessor;

import org.bushe.swing.event.EventService;
import org.bushe.swing.event.EventServiceLocator;
import org.bushe.swing.event.EventTopicSubscriber;

import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class StellarNode extends Node {

	public static final String BUS_TOPIC_STELLAR_SELECT = "BUS_TOPIC_STELLAR_SELECT";

	protected final EventService eventBus;

	public StellarNode(final AssetManager assetManager) {		
		setQueueBucket(Bucket.Sky);
		eventBus = EventServiceLocator.getEventBusService();
		eventBus.subscribeStrongly(InputProcessor.BUS_TOPIC_USERSELECT, new EventTopicSubscriber<Object>() {
			@Override
			public void onEvent(String string, Object ray) {
				System.out.println("onEvent: " + InputProcessor.BUS_TOPIC_USERSELECT);
				CollisionResults results = new CollisionResults();
				collideWith((Ray)ray, results);				
				CollisionResult collisionResult = results.getClosestCollision();
				if (collisionResult != null) {
					Geometry geometry = collisionResult.getGeometry(); // use a planet hashmap to resolve the planet
					Planet planet = (Planet) geometry.getUserData(AbstractPlanet.PLANET_KEY);
					eventBus.publish(BUS_TOPIC_STELLAR_SELECT, planet);
				}
			}
		});
	}


	@Override
	public int attachChild(Spatial child) {
		return super.attachChild(child);
	}


	/**
	 * sort for correct rendering in the Bucket.Sky queue
	 */
	void sortChildren() {
		ArrayList<Spatial> list = new ArrayList<Spatial>();
		list.addAll(children);		
		children.clear();

		Collections.sort(list, new Comparator<Spatial>() {
			@Override
			public int compare(Spatial spatial1, Spatial spatial2) {
				return -Float.compare(
						spatial1.getWorldTranslation().length(), 
						spatial2.getWorldTranslation().length());
			}			
		});

		for (Spatial spatial : list) {
			children.add(spatial);
		}
	}
	
	
	


}
