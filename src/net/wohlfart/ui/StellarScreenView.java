package net.wohlfart.ui;

import java.util.List;
import java.util.logging.Logger;

import net.wohlfart.IStateContext;
import net.wohlfart.model.planets.ICelestial;
import net.wohlfart.ui.commands.DragAndDropCommand;
import net.wohlfart.ui.commands.ICommand;
import net.wohlfart.ui.commands.NullCommand;
import net.wohlfart.ui.commands.ResizeCommand;
import net.wohlfart.ui.commands.ResizeCommand.IResizable;
import net.wohlfart.ui.components.ComponentFactory;
import net.wohlfart.ui.components.Window;
import net.wohlfart.user.IAvatar;
import net.wohlfart.user.IMouseAware;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.MouseInput;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial.CullHint;

/*
 * view for a screen
 */
public class StellarScreenView implements IMouseAware, IScreenView {
    private static final Logger LOGGER = Logger.getLogger(StellarScreenView.class.getName());
    private static final String SCREEN_ID = "stellarScreen";

    protected final Node delegatee;
    protected final AssetManager assetManager;
    protected final ViewPort guiViewPort;
    protected final ComponentFactory componentFactory;


    public StellarScreenView(final IStateContext context) {
        delegatee = new Node(SCREEN_ID);
        //delegatee.rotateUpTo(new Vector3f(0, -1, 0));
        delegatee.setQueueBucket(Bucket.Gui);
        delegatee.setCullHint(CullHint.Never);
        this.assetManager = context.getAssetManager();
        this.guiViewPort = context.getGuiViewPort();
        this.componentFactory = new ComponentFactory(new DefaultLookAndFeelImpl(), assetManager);
    }


    @Override
    public Node getNode() {
        return delegatee;
    }


    public void setPresenter(IScreenPresenter presenter) {
        //this.presenter = presenter;
        setupUserView();
    }



    // incoming from the presenter
    public void setCelestialList(List<ICelestial> list) {
        System.out.println("incoming list of planets: " + list);
    }

    // incoming from the presenter
    public void setSelectedPlanet(ICelestial iCelestial) {
        System.out.println("selected planet: " + iCelestial);
    }


    // create the whole view
    private void setupUserView() {
        BitmapFont guiFont = assetManager.loadFont("Interface/Fonts/Console.fnt");
        BitmapText perfText = new BitmapText(guiFont, false);
        perfText.setSize(guiFont.getCharSet().getRenderedSize());
        perfText.setColor(ColorRGBA.Blue);
        perfText.setLocalTranslation(10, perfText.getLineHeight() * 10, 0);
        perfText.setText("hello");
        delegatee.attachChild(perfText);
        //
        delegatee.attachChild(componentFactory.createWindow());
    }

    public void setCurrentAvatar(final IAvatar iAvatar) {

    }


    ICommand ongoingCommand = NullCommand.INSTANCE;


    @Override
    public void mouseMotion(final MouseMotionEvent evt) {

        if (ongoingCommand != null) {
            ongoingCommand.updateCommand(evt);
        }

    }

    @Override
    public void mouseButton(final MouseButtonEvent evt) {
        LOGGER.info("StellarScreenView: incoming mouse event: " + evt);

        Window window;
        if ((window = findDragObject(evt)) != null) {
            if (isResizeStartGesture(evt, window)) {
                ongoingCommand = new ResizeCommand(window, new Vector2f(evt.getX(), evt.getY()));
                evt.setConsumed();
            } else {
                ongoingCommand = new DragAndDropCommand(window, new Vector2f(evt.getX(), evt.getY()));
                evt.setConsumed();
            }
        }
        else if (isEndGesture(evt)) {
            ongoingCommand.execute(evt);
            ongoingCommand = NullCommand.INSTANCE;
            evt.setConsumed();
        }
    }


    private boolean isResizeStartGesture(final MouseButtonEvent evt, final Window window) {
        float maxX = window.getLocation().x + window.getSize().x;
        float minY = window.getLocation().y;
        float minX = maxX - 15;
        float maxY = minY + 15;
        boolean inDragRegion = (maxX > evt.getX()) && (minX < evt.getX()) && (maxY > evt.getY()) && (minY < evt.getY());
        return (evt.isPressed() && (evt.getButtonIndex() == MouseInput.BUTTON_LEFT) && inDragRegion);
    }


    private Window findDragObject(final MouseButtonEvent evt) {
        if (evt.isPressed() && (evt.getButtonIndex() == MouseInput.BUTTON_LEFT)) {
            for (Window window : componentFactory.getWindows()) {
                if (window.getWorldBound().intersects(new Vector3f(evt.getX(), evt.getY(), 0))) {
                    return window;
                }
            }
        }
        return null;
    }

    private boolean isEndGesture(final MouseButtonEvent evt) {
        return (evt.isReleased() && (evt.getButtonIndex() == MouseInput.BUTTON_LEFT));
    }


    private IResizable getResizable(final MouseButtonEvent evt) {
        // TODO Auto-generated method stub
        return null;
    }


}
