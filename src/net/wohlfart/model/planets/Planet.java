package net.wohlfart.model.planets;

import java.util.Random;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;

public class Planet extends AbstractPlanet {
    // number of sample / level of detail 3 means we have a pyramid...
    private static final int SAMPLES = 40;
    // neutral texture
    private static final String TEXTURE_PATH = "Common/MatDefs/Light/Lighting.j3md";

    private final long seed;
    private final Random random;

    private final PlanetType planetType;

    private final Geometry planetGeometry; // z seems to be the up axis for geometries
    private final float rotSpeed; // [rad/s]
    private final Vector3f rotAxis;
    private final float radius; // in [10^6 m]

    public Planet(final AssetManager assetManager, final long seed) {
        this.seed = seed;
        this.random = new Random(seed);

        setName("Planet[" + seed + "]");

        // random planet type
        int index = random.nextInt(PlanetType.values().length);
        planetType = PlanetType.values()[index];

        radius = getRandom(planetType.minRadius, planetType.maxRadius);

        rotSpeed = getRandom(planetType.minRot, planetType.maxRot);

        float f = planetType.maxAxisDeplacement;
        // rotAxis = new Vector3f(0,0,1);
        rotAxis = new Vector3f(getRandom(-f, f), getRandom(-f, f), 1);

        // Lighting.j3md supports: DiffuseMap, NormalMap, SpecularMap, and ParallaxMap.
        Material material = new Material(assetManager, TEXTURE_PATH);
        material.setTexture("DiffuseMap", new PlanetTexture(radius, planetType, seed));

        Sphere sphere = new Sphere(SAMPLES, SAMPLES, radius);
        sphere.setTextureMode(Sphere.TextureMode.Projected);

        planetGeometry = new Geometry("PlanetGeometry[" + seed + "]", sphere);
        planetGeometry.rotateUpTo(rotAxis);
        planetGeometry.setMaterial(material);
        setGeometry(planetGeometry);
    }

    private float getRandom(float min, float max) {
        return ((1f - random.nextFloat()) * (max - min)) + min;
    }

    @Override
    public float getRadius() {
        return radius;
    }

    @Override
    public float getRotSpeed() {
        return rotSpeed;
    }

    @Override
    public PlanetType getType() {
        return planetType;
    }

    /**
     * @param timePerFrame
     *            : [s/frame]
     */
    @Override
    public void updateLogicalState(final float timePerFrame) {
        super.updateLogicalState(timePerFrame);
        planetGeometry.rotate(0, 0, timePerFrame * rotSpeed); // rotation around the up axis
    }

    public String toString() {
        return this.getClass().getSimpleName() + " seed: " + seed + " radius: " + radius + " rotSpeed: " + rotSpeed + " location: " + getWorldTranslation();
    }

}
