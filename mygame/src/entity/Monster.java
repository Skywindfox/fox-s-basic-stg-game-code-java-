package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * 怪物类，实现游戏中的敌对单位
 * 包含基本属性和行为
 */
public class Monster {
    public int x, y, hp, speed;
    public BufferedImage image;
    public String type;  // 用于区分怪物类型，例如 "zombie", "ghost"
    public boolean isActive = true; // 标记怪物是否活跃
    public int scoreValue = 100; // 击败怪物获得的默认分数

    /**
     * 怪物构造函数
     * @param x 初始X坐标
     * @param y 初始Y坐标
     * @param hp 生命值
     * @param speed 移动速度
     * @param image 怪物图像
     * @param type 怪物类型
     */
    public Monster(int x, int y, int hp, int speed, BufferedImage image, String type) {
        this.x = x;
        this.y = y;
        this.hp = hp;
        this.speed = speed;
        this.image = image;
        this.type = type;
    }

    /**
     * 更新怪物状态
     */
    public void update() {
        if (!isActive) return; // 如果怪物已经不活跃，不执行更新
        
        y += speed; // 向下移动
        
        // 死亡逻辑
        if (hp <= 0) {
            die();
        }
    }

    /**
     * 怪物死亡处理
     */
    public void die() {
        isActive = false;
        // 可以在这里添加死亡动画、音效等逻辑
    }
    
    /**
     * 怪物受到伤害
     * @param damage 伤害值
     * @return 如果怪物死亡则返回true，否则返回false
     */
    public boolean takeDamage(int damage) {
        hp -= damage;
        if (hp <= 0) {
            die();
            return true; // 怪物已死亡
        }
        return false; // 怪物仍然存活
    }

    /**
     * 绘制怪物
     * @param g2 图形上下文
     */
    public void draw(Graphics2D g2) {
        if (isActive) {
            g2.drawImage(image, x, y, null);
        }
    }
    
    /**
     * 检查怪物是否超出屏幕边界
     * @param screenHeight 屏幕高度
     * @return 如果怪物超出屏幕则返回true
     */
    public boolean isOutOfBounds(int screenHeight) {
        return y > screenHeight;
    }
}
