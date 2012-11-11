package net.wohlfart.model.planets;

import static net.wohlfart.model.planets.Assert.assertEqualVector;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jme3.math.Vector3f;
import com.jme3.texture.Image;

public class PlanetTextureTest {

    CelestialTexture planetTexture;
    int height;
    int width;

    @Before
    public void runBeforeEveryTest() {
        // use a big enough radius so we don't have rounding errors...
        planetTexture = new CelestialTexture(200f, CelestialType.values()[0], 1l); // radius and random planet type
        Image image = planetTexture.getImage();
        height = image.getWidth();
        width = image.getHeight();
    }

    @After
    public void runAfterEveryTest() {
        planetTexture = null;
    }

    @Test
    public void size() {
        Image image = planetTexture.getImage();
        assertEquals(1257, image.getHeight());
        assertEquals(1257, image.getWidth());
    }

    @Test
    public void poleVectors() {
        Vector3f vec;

        vec = planetTexture.getNormalVector(0, 0);
        assertEqualVector(new Vector3f(0, +1, 0), vec);

        vec = planetTexture.getNormalVector(width - 1, 0);
        assertEqualVector(new Vector3f(0, +1, 0), vec);

        vec = planetTexture.getNormalVector((width - 1) / 2, 0);
        assertEqualVector(new Vector3f(0, +1, 0), vec);

        vec = planetTexture.getNormalVector(0, height - 1);
        assertEqualVector(new Vector3f(0, -1, 0), vec);

        vec = planetTexture.getNormalVector(width - 1, height - 1);
        assertEqualVector(new Vector3f(0, -1, 0), vec);

        vec = planetTexture.getNormalVector((width - 1) / 2, height - 1);
        assertEqualVector(new Vector3f(0, -1, 0), vec);
    }

    @Test
    public void equatorVectors() {
        Vector3f vec;

        vec = planetTexture.getNormalVector(0, (height - 1) / 2);
        assertEqualVector(new Vector3f(0, 0, +1), vec);

        vec = planetTexture.getNormalVector((width - 1) / 4, (height - 1) / 2);
        assertEqualVector(new Vector3f(+1, 0, 0), vec);

        vec = planetTexture.getNormalVector((width - 1) / 2, (height - 1) / 2);
        assertEqualVector(new Vector3f(0, 0, -1), vec);

        vec = planetTexture.getNormalVector((int) (3f * ((float) width - 1) / 4f), (height - 1) / 2);
        assertEqualVector(new Vector3f(-1, 0, 0), vec);
    }

}
