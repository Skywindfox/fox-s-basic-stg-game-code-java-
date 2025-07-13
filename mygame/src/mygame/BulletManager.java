// BulletManager.java
package mygame;

import entity.player;
import entity.bullet;
import entity.woodenPost;

public class BulletManager {
    private final player player;
    private final woodenPost woodenPost;

    public BulletManager(player p, woodenPost wp) {
        this.player = p;
        this.woodenPost = wp;
    }

    public void updateBullets() {
        for (bullet b : player.getBullets()) {
            if (b.active && checkCollision(b, woodenPost)) {
                woodenPost.takeDamage(1);
                b.active = false;
            }
        }
    }

    private boolean checkCollision(bullet b, woodenPost wp) {
        return b.x > wp.x && b.x < wp.x + 32 &&
               b.y > wp.y && b.y < wp.y + 32;
    }
}
