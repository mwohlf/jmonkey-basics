package net.wohlfart.model;

import java.util.Iterator;
import java.util.Random;

import net.wohlfart.model.planets.AbstractPlanet;
import net.wohlfart.model.planets.Planet;

import com.jme3.asset.AssetManager;
import com.jme3.light.Light;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class StellarSystem {
	
	public static final String ROOT_NODE = "ROOT_NODE";
	public static final String SKY_CUBE = "SKY_CUBE";
	public static final String LOCAL_NODE = "LOCAL_NODE";
	public static final String STELLAR_NODE = "STELLAR_NODE";


	private final Node delegatee;
	
	private final LocalNode localNode;
	private final StellarNode stellarNode;
	private final SkyCube skyCube;
	


	public StellarSystem(final AssetManager assetManager) {
		delegatee = new Node(ROOT_NODE);
		// skyCube and stellarNode are in the Sky rendering bucket, 
		// the order of adding them is important
		delegatee.attachChild(skyCube = new SkyCube(assetManager));
		delegatee.attachChild(stellarNode = new StellarNode(assetManager));
		// this contains local stuff like ships etc.
		delegatee.attachChild(localNode = new LocalNode(assetManager));
		
		skyCube.setName(SKY_CUBE);
		stellarNode.setName(STELLAR_NODE);
		localNode.setName(LOCAL_NODE);
		
		Iterator<Light> iterator = new Lights().iterator();
		while (iterator.hasNext()) {
			delegatee.addLight(iterator.next());
		}	
				
		
		int planetCount = 10;
		Random random = new Random(1);
		for (int i = 1; i <= planetCount; i++) {
			Planet planet = new Planet(assetManager, random.nextLong());
			add(planet);
			float rad = (FastMath.TWO_PI / (float) planetCount) * (float)i;
			planet.setLocalTranslation(
					FastMath.sin(rad) * 200,
					0,
					FastMath.cos(rad) * 200); 
		}		

		//  construction ahead
		//
		// rootNode.attachChild(new CoordAxis(assetManager));
		// rootNode.attachChild(new CoordSperes(assetManager));
		// add(new Ship(assetManager));
				
		stellarNode.sortChildren();
	}

	
	public void add(final AbstractPlanet planet) {
		stellarNode.attachChild(planet);
	}


	
	public void add(final Ship ship) {
		localNode.attachChild(ship);
	}


	public Spatial getNode() {
		return delegatee;
	}


	public void updateLogicalState(final float timePerFrame) {
		delegatee.updateLogicalState(timePerFrame);
	}


	public void updateGeometricState() {
		delegatee.updateGeometricState();
	}


	public void move(final Vector3f vector) {
		localNode.move(vector);
		stellarNode.move(vector.mult(1f));	
		//stellarNode.move(vector.mult(0.1f));		
	}



}
