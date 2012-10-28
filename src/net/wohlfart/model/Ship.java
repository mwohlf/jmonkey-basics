package net.wohlfart.model;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class Ship extends Node {
	
	
	public Ship(AssetManager assetManager) {
		Spatial ship = assetManager.loadModel("models/tinyShip2.j3o");
		this.attachChild(ship);
	}

    @Override
    public void updateLogicalState(final float timePerFrame){
        super.updateLogicalState(timePerFrame);
       // rotate(0, 0, 0.003f);
    }


}
