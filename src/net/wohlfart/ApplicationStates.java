package net.wohlfart;

import net.wohlfart.states.StellarState;

import com.jme3.app.state.AppState;

public enum ApplicationStates {
    INTRO(null),
    GAME(new StellarState());


    AppState appState;


    ApplicationStates(final AppState appState) {
        this.appState = appState;
    }

    AppState instance() {
        return appState;
    }

}
