package net.wohlfart.tools;

import static com.jme3.math.Vector3f.UNIT_X;
import static com.jme3.math.Vector3f.UNIT_Y;
import static com.jme3.math.Vector3f.UNIT_Z;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.debug.Arrow;

/**
 * a simple coord system at the origin use like this: rootNode.attachChild(new CoordAxis(assetManager));
 * 
 * @author michael
 */
public class CoordAxis extends Node {

    public CoordAxis(final AssetManager assetManager) {
        attachArrow(assetManager, UNIT_X, ColorRGBA.Red);
        attachArrow(assetManager, UNIT_Y, ColorRGBA.Green);
        attachArrow(assetManager, UNIT_Z, ColorRGBA.Blue);
    }

    private void attachArrow(final AssetManager assetManager, final Vector3f vector3f, final ColorRGBA color) {
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.setColor("Color", color);
        Geometry g = new Geometry("axis", new Arrow(vector3f) {
            {
                setLineWidth(4);
            }
        });
        g.setMaterial(mat);
        g.setLocalTranslation(Vector3f.ZERO);
        attachChild(g);
    }
}
