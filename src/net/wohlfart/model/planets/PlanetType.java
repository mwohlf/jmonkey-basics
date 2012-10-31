package net.wohlfart.model.planets;

import java.awt.Color;

import net.wohlfart.model.noise.ColorGradient;
import net.wohlfart.model.noise.SimplexNoise;

import com.jme3.math.FastMath;


/**
 * different strategies for building planets
 */
public enum PlanetType {

	FIRE {
		{maxRadius = 40; minRadius = 60;}
		ColorGradient gradient = new ColorGradient(Color.RED.brighter(), Color.YELLOW);
		Color getColor(final float x, final float y, final float z, final PlanetTexture texture) {
			double noise = createNoise(x, FastMath.asin(y), z, 0.5f, 5);
			return gradient.getColor(noise);
		}
	},
	
	
	WATER {
		{maxRadius = 20; minRadius = 30;}
		ColorGradient gradient = new ColorGradient(Color.BLUE, Color.WHITE);
		Color getColor(final float x, final float y, final float z, final PlanetTexture texture) {
			double noise = createNoise(x, FastMath.asin(y), z, 0.5f, 5);
			return gradient.getColor(noise);
		}
	},
	
	
	GAS {
		{maxRadius = 70; minRadius = 50;}
		ColorGradient gradient = new ColorGradient(new Color(255,213,133), new Color(102,68,58));
		Color getColor(final float x, final float y, final float z, final PlanetTexture texture) {
			double noise = createNoise(x/1.5f, FastMath.asin(y) * 10, z/1.5f, 0.5f, 5);
			return gradient.getColor(noise);
		}
	},
	
	
	CONTINENTAL {
		{maxRadius = 30; minRadius = 60;}
		ColorGradient gradient = new ColorGradient( 
				new Color(0,0,0), 
				new Color(0,0,100), 
				new Color(0,0,255), 
				new Color(10,10,255), 
				new Color(180,180,180), 
				new Color(10,255,10), 
				new Color(0,255,0), 
				new Color(0,100,0));
		Color getColor(final float x, final float y, final float z, final PlanetTexture texture) {
			double groundNoise = createNoise(x, FastMath.asin(y), z, 0.5f, 4);
			Color ground = gradient.getColor(groundNoise);
			double skyNoise = createNoise(x*2, FastMath.asin(y)*4, z*2, 0.2f, 3);			
			return ColorGradient.linearGradient(ground, Color.WHITE, skyNoise);
		}
	};
	
//	ROCK {
//		{maxRadius = 15;}
//		Color getColor(final float x, final float y, final float z, final PlanetTexture texture) {
//			float persistence;
//			int octaves;
//			double result;
//			float max;
//
//			// some initial corrections
//			float xx = x;
//			float yy = FastMath.asin(y);
//			float zz = z;
//			
//			
//			//// first run for the ground:
//			persistence = 0.1f;
//			octaves = 5;
//
//			result = 0; max = 0;
//			for (int i=0; i < octaves; i++) {
//				float frequency = FastMath.pow(2, i);
//				float amplitude = FastMath.pow(persistence, i);
//				result += perlinNoise(xx, yy, zz, amplitude, frequency);
//				max += amplitude;
//			}
//			Color ground = cosGradient(Color.BLUE.darker(), Color.GREEN.darker().darker(), (float)(result / max));		
//			
//			
//			// another run for the clouds:
//			persistence = 0.5f;
//			octaves = 5;
//
//			result = 0; max = 0;
//			for (int i=0; i < octaves; i++) {
//				float frequency = FastMath.pow(2, i);
//				float amplitude = FastMath.pow(persistence, i);
//				result += perlinNoise(xx, yy* 2, zz, amplitude, frequency);
//				max += amplitude;
//			}
//			
//			return cosGradient(ground, Color.WHITE, (float)result / max);		
//		}
//	};


	
	float minRadius = 10;  // in 10^6 m = 1000 km (earth has 12 000 km)
	float maxRadius = 100;
	
	float minRot = FastMath.TWO_PI / 60f;     // in rad/s, 2pi mean one rotation per second 2pi/3 means one rotation in 3 sec
	float maxRot = FastMath.TWO_PI / 300f;
	
	



	/**
	 * fallback
	 * 
	 * @param x [0..1]
	 * @param y [0..1]
	 * @param z [0..1]
	 * @param texture, needed for access to some randomness
	 * @return the color on the surface at the location with the specified normal vector
	 */
	Color getColor(final float x, final float y, final float z, final PlanetTexture texture) {
		return Color.YELLOW;
	}


	
	double createNoise(final float x, final float y, final float z, final float persistence, final int octaves) {
		double result = 0;
		float max = 0;
		for (int i=0; i < octaves; i++) {
			float frequency = FastMath.pow(2, i);
			float amplitude = FastMath.pow(persistence, i);
			result += createNoise(x, y, z, amplitude, frequency);
			max += amplitude;
		}
		return result/max;
	}
	
	
	double createNoise(final float x, final float y, final float z, final float amplitude, final float frequency) {
		// the noise returns [-1 .. +1]
		//double noise = PerlinNoise.noise(x * frequency, y * frequency, z * frequency);
		double noise = SimplexNoise.noise(x * frequency, y * frequency, z * frequency);
		return amplitude * noise;
	}

	
}
