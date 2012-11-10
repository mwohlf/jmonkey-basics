package net.wohlfart;

public final class Start {

    private Start() {};

    public static void main(String[] args) {
        Game game = new Game();
        game.setSettings(game.getWindowSetting());
        //game.setSettings(game.getFullscreenSetting());
        game.start();
    }

}
