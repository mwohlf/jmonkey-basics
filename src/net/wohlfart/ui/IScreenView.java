package net.wohlfart.ui;

import com.jme3.scene.Node;

public interface IScreenView {
    
    
    public interface IScreenPresenter extends IPresenter {
        
    }
    
    public abstract Node getNode();


}
