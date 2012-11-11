package net.wohlfart.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;




/**
 *
 *
 */
public class StellarNode extends Node {
    // node scaling, this affects all planet radius and movements
    public static final float SCALE = 1000000.0f; // 1 unit is 1000km = x10^3 km = x10^6km
    //
    public static final float SCALE_INV = 1.f/SCALE;
    // at least 100 km distance from any planet we can't get closer
    private static final float MIN_DIST_FROM_STELLAR_OBJECT = 0.1f; // ~100km ; [10^3 km]

    public StellarNode(final AssetManager assetManager) {
        setQueueBucket(Bucket.Sky);
    }

    @Override
    public int attachChild(final Spatial child) {
        return super.attachChild(child);
    }

    /**
     * sort for correct rendering in the Bucket.Sky queue
     */
    void sortChildren() {
        final ArrayList<Spatial> list = new ArrayList<Spatial>();
        list.addAll(children);
        children.clear();

        Collections.sort(list, new Comparator<Spatial>() {
            @Override
            public int compare(final Spatial spatial1, final Spatial spatial2) {
                return -Float.compare(spatial1.getWorldTranslation().length(), spatial2.getWorldTranslation().length());
            }
        });

        for (Spatial spatial : list) {
            children.add(spatial);
        }
    }


    // scale down the moves

    @Override
    public Spatial move(final Vector3f move) {
        return this.move(move.x , move.y, move.z);
    }

    @Override
    public Spatial move(final float x, final float y, final float z) {
        // TODO: make sure we are not too close, check with MIN_DIST_FROM_STELLAR_OBJECT
        super.move(x /SCALE, y /SCALE, y /SCALE);
        return this;
    }

}
