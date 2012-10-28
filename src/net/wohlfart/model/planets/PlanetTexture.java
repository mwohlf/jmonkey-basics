package net.wohlfart.model.planets;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.util.Random;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.texture.Image;
import com.jme3.texture.Image.Format;
import com.jme3.texture.Texture2D;
import com.jme3.util.BufferUtils;

public class PlanetTexture extends Texture2D {
	
	final Random random;
	
	public PlanetTexture(final float radius, final PlanetType planetType, final long seed) {
		this((int)(radius * 2f * FastMath.PI + 0.5f), (int)(radius * 2f * FastMath.PI + 0.5f), planetType, seed);
	}

	
	// single constructor entry point
	protected PlanetTexture(final int width, final int height, final PlanetType planetType, final long seed) {
		super(new Image(Format.RGBA8, width, height, 
				ByteBuffer.allocateDirect(width * height * 4)));
		random = new Random(seed);
		createTexture(planetType);
	}

	
	/**
	 * the whole texture is mapped onto the sphere
	 */
	void createTexture(final PlanetType planetType) {

		int height = getImage().getHeight();
		int width = getImage().getWidth();
		byte[] data = new byte[width * height * 4];
		getImage().getData(0).get(data);
				
		Vector3f vector;
		Color color;
		for (int x = 0 ; x < width; x++) {
			for (int y = 0 ; y < height; y++) {
				vector = getNormalVector(x,y);
				vector.mult(255f);
				color = planetType.getColor(vector.x, vector.y, vector.z, this);
				setPixel(x,y, color, width, height, data);
			}
		}	
		getImage().setData(BufferUtils.createByteBuffer(data));
	}

	
	/**
	 * 0/0 is top left, the whole texture is wrapped around the planet
	 * @return a vector with each element [0..1]
	 */
	Vector3f getNormalVector(final int x, final int y) {
		int yRange = getImage().getHeight() - 1;
		int xRange = getImage().getWidth() - 1;
		float latitude = (FastMath.PI * ((float)y/(float)yRange)); // [0 .. PI]  (north-south)
		float longitude = (FastMath.TWO_PI * ((float)x/(float)xRange));  // [0 .. TWO_PI]		
		float xx = FastMath.sin(longitude) * FastMath.sin(latitude);   // 0 -> 0; 1/2pi -> 1 ; pi -> 0
		float yy = FastMath.cos(latitude);    // 0 -> 1; 1/2pi -> 0 ; pi -> -1
		float zz = FastMath.cos(longitude) * FastMath.sin(latitude);   // 0 -> 1;...
		return new Vector3f(xx,yy,zz);
	}

	Random getRandom() {
		return random;
	}
	
	private void setPixel(int x, int y, Color color, 
			int width, int height, byte[] data) {
		y = height-y-1;
		if (x < 0) {x = 0;}
		if (y < 0) {y = 0;}
		if (x > width-1) {x = width-1;}
		if (y > height-1) {y = height-1;}
		int i = (x + y * width) * 4;
		data[i + 0] = (byte) color.getRed();   // r
		data[i + 1] = (byte) color.getGreen(); // g
		data[i + 2] = (byte) color.getBlue();  // b
		data[i + 3] = (byte) color.getAlpha(); // a
	}

}