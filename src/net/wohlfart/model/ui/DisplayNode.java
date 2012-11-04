package net.wohlfart.model.ui;


import net.wohlfart.events.Channels;
import net.wohlfart.user.IAvatar;

import org.bushe.swing.event.EventService;
import org.bushe.swing.event.EventTopicSubscriber;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial.CullHint;

/**
 *
 * dialogs and hud for the main game screen
 */
public class DisplayNode {
    public static final String DISPLAY_NODE = "DISPLAY_NODE";

    protected final EventService eventBus;

    private Node delegatee = new Node();

    private final BitmapFont guiFont;

    private final BitmapText perfText;
    private final BitmapText viewText;
    private final BitmapText infoText;

    private final IAvatar avatar;

    public DisplayNode(final AssetManager assetManager, final IAvatar avatar, final EventService eventBus) {
        delegatee = new Node(DISPLAY_NODE);
        this.avatar = avatar;
        this.eventBus = eventBus;

        delegatee.setQueueBucket(Bucket.Gui);
        delegatee.setCullHint(CullHint.Never);

        assetManager.loadFont("Interface/Fonts/Default.fnt");
        guiFont = assetManager.loadFont("Interface/Fonts/Console.fnt");

        perfText = new BitmapText(guiFont, false);
        perfText.setSize(guiFont.getCharSet().getRenderedSize());
        perfText.setColor(ColorRGBA.Blue);
        perfText.setLocalTranslation(10, perfText.getLineHeight() * 10, 0);
        perfText.setText("");
        delegatee.attachChild(perfText);

        viewText = new BitmapText(guiFont, false);
        viewText.setSize(guiFont.getCharSet().getRenderedSize());
        viewText.setColor(ColorRGBA.Green);
        viewText.setLocalTranslation(10, 200 + viewText.getLineHeight() * 10, 0);
        viewText.setText("");
        delegatee.attachChild(viewText);

        infoText = new BitmapText(guiFont, false);
        infoText.setSize(guiFont.getCharSet().getRenderedSize());
        infoText.setColor(ColorRGBA.Red);
        infoText.setLocalTranslation(10, 300 + infoText.getLineHeight() * 10, 0);
        infoText.setText("info text here");
        delegatee.attachChild(infoText);

        eventBus.subscribeStrongly(Channels.TEXTMESSAGES.name(), new EventTopicSubscriber<String>() {
            @Override
            public void onEvent(String string, String message) {
                infoText.setText("\n----> " + message);
            }
        });
    }

    public Node getNode() {
        return delegatee;
    }

    public void updateLogicalState(final float timePerFrame) {
        delegatee.updateLogicalState(timePerFrame);

        perfText.setText("" + "seconds per frame: " + timePerFrame + "\n" + "frames per second: " + 1f / timePerFrame + "\n");

        viewText.setText("" + "direction: " + avatar.getDirection() + "\n" + "up: " + avatar.getDirection() + "\n" + "left: " + avatar.getLeft() + "\n");
    }

    public void updateGeometricState() {
        delegatee.updateGeometricState();
    }
}
