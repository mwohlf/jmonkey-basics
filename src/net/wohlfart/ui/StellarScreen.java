package net.wohlfart.ui;

import java.util.List;

import net.wohlfart.model.planets.ICelestial;

/*
 * view for a screen
 */
public class StellarScreen extends AbstractScreenView<StellarScreenPresenter, StellarScreen> {


    public StellarScreen(final String id) {
        super(id);
    }


    public void setCelestialList(List<ICelestial> list) {
        System.out.println("incoming list of planets: " + list);
    }

    public void setSelectedPlanet(ICelestial iCelestial) {
        System.out.println("selected planet: " + iCelestial);
    }


}
