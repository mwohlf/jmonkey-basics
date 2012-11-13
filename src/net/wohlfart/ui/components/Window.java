package net.wohlfart.ui.components;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;

public class Window extends Node {

    private BitmapText titleText;
    private Quad quad;


    Window(final ILookAndFeel lookAndFeel, final AssetManager assetManager) {
        super();
        setQueueBucket(Bucket.Gui);
        setCullHint(CullHint.Never);

        BitmapFont guiFont = assetManager.loadFont("Interface/Fonts/Console.fnt");

        titleText = new BitmapText(guiFont, false);
        titleText.setSize(guiFont.getCharSet().getRenderedSize());
        titleText.setColor(ColorRGBA.Blue);
        titleText.setLocalTranslation(10, titleText.getLineHeight() * 10, 0);
        titleText.setText("window here");
        attachChild(titleText);

        quad = new Quad(100,100);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTransparent(true);
        mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        mat.setColor("Color", lookAndFeel.getColorRGBA(ILookAndFeel.WINDOW_BACKGROUND));
        Geometry geo = new Geometry("OurMesh", quad);
        geo.setMaterial(mat);
        attachChild(geo);

        setLocation(50, 50);
        setSize(270,270);
    }


    public void setTitle(String text) {
        titleText.setText(text);
    }

    public void setLocation(final int x, final int y) {
        setLocalTranslation(x,y,0);
    }

    public void setSize(final int width, final int height) {
        quad.updateGeometry(width, height);
    }


}
