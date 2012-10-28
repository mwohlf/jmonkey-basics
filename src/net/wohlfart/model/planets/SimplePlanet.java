package net.wohlfart.model.planets;


import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;


public class SimplePlanet extends AbstractPlanet {
	private final Geometry planetGeometry;
	private final float rotSpeed = 0.0012f;
	

	public SimplePlanet(final AssetManager assetManager) {
		planetGeometry = new Geometry("Planet", new Sphere(32,32, 2f));
		// Lighting.j3md supports: DiffuseMap, NormalMap, SpecularMap, and ParallaxMap. 
	    Material material = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");	    
	    
	    material.setTexture("DiffuseMap", assetManager.loadTexture("Textures/Terrain/Pond/Pond.jpg"));
	    material.setTexture("NormalMap", assetManager.loadTexture("Textures/Terrain/Pond/Pond_normal.png"));	    
	    
	    material.setBoolean("UseMaterialColors",true);    
	    material.setColor("Specular",ColorRGBA.White);
	    material.setColor("Diffuse",ColorRGBA.White);
	    material.setColor("Ambient",  ColorRGBA.White);
	    material.setFloat("Shininess", 5f); // [1,128]    
	    planetGeometry.setMaterial(material);
	    attachChild(planetGeometry);
	}
	
    @Override
    public void updateLogicalState(float tpf){
        super.updateLogicalState(tpf);
        planetGeometry.rotate(rotSpeed, 0, 0);
    }

}
