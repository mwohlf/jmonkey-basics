package net.wohlfart.ui;

import java.util.List;

import net.wohlfart.IStateContext;
import net.wohlfart.model.planets.ICelestial;
import net.wohlfart.ui.components.ComponentFactory;
import net.wohlfart.user.IAvatar;

import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;

/*
 * view for a screen
 */
public class StellarScreenView implements IScreenView {
    private static final String SCREEN_ID = "stellarScreen";

    protected final Node delegatee;
    protected final AssetManager assetManager;
    protected final ViewPort guiViewPort;
    //protected StellarScreenPresenter presenter;
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




    public void setPresenter(StellarScreenPresenter presenter) {
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

    public void updateLogicalState(float timePerFrame) {
        delegatee.updateLogicalState(timePerFrame);
    }

    public void updateGeometricState() {
        delegatee.updateGeometricState();
    }


    @Override
    public void mouseMotion(final MouseMotionEvent evt) {
        int x = evt.getX();
        int y = evt.getY();
        // TODO Auto-generated method stub
        CollisionResults results = new CollisionResults();
        for (Spatial spatial : delegatee.getChildren()) {
           boolean hit = spatial.getWorldBound().intersects(new Vector3f(x, y, 0));
           System.out.println("hit " + hit + "---->  x: " + x + " y: " + y + " spatial: " + spatial);
        }
        // delegatee.collideWith(, results);
        // delegatee.collideWith(ray, results);
    };

}
