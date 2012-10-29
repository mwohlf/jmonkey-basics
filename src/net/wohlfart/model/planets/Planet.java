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
	
	private final long seed;
	
	private final PlanetType planetType;
	
	private final Geometry planetGeometry;
	private final float rotSpeed;              // [rad/s]
	private final Vector3f rotAxis;
	private final float radius;                // in [10^6 m] 
	
	
	public Planet(final AssetManager assetManager, final long seed) {
		Random random = new Random(seed);
		this.seed = seed;
		
		setName("Planet["+seed+"]");
		
		// random planet type
		int index = (int) random.nextInt(PlanetType.values().length);
		planetType = PlanetType.values()[index];
		
		radius = (1f - random.nextFloat()) * (planetType.maxRadius - planetType.minRadius)
				+ planetType.minRadius;
		
		rotSpeed = (1f - random.nextFloat()) * (planetType.maxRot - planetType.minRot)
				+ planetType.minRot;
				
		rotAxis = new Vector3f(
				random.nextFloat(), random.nextFloat(), random.nextFloat()).normalizeLocal(); // TODO: parameterize
		
		// Lighting.j3md supports: DiffuseMap, NormalMap, SpecularMap, and ParallaxMap. 
	    Material material = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");	  
		material.setTexture("DiffuseMap", new PlanetTexture(radius, planetType, seed));		

		
		Sphere sphere = new Sphere(SAMPLES, SAMPLES, radius);
		sphere.setTextureMode(Sphere.TextureMode.Projected);
		
		planetGeometry = new Geometry("PlanetGeometry["+seed+"]", sphere);
		planetGeometry.rotateUpTo(rotAxis);
		planetGeometry.setMaterial(material);
	    setGeometry(planetGeometry);
	}
		


	public float getRadius() {
		return radius;
	}
	
	public float getRotSpeed() {
		return rotSpeed;
	}

	
	/**
	 * @param timePerFrame:  [s/frame]
	 */
    @Override
    public void updateLogicalState(final float timePerFrame){
        super.updateLogicalState(timePerFrame);
        planetGeometry.rotate(0, 0, timePerFrame * rotSpeed); // rotation around the up axis
    }

	
	public String toString() {
		return this.getClass().getSimpleName() 
				+ " seed: " + seed
				+ " radius: " + radius
				+ " rotSpeed: " + rotSpeed
				+ " location: " + getWorldTranslation();
	}

}
