package net.wohlfart.model.noise;

import java.awt.Color;
import java.util.Set;
import java.util.TreeSet;

/**
 * converting noise to color
 * 
 * @author michael
 */
public class ColorGradient {
	private static final double LEFT  = -1d;
	private static final double RIGHT = +1d;


	private final Set<GradientPoint> gradientPoints = new TreeSet<GradientPoint>();


	public ColorGradient(Color... colors) {
		if (colors.length == 1) {
			gradientPoints.add(new GradientPoint(LEFT, colors[0]));
			gradientPoints.add(new GradientPoint(RIGHT, colors[0]));
		} else {
			for (int i = 0; i < colors.length; i++) {
				double delta = ((RIGHT - LEFT) * i / (colors.length-1d));
				gradientPoints.add(new GradientPoint(LEFT + delta, colors[i]));
			}
		}
	}

	public ColorGradient(GradientPoint... points) {
		for (GradientPoint gradientPoint : points) {
			gradientPoints.add(gradientPoint);
		}
	}
	
	// for testing
	Set<GradientPoint> getGradientPoints() {
		return gradientPoints;
	}


	/**
	 * 
	 * @param value [-1 .. +1]
	 * @return
	 */
	public Color getColor(final double value) {

		int size = gradientPoints.size();
		GradientPoint pointArray[] = new GradientPoint[size];
		gradientPoints.toArray(pointArray);

		GradientPoint left = pointArray[0];
		for (int i = 0; i < size; i++) {
			GradientPoint next = pointArray[i];
			if (next.point > value) {
				break; // we need to stay below the next value
			}
			left = next;
		}

		GradientPoint right = pointArray[size-1];
		for (int i = size-1; i >= 0; i--) {
			GradientPoint next = pointArray[i];
			if (next.point < value) {
				break; // we need to stay below the next value
			}
			right = next;
		}
		
		if (left == right) {
			return right.color;
		}


		// now calculate gradient between left and right
		double delta = 1d;
		double distanceLeft = 0.5d;
		double distanceRight = 0.5d;
		if (right.point > left.point) {
			delta = right.point - left.point;
			distanceLeft = (value - left.point) / delta;
			distanceRight = (right.point - value) / delta;
		}

		//return calculateHSVColor(left, right, distanceLeft, distanceRight);
		return calculateRGBColor(left, right, distanceLeft, distanceRight);
	}

	
	private Color calculateHSVColor(final GradientPoint left, final GradientPoint right, final double distanceLeft, final double distanceRight) {
			
		HSLColor left2 = new HSLColor(left.color);
		HSLColor right2 = new HSLColor(right.color);

		
		float hue   =   ((((float)left2.getHue()   * (float)distanceLeft) 
				+ ((float)right2.getHue()  * (float)distanceRight))) / 2f;
		
		float saturation =    ((((float)left2.getSaturation()  * (float)distanceLeft) 
				+ ((float)right2.getSaturation() * (float)distanceRight))) / 2f;

		float luminance =   ((((float)left2.getLuminance() * (float)distanceLeft) 
				+ ((float)right2.getLuminance() * (float)distanceRight))) / 2f;
		
		return  new HSLColor(hue, saturation, luminance).getRGB();
	}

	
	
	private Color calculateRGBColor(final GradientPoint left2, final GradientPoint right2, final double distanceLeft, final double distanceRight) {

		float red   =   ((((float)left2.color.getRed()   * (float)distanceRight) 
				+ ((float)right2.color.getRed()  * (float)distanceLeft))) / 256f;
		
		float green =   ((((float)left2.color.getGreen() * (float)distanceRight) 
				+ ((float)right2.color.getGreen()* (float)distanceLeft))) / 256f;
		
		float blue =    ((((float)left2.color.getBlue()  * (float)distanceRight) 
				+ ((float)right2.color.getBlue() * (float)distanceLeft))) / 256f;
		
		return new Color(red, green, blue);
	}
	
	

	public static class GradientPoint implements Comparable<GradientPoint> {		
		final double point;
		final Color color;

		public GradientPoint(final double point, final Color color) {
			this.point = point;
			this.color = color;
		}

		@Override
		public int compareTo(final GradientPoint that) {			
			return Double.compare(this.point, that.point);
		}
	}

}
