package net.wohlfart.model.planets;

import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

public abstract class AbstractCelestial extends Node implements ICelestial {

    // the key used in the geometry to store the planet as userdata since we need a link 
    // from the geometry that is used for collision detection and this object
    public static final String PLANET_KEY = "PLANET_KEY";

    private float distance = 0; // distance to the cam

    protected void setGeometry(final Geometry geometry) {
        super.attachChild(geometry);
        geometry.setUserData(PLANET_KEY, this);
    }

    
    
    @Override
    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    
}
