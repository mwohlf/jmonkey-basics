package net.wohlfart.ui.components;

import net.wohlfart.ui.commands.DragAndDropCommand;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;


public class Window extends Node implements DragAndDropCommand.IDragAndDropable {

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

        setLocation(new Vector2f(50, 50));
        setSize(new Vector2f(270,270));
    }


    public void setTitle(String text) {
        titleText.setText(text);
    }

    @Override
    public void setLocation(final Vector2f v) {
        setLocalTranslation(v.x,v.y,0);
    }

    @Override
    public Vector2f getLocation() {
        Vector3f v = getLocalTranslation();
        return new Vector2f(v.x,v.y);
    }


    public void setSize(final Vector2f v) {
        quad.updateGeometry(v.x, v.y);
    }

    public Vector2f getSize() {
        return new Vector2f(quad.getWidth(), quad.getHeight());
    }


}
