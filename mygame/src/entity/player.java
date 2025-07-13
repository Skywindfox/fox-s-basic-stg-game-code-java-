package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

import mygame.GamePad;
import mygame.KeyControl;

/**
 * 玩家类，继承自entity基类
 * 实现玩家角色的移动、射击和状态管理
 */
public class player extends entity
{
    // 基础属性
    private GamePad gp;
    private KeyControl KeyH;
    private ArrayList<bullet> bullets = new ArrayList<>();
    private long lastShotTime = 0;
    public int brokenHp = 0;  // 残机碎片数，允许值为0-3
    
    // 分数控制
    private int targetScore = 0;
    private float displayScore;
    private final float smoothSpeed = 0.1f;
    private final long shotCooldown = 25;  // 射击冷却时间（毫秒）
    private final int MAX_HP = 8;  // 玩家最大生命值

    /**
     * 玩家构造函数
     * @param KeyH 键盘控制器
     * @param gp 游戏面板
     */
    public player(KeyControl KeyH, GamePad gp)
    {
        this.gp = gp;
        this.KeyH = KeyH;
        setDefaultValues();
        loadPlayerImages();
    }

    /**
     * 设置玩家默认值
     */
    private void setDefaultValues()
    {
        x = 350;
        y = 400;
        hp = 3; 
        speed = 4;  // 修正：使用小写s的speed，保持命名一致性
        direction = "stand";
        behavior = "free";
    }

    /**
     * 加载玩家图片资源
     */
    private void loadPlayerImages() 
    {
        try {
            stand1 = ImageIO.read(getClass().getResourceAsStream("/player/player.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/player/left1.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/player/right1.png"));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 增加分数
     * @param points 增加的分数点数
     */
    public void addScore(int points)
    {
        targetScore += points;
    }

    /**
     * 增加残机碎片数
     * 当碎片数达到4时，增加一个完整生命值（如果未达到上限）
     */
    public void collectPiece() 
    {
        addHpFragment();
    }
    
    /**
     * 获取当前显示的分数（平滑过渡后）
     * @return 当前显示分数
     */
    public int getDisplayScore()
    {
        return Math.round(displayScore);
    }

    /**
     * 增加残机碎片并在达到阈值时增加生命值
     * 合并了collectPiece和addHpAuto的功能
     */
    public void addHpFragment()
    {
        brokenHp++;
        if (brokenHp >= 4)
        {
            if (hp < MAX_HP) 
            {
                hp++;
                brokenHp = 0;
            } else 
            {
                brokenHp = 3; // 保持最大值
            }
        }
    }
    
    /**
     * 更新玩家状态
     * 处理移动、射击和边界检测
     */
    public void update()
    {
        // 根据Shift键状态确定移动速度
        int playerSpeed = (KeyH.ShiftPressed) ? speed / 2 : speed;  // 修正：使用小写s的speed

        // 处理玩家的移动
        if (KeyH.WPressed) y -= playerSpeed;
        if (KeyH.SPressed) y += playerSpeed;
        if (KeyH.APressed) x -= playerSpeed;
        if (KeyH.DPressed) x += playerSpeed;

        // 更新玩家方向
        updateDirection();

        // 限制玩家的移动范围
        x = gp.gameArea.clampX(x, gp.TileSize);
        y = gp.gameArea.clampY(y, gp.TileSize);

        // 分数平滑过渡
        updateScoreDisplay();

        // 处理射击逻辑
        handleShooting();

        // 更新子弹状态，移除无效子弹
        updateBullets();
    }

    /**
     * 根据移动方向更新玩家朝向
     */
    private void updateDirection() {
        if (KeyH.APressed) {
            direction = "left";
        } else if (KeyH.DPressed) {
            direction = "right";
        } else {
            direction = "stand";
        }
    }

    /**
     * 更新分数显示（平滑过渡）
     */
    private void updateScoreDisplay() {
        if (displayScore != targetScore) {
            displayScore += (targetScore - displayScore) * smoothSpeed;
            if (Math.abs(targetScore - displayScore) < 500) {
                displayScore = targetScore;
            }
        }
    }

    /**
     * 处理玩家射击逻辑
     */
    private void handleShooting() {
        if (KeyH.ZPressed) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastShotTime > shotCooldown) {
                int bulletX = x + gp.TileSize / 2 - 4;  // 子弹起始X位置
                int bulletY = y;  // 子弹起始Y位置
                bullets.add(new bullet(gp, bulletX, bulletY));  // 创建子弹
                lastShotTime = currentTime;  // 更新射击时间
            }
        }
    }

    /**
     * 更新所有子弹状态
     */
    private void updateBullets() {
        bullets.removeIf(bullet -> !bullet.active);
        for (bullet b : bullets) {
            b.update();
        }
    }

    /**
     * 绘制玩家和子弹
     * @param g2 图形上下文
     */
    public void draw(Graphics2D g2) 
    {
        BufferedImage image = null;

        // 根据玩家的方向选择对应的图片
        switch (direction) {
            case "stand": image = stand1; break;
            case "left": image = left1; break;
            case "right": image = right1; break;
        }

        // 绘制玩家角色
        g2.drawImage(image, x, y, gp.TileSize, gp.TileSize, null);

        // 绘制所有子弹
        for (bullet b : bullets) {
            b.draw(g2);
        }
    }
    
    /**
     * 获取玩家发射的所有子弹 
     * @return 子弹列表
     */
    public ArrayList<bullet> getBullets() 
    {
        return bullets;
    }
    
    /**
     * 玩家受到伤害
     * @param damage 伤害值
     * @return 如果玩家死亡则返回true
     */
    public boolean takeDamage(int damage) {
        hp -= damage;
        if (hp <= 0) {
            // 玩家死亡逻辑
            return true;
        }
        return false;
    }
}
