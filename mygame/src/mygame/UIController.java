// UIController.java
package mygame;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import entity.player;

/**
 * 游戏UI控制器，负责管理游戏界面元素的绘制
 */
public class UIController {
    // ========== 依赖注入 ==========  
    private final GamePad gamePad;       // 游戏主面板引用
    private final player player;        // 玩家对象引用
    private final KeyControl keyControl; // 键盘控制引用
    private final GameArea gameArea;    // 游戏区域引用
    
    // ========== 样式配置 ==========  
    private final Color panelColor = new Color(0, 0, 0, 150); // 半透明黑色面板背景（RGBA格式）
    private final Color textColor = Color.WHITE;               // 信息文本颜色
    private final Font infoFont = new Font("Consolas", Font.BOLD, 20); // 信息面板字体
    private final Font fpsFont = new Font("Comic sans MS", Font.BOLD, 16); // FPS显示字体
    
    // ========== 动态数据 ==========  
    private String fpsDisplay = "FPS: ??? "; // FPS显示缓存字符串

    //==========UI图片引用~==========  
    //private BufferedImage fullHeart;
    //private BufferedImage halfHeart;
    private BufferedImage[] heartStates = new BufferedImage [4];
    private BufferedImage fullHeart;
    private final int HEART_SIZE = 20;
    
    /**
     * 构造函数（依赖注入模式）
     * @param gamePad 游戏主面板
     * @param player 玩家对象
     * @param keyControl 键盘控制
     * @param gameArea 游戏区域
     */
    public UIController(GamePad gamePad, player player, KeyControl keyControl, GameArea gameArea) {
        this.gamePad = gamePad;
        this.player = player;
        this.keyControl = keyControl;
        this.gameArea = gameArea;

        // 图片加载放在构造函数内
        try {
            fullHeart = ImageIO.read(getClass().getResourceAsStream("/UI/HP_full.png"));
            //halfHeart = ImageIO.read(getClass().getResourceAsStream("/UI/HP_half.png"));
            heartStates[0] = ImageIO.read(getClass().getResourceAsStream("/UI/hp_0.png"));
            heartStates[1] = ImageIO.read(getClass().getResourceAsStream("/UI/hp_1.png"));
            heartStates[2] = ImageIO.read(getClass().getResourceAsStream("/UI/hp_2.png"));
            heartStates[3] = ImageIO.read(getClass().getResourceAsStream("/UI/hp_3.png"));
        } catch (IOException e) {
            System.err.println("图片加载失败，使用替代图形");
            e.printStackTrace();
        }
    }

    /**
     * 更新FPS显示值
     * @param fps 当前帧率
     */
    public void updateFPS(int fps) {
        this.fpsDisplay = "FPS: " + fps; // 格式化显示字符串
    }

    /**
     * 主绘制方法（由GamePad调用）
     * @param g2 图形上下文对象
     */
    public void draw(Graphics2D g2) {
        // 启用图形抗锯齿
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        drawInfoPanel(g2); // 绘制右侧信息面板
        drawFPS(g2);       // 绘制右上角FPS
    }

    /**
     * 绘制右侧信息面板（包含得分、生命值等）
     * @param g2 图形上下文对象
     */
    private void drawInfoPanel(Graphics2D g2) {
        // ===== 面板位置计算 =====
        int panelX = gameArea.getX() + gameArea.getWidth() + 20;         // 游戏区域右侧20像素
        int panelY = gameArea.getY() + 50;                               // 与游戏区域顶部对齐？
        int panelWidth = gamePad.screenWidth - panelX - 20;               // 右侧保留20像素边距

        // ===== 绘制圆角背景 =====
        Shape panel = new RoundRectangle2D.Double(panelX, panelY, panelWidth, 120, 15, 15);
        g2.setColor(panelColor);
        g2.fill(panel); // 填充背景

        // ===== 文字内容设置 =====
        g2.setFont(infoFont);
        g2.setColor(textColor);
        
        // ===== hp计算 =====
        int fullHearts = player.hp / 4;  // 完整爱心数量（每4个破损爱心转换为1个完整爱心）
        int damagedHearts = player.hp % 4;  // 破损爱心数量
        int maxHearts = 8;  // 最大爱心数（最多8颗）

        // 信息行数组（可扩展）
        String[] lines = {
            "SCORE: " + player.getDisplayScore(),       // 玩家得分
            "HP: " + player.hp,                         // 生命值
            "SPEED: " + (keyControl.ShiftPressed ? "LOW" : "NORMAL"), // 速度状态
            "SHOOT: " + (keyControl.ZPressed ? "shoot" : "no")        // 射击状态
        };

        // ===== 逐行绘制文字 =====
        int textY = panelY + 40; // 初始Y坐标（距面板顶部40像素）
        for (String line : lines) {
            g2.drawString(line, panelX + 20, textY); // X坐标偏移20像素
            textY += 30; // 行间距30像素
        }
        
     // ===== HP绘制 =====
        int HP_max = 8;              // 最大爱心数量
        int HP_full = player.hp;     // 完整爱心数量（直接对应显示数量）
        int HP_half = player.brokenHp; // 破损爱心进度（0-3）

        // 获取"HP: "文本的宽度用于定位图标
        FontMetrics fm = g2.getFontMetrics();
        int hpLabelWidth = fm.stringWidth("HP: ");
        
        // 起始绘制坐标（在"HP: "文本右侧）
        int startX = panelX + 20 + hpLabelWidth ; // +5像素间隔
        int startY = panelY + 55;                    // 与文字基线对齐的Y坐标

        // 绘制完整爱心（最多8个）
        for (int i = 0; i < HP_full && i < HP_max; i++) {
            if (fullHeart != null) {
                g2.drawImage(fullHeart, 
                            startX + i * (HEART_SIZE + 5), 
                            startY, 
                            HEART_SIZE, 
                            HEART_SIZE, 
                            null);
            } else { // 图片加载失败时回退到方块显示
                g2.setColor(Color.RED);
                g2.fillRect(startX + i * 25, startY, 20, 20);
            }
        }

        // 绘制破损爱心进度（仅在未满8个时显示）
        if (HP_full < HP_max && HP_half > 0) {
            // 计算破损爱心的位置
            int brokenX = startX + HP_full * (HEART_SIZE + 5);
            
            // 根据进度选择贴图（0-3对应不同状态）
            int stateIndex = Math.min(HP_half, 3);
            BufferedImage stateImg = heartStates[stateIndex];
            
            if (stateImg != null) {
                g2.drawImage(stateImg, 
                            brokenX, 
                            startY, 
                            HEART_SIZE, 
                            HEART_SIZE, 
                            null);
            } else { // 图片加载失败时回退到文字
                g2.setColor(Color.ORANGE);
                g2.drawString(HP_half + "/4", brokenX, startY + HEART_SIZE);
            }
        }
    }

    /**
     * 绘制右上角FPS计数器
     * @param g2 图形上下文对象
     */
    private void drawFPS(Graphics2D g2) {
        // ===== 字体设置 =====
        g2.setFont(fpsFont);
        g2.setColor(Color.YELLOW); // 使用醒目的黄色
        
        // ===== 动态位置计算 =====
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(fpsDisplay);  // 获取文本像素宽度
        int x = gamePad.screenWidth - textWidth - 20; // 屏幕宽度-文本宽度-右边距20
        int y = 30; // 距顶部30像素

        // ===== 绘制文本 =====
        g2.drawString(fpsDisplay, x, y);
    }
}
