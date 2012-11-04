package net.wohlfart.model.planets;

import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

public abstract class AbstractPlanet extends Node implements IPlanet {

    // the key used in the geometry to store the planet as userdata
    public static final String PLANET_KEY = "PLANET_KEY";

    protected void setGeometry(final Geometry geometry) {
        super.attachChild(geometry);
        geometry.setUserData(PLANET_KEY, this);
    }

}
