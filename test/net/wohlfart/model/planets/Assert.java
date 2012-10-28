package net.wohlfart.model.planets;

import static org.junit.Assert.assertEquals;

import com.jme3.math.Vector3f;

public class Assert {

	// compare two vectors
	public static void assertEqualVector(Vector3f expected, Vector3f actual) {
		assertEquals(expected.x, actual.x, 0.01); // 2% tollerance is too much ?
		assertEquals(expected.y, actual.y, 0.01);
		assertEquals(expected.z, actual.z, 0.01);
	}

}
