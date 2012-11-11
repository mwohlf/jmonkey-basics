package net.wohlfart.model;

import java.util.Iterator;
import java.util.Random;

import net.wohlfart.events.DoSelection;
import net.wohlfart.model.planets.AbstractCelestial;
import net.wohlfart.model.planets.ICelestial;
import net.wohlfart.model.planets.CelestialImpl;

import org.bushe.swing.event.EventService;
import org.bushe.swing.event.generics.TypeReference;

import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.light.Light;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
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

    private final EventService eventBus;

    public StellarSystem(final AssetManager assetManager, final EventService eventBus) {
        delegatee = new Node(ROOT_NODE);
        this.eventBus = eventBus;

        // skyCube and stellarNode are in the Sky rendering bucket,
        // the order of adding them is important
        delegatee.attachChild(skyCube = new SkyCube(assetManager));
        delegatee.attachChild(stellarNode = new StellarNode(assetManager));
        // this contains local stuff like ships etc.
        delegatee.attachChild(localNode = new LocalNode(assetManager));

        skyCube.setName(SKY_CUBE);
        stellarNode.setName(STELLAR_NODE);
        localNode.setName(LOCAL_NODE);

        final Iterator<Light> iterator = new Lights().iterator();
        while (iterator.hasNext()) {
            delegatee.addLight(iterator.next());
        }

        // FIXME: this is ad-hoc init of a solar system remove in production...
        final int planetCount = 10;
        final Random random = new Random(1);
        for (int i = 1; i <= planetCount; i++) {
            final CelestialImpl planet = new CelestialImpl(assetManager, random.nextLong());
            stellarNode.attachChild(planet);
            final float rad = (FastMath.TWO_PI / (float) planetCount) * (float) i;
            planet.setLocalTranslation(FastMath.sin(rad) * 200, 0, FastMath.cos(rad) * 200);
        }
        // rootNode.attachChild(new CoordAxis(assetManager));
        // rootNode.attachChild(new CoordSperes(assetManager));
        // add(new Ship(assetManager));

        stellarNode.sortChildren();
    }

    public void add(final AbstractCelestial planet) {
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
        stellarNode.move(vector);
    }

    public Vector3f getWorldTranslation() {
        return stellarNode.getWorldTranslation();
    }

    public void actionClickRay(final Ray ray) {
        CollisionResults results = new CollisionResults();
        stellarNode.collideWith(ray, results);
        CollisionResult collisionResult = results.getClosestCollision();
        ICelestial planet = null;
        if (collisionResult != null) {
            Geometry geometry = collisionResult.getGeometry(); 
            planet = (ICelestial) geometry.getUserData(AbstractCelestial.PLANET_KEY);
        }
        TypeReference<DoSelection<ICelestial>> type = new TypeReference<DoSelection<ICelestial>>() {};
        DoSelection<ICelestial> event = new DoSelection<ICelestial>(planet);
        eventBus.publish(type.getType(), event);
    }

}
