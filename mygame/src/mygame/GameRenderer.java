// GameRenderer.java
package mygame;

import java.awt.*;
import entity.player;
import entity.woodenPost;

public class GameRenderer {
    private final GameArea gameArea;
    private final player player;
    private final woodenPost woodenPost;
    private final UIController uiController;

    public GameRenderer(GameArea area, player p, woodenPost wp, UIController ui) {
        this.gameArea = area;
        this.player = p;
        this.woodenPost = wp;
        this.uiController = ui;
    }

    public void render(Graphics2D g2, int fps) {
        gameArea.draw(g2);
        player.draw(g2);
        woodenPost.draw(g2);
        uiController.updateFPS(fps);
        uiController.draw(g2);
    }
}
