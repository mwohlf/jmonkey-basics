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

    private BitmapText perfText;

    private Quad quad;


    Window(final ILookAndFeel lookAndFeel, final AssetManager assetManager) {
        super();
        setQueueBucket(Bucket.Gui);
        setCullHint(CullHint.Never);

        assetManager.loadFont("Interface/Fonts/Default.fnt");
        BitmapFont guiFont = assetManager.loadFont("Interface/Fonts/Console.fnt");

        BitmapText perfText = new BitmapText(guiFont, false);
        perfText.setSize(guiFont.getCharSet().getRenderedSize());
        perfText.setColor(ColorRGBA.Blue);
        perfText.setLocalTranslation(10, perfText.getLineHeight() * 10, 0);
        perfText.setText("window here");
        attachChild(perfText);

        quad = new Quad(100,200);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTransparent(true);
        mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        mat.setColor("Color", lookAndFeel.getColorRGBA(ILookAndFeel.WINDOW_BACKGROUND));
        Geometry geo = new Geometry("OurMesh", quad);
        geo.setMaterial(mat);
        attachChild(geo);

        setLocalTranslation(50, 50, 0);
    }


    public void setText(String text) {
        perfText.setText(text);
    }


}
