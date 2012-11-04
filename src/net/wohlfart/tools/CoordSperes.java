package net.wohlfart.tools;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.debug.WireSphere;

/**
 * a simple coord system at the origin
 * 
 * 
 * @author michael
 */
public class CoordSperes extends Node {

    public CoordSperes(final AssetManager assetManager) {
        attachSpere(assetManager, 10, ColorRGBA.Red);
        attachSpere(assetManager, 20, ColorRGBA.Green);
        attachSpere(assetManager, 30, ColorRGBA.Blue);
    }

    private void attachSpere(final AssetManager assetManager, int radius, final ColorRGBA color) {
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.setColor("Color", color);
        Geometry g = new Geometry("axis", new WireSphere(radius) {
            {
                setLineWidth(4);
            }
        });
        g.setMaterial(mat);
        g.setLocalTranslation(Vector3f.ZERO);
        attachChild(g);
    }
}
