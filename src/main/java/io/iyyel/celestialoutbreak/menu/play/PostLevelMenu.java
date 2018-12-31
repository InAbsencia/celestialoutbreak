package io.iyyel.celestialoutbreak.menu.play;

import io.iyyel.celestialoutbreak.controller.GameController;
import io.iyyel.celestialoutbreak.controller.GameController.State;
import io.iyyel.celestialoutbreak.handler.LevelHandler;
import io.iyyel.celestialoutbreak.level.Level;
import io.iyyel.celestialoutbreak.menu.AbstractMenu;

import java.awt.*;

public final class PostLevelMenu extends AbstractMenu {

    private final LevelHandler levelHandler = LevelHandler.getInstance();

    public PostLevelMenu(GameController gameController) {
        super(gameController);
    }

    @Override
    public void update() {
        decInputTimer();

        if (inputHandler.isOKPressed() && isInputAvailable()) {
            resetInputTimer();
            gameController.switchState(State.MAIN);
        }

    }

    @Override
    public void render(Graphics2D g) {
        Level activeLevel = levelHandler.getActiveLevel();

        g.setColor(menuFontColor);
        drawCenterString(textHandler.GAME_TITLE, 100, g, titleFont);

        drawSubmenuTitle(activeLevel.getName(), g);
        drawCenterString("Victory!", gameController.getHeight() / 2, g, msgFont);

        drawInfoPanel(g);
    }

}