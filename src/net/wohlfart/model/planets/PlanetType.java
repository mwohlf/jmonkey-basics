package net.wohlfart.model.planets;

import java.awt.Color;

import com.jme3.math.FastMath;


/**
 * different strategies for building planets
 */
public enum PlanetType {

	WATER {
		/**
		 * @param x [0..1]
		 * @param y [0..1]
		 * @param z [0..1]
		 * @param texture, needed for access to some randomness
		 * @return the color on the surface at the location with the specified normal vector
		 */
		Color getColor(final float x, final float y, final float z, final PlanetTexture texture) {
			final float persistence = 0.5f;
			final int octaves = 5;

			// some initial corrections
			float xx = x;
			float yy = FastMath.asin(y);
			float zz = z;

			double result = 0;
			float max = 0;
			for (int i=0; i < octaves; i++) {
				float frequency = FastMath.pow(2, i);
				float amplitude = FastMath.pow(persistence, i);
				result += perlinNoise(xx, yy, zz, amplitude, frequency);
				max += amplitude;
			}
			// create a color
			return linearGradient(Color.BLUE, Color.WHITE, (float)result / max);
		}
	},
	
	FIRE {
		Color getColor(final float x, final float y, final float z, final PlanetTexture texture) {
			final float persistence = 0.5f;
			final int octaves = 5;

			// some initial corrections
			float xx = x;
			float yy = FastMath.asin(y);
			float zz = z;

			double result = 0;
			float max = 0;
			for (int i=0; i < octaves; i++) {
				float frequency = FastMath.pow(2, i);
				float amplitude = FastMath.pow(persistence, i);
				result += perlinNoise(xx, yy, zz, amplitude, frequency);
				max += amplitude;
			}
			// create a color
			return linearGradient(Color.RED.brighter(), Color.YELLOW, (float)result / max);
		}
	},
	
	GAS {
		{maxRadius = 70; minRadius = 50;}
		Color getColor(final float x, final float y, final float z, final PlanetTexture texture) {
			final float persistence = 0.5f;
			final int octaves = 5;

			// some initial corrections
			float xx = x/1.5f;
			float yy = FastMath.asin(y) * 10;
			float zz = z/1.5f;

			double result = 0;
			float max = 0;
			for (int i=0; i < octaves; i++) {
				float frequency = FastMath.pow(2, i);
				float amplitude = FastMath.pow(persistence, i);
				result += perlinNoise(xx, yy, zz, amplitude, frequency);
				max += amplitude;
			}
			// create a color
			return linearGradient(new Color(255,213,133), new Color(102,68,58), (float)result / max);
		}
	},
	
	CONTINENTAL {
		{maxRadius = 15;}
		Color getColor(final float x, final float y, final float z, final PlanetTexture texture) {
			float persistence;
			int octaves;
			double result;
			float max;

			// some initial corrections
			float xx = x;
			float yy = FastMath.asin(y);
			float zz = z;
			
			
			//// first run for the ground:
			persistence = 0.1f;
			octaves = 5;

			result = 0; max = 0;
			for (int i=0; i < octaves; i++) {
				float frequency = FastMath.pow(2, i);
				float amplitude = FastMath.pow(persistence, i);
				result += perlinNoise(xx, yy, zz, amplitude, frequency);
				max += amplitude;
			}
			Color ground = cosGradient(Color.BLUE.darker(), Color.GREEN.darker().darker(), (float)(result / max));		
			
			
			// another run for the clouds:
			persistence = 0.5f;
			octaves = 5;

			result = 0; max = 0;
			for (int i=0; i < octaves; i++) {
				float frequency = FastMath.pow(2, i);
				float amplitude = FastMath.pow(persistence, i);
				result += perlinNoise(xx, yy* 2, zz, amplitude, frequency);
				max += amplitude;
			}
			
			return cosGradient(ground, Color.WHITE, (float)result / max);		
		}
	};


	
	float minRadius = 10;  // in 10^6 m = 1000 km (earth has 12 000 km)
	float maxRadius = 100;
	
	float minRot = FastMath.TWO_PI / 60f;     // in rad/s, 2pi mean one rotation per second 2pi/3 means one rotation in 3 sec
	float maxRot = FastMath.TWO_PI / 300f;
	
	



	// fallback
	Color getColor(final float x, final float y, final float z, final PlanetTexture texture) {
		return Color.YELLOW;
	}


	// ---- tool methods

	double perlinNoise(final float x, final float y, final float z, final float amplitude, final float frequency) {
		// the noise returns [-1 .. +1]
		//double noise = PerlinNoise.noise(x * frequency, y * frequency, z * frequency);
		double noise = SimplexNoise.noise(x * frequency, y * frequency, z * frequency);
		// normalize into [0 .. 1] and multiply with the amplitude
		return amplitude * ((noise + 1d) / 2d);   // [0 .. 1]
	}

	
	Color overlay(final Color ground, final Color air, final Color transparent) {
		return ground;
	}
	
	
	Color cosGradient(final Color top, final Color low, final float v) {		
		// v [0 .. 1]
		float value = v;
		value = (1-(FastMath.cos(value*FastMath.PI) + 1) / 2);
		value = (1-(FastMath.cos(value*FastMath.PI) + 1) / 2);
		value = (1-(FastMath.cos(value*FastMath.PI) + 1) / 2);
	
		float red = (   ((float)top.getRed() * value) 
				+ ((float)low.getRed() * (1f-value))) / 256f;
		float green = (   ((float)top.getGreen() * value) 
				+ ((float)low.getGreen() * (1f-value))) / 256f;
		float blue = (   ((float)top.getBlue() * value) 
				+ ((float)low.getBlue() * (1f-value))) / 256f;
		return new Color(red, green, blue);
	}

	
	Color linearGradient(final Color top, final Color low, final float value) {		
		float red = (   ((float)top.getRed() * value) 
				+ ((float)low.getRed() * (1f-value))) / 256f;
		float green = (   ((float)top.getGreen() * value) 
				+ ((float)low.getGreen() * (1f-value))) / 256f;
		float blue = (   ((float)top.getBlue() * value) 
				+ ((float)low.getBlue() * (1f-value))) / 256f;
		return new Color(red, green, blue);
	}

}
