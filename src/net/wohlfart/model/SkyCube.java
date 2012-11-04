package net.wohlfart.model;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingSphere;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.TextureCubeMap;

public class SkyCube extends Geometry {

    public SkyCube(final AssetManager assetManager) {
        super();

        Sphere skyMesh = new Sphere(100, 100, 100, true, false);
        setMesh(skyMesh);

        Material material = new Material(assetManager, "Common/MatDefs/Misc/Sky.j3md");
        material.setVector3("NormalScale", Vector3f.UNIT_XYZ);
        TextureCubeMap texture = new TextureCubeMap();
        texture.setImage(assetManager.loadTexture("textures/starCube.dds").getImage());
        // texture.setImage(assetManager.loadTexture("textures/skyCube.dds").getImage());
        material.setTexture("Texture", texture);
        setMaterial(material);

        setQueueBucket(Bucket.Sky);
        setCullHint(Spatial.CullHint.Never);
        setModelBound(new BoundingSphere(Float.POSITIVE_INFINITY, Vector3f.UNIT_XYZ));
        // setModelBound(new BoundingSphere(Float.POSITIVE_INFINITY, Vector3f.ZERO));
    }

}
