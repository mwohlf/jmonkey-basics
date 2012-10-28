package net.wohlfart.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.jme3.asset.AssetManager;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class StellarNode extends Node {

	public StellarNode(final AssetManager assetManager) {		
        setQueueBucket(Bucket.Sky);
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