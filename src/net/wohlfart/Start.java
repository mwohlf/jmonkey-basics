package net.wohlfart;

public final class Start {

    private Start() {};

    public static void main(final String[] args) {
        final Game game = new Game();
        game.setSettings(game.getWindowSetting());
        //game.setSettings(game.getFullscreenSetting());
        game.start();
    }

}
