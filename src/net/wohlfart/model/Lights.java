package net.wohlfart.model;

import java.util.Arrays;
import java.util.Iterator;

import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

public class Lights implements Iterable<Light> {

    Light[] lights = new Light[] { new AmbientLight() {
        {
            setColor(ColorRGBA.White.mult(0.6f));
        }
    }, new DirectionalLight() {
        {
            setDirection(new Vector3f(-1, 1, -2).normalizeLocal());
            setColor(ColorRGBA.White);
        }
    }, };

    @Override
    public Iterator<Light> iterator() {
        return Arrays.asList(lights).iterator();
    }

}
