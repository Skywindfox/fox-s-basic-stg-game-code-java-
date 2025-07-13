// GamePad.java
package mygame;

import java.awt.*;
import javax.swing.*;
import entity.player;
import entity.woodenPost;

public class GamePad extends JPanel implements GameUpdateCallback, GameRenderCallback {
    final int OrigionalTileSize = 16;
    final int scale = 2;
    public final int TileSize = OrigionalTileSize * scale;

    final int maxScreenCol = 32;
    final int maxScreenRow = 18;
    final int screenWidth = TileSize * maxScreenCol;
    final int screenHeight = TileSize * maxScreenRow;

    int fps = 60;
    private int currentFPS;

    private final KeyControl KeyH = new KeyControl();
    public final GameArea gameArea;
    private final UIController uiController;
    private final player p;
    private final woodenPost woodenPost;

    private BulletManager bulletManager;
    private GameRenderer renderer;
    private GameLoopManager loopManager;

    public GamePad() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(KeyH);
        this.setFocusable(true);

        this.p = new player(KeyH, this);
        this.woodenPost = new woodenPost(200, 200, this.p);
        this.gameArea = new GameArea(screenWidth, screenHeight);
        this.uiController = new UIController(this, p, KeyH, gameArea);

        this.bulletManager = new BulletManager(p, woodenPost);
        this.renderer = new GameRenderer(gameArea, p, woodenPost, uiController);
    }

    public void startGameThread() {
        loopManager = new GameLoopManager(fps, this, this);
        new Thread(loopManager).start();
    }

    @Override
    public void update() {
        p.update();
        bulletManager.updateBullets();
    }

    @Override
    public void render() {
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        renderer.render((Graphics2D) g, currentFPS);
    }

    @Override
    public void setFPS(int fps) {
        this.currentFPS = fps; // 直接赋值
    }

}
