package net.wohlfart.ui;

import java.util.List;
import java.util.logging.Logger;

import net.wohlfart.IStateContext;
import net.wohlfart.model.planets.ICelestial;
import net.wohlfart.ui.commands.DragAndDropCommand;
import net.wohlfart.ui.commands.ICommand;
import net.wohlfart.ui.commands.NullCommand;
import net.wohlfart.ui.components.ComponentFactory;
import net.wohlfart.ui.components.Window;
import net.wohlfart.user.IAvatar;

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
public class StellarScreenView implements IScreenView {
    private static final Logger LOGGER = Logger.getLogger(StellarScreenView.class.getName());

    private static final String SCREEN_ID = "stellarScreen";

    protected final Node delegatee;
    protected final AssetManager assetManager;
    protected final ViewPort guiViewPort;
    protected final ComponentFactory componentFactory;


    public StellarScreenView(final IStateContext context) {
        delegatee = new Node(SCREEN_ID);
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


    ICommand dragAndDropCommand = NullCommand.INSTANCE;


    @Override
    public void mouseMotion(final MouseMotionEvent evt) {

        if (dragAndDropCommand != null) {
            dragAndDropCommand.updateCommand(evt);
        }

    }

    @Override
    public void mouseButton(final MouseButtonEvent evt) {
        LOGGER.info("StellarScreenView: incoming mouse event: " + evt);

        DragAndDropCommand.IDragAndDropable dragAndDropable;
        if (isDragStartGesture(evt) && (dragAndDropable = getDragableComponent(evt)) != null) {
            dragAndDropCommand = new DragAndDropCommand(dragAndDropable, new Vector2f(evt.getX(), evt.getY()));
            evt.setConsumed();
        }
        else if (isDragEndGesture(evt)) {
            dragAndDropCommand.execute(evt);
            dragAndDropCommand = NullCommand.INSTANCE;
            evt.setConsumed();
        }

    }


    private boolean isDragEndGesture(final MouseButtonEvent evt) {
        return (evt.isReleased() && (evt.getButtonIndex() == MouseInput.BUTTON_LEFT));
    }

    private boolean isDragStartGesture(final MouseButtonEvent evt) {
        return (evt.isPressed() && (evt.getButtonIndex() == MouseInput.BUTTON_LEFT));
    }

    private Window getDragableComponent(final MouseButtonEvent evt) {
        int x = evt.getX();
        int y = evt.getY();
        for (Window window : componentFactory.getWindows()) {
            if (window.getWorldBound().intersects(new Vector3f(x, y, 0))) {
                evt.setConsumed(); // start dragging the drag is done by the move method
                return window;
            }
        }
        return null; // nothing found
    }


}
