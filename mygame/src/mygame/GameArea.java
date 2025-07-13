package mygame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.BasicStroke;

public class GameArea {
    // 核心参数
    private final int x;         // 区域左上角X坐标
    private final int y;         // 区域左上角Y坐标
    private final int width;     // 区域宽度（基于3:4比例）
    private final int height;    // 区域高度
    
    // 调试模式
    private boolean debugMode = true;

    public GameArea(int screenWidth, int screenHeight) {
        // 计算区域高度（占屏幕高度的90%）
        this.height = (int)(screenHeight * 0.9);
        // 根据3:4比例计算宽度
        this.width = (int)(height * 3.5 / 4);
        
        // 计算位置（居中偏左：向左偏移15%屏幕宽度）
        int baseX = (screenWidth - width) / 2;
        this.x = baseX - (int)(screenWidth * 0.15);
        this.y = (screenHeight - height) / 2;
    }

    // 核心方法：绘制区域边界
    public void draw(Graphics2D g2) {
        // 绘制白色边框
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(3));
        g2.drawRect(x, y, width, height);
        
        // 调试模式显示网格
        if(debugMode) {
            drawDebugGrid(g2);
        }
    }

    // 调试网格绘制
    private void drawDebugGrid(Graphics2D g2) {
        g2.setColor(new Color(255, 255, 255, 50)); // 半透明白色
        g2.setStroke(new BasicStroke(1));
        
        // 横向网格线（假设TileSize为32）
        for (int i = y; i <= y + height; i += 32) {
            g2.drawLine(x, i, x + width, i);
        }
        
        // 纵向网格线
        for (int j = x; j <= x + width; j += 32) {
            g2.drawLine(j, y, j, y + height);
        }
    }

    // 边界检测方法（供Player类调用）
    public boolean isWithinBounds(int objX, int objY, int objWidth, int objHeight) {
        return objX >= x && 
               objX + objWidth <= x + width &&
               objY >= y && 
               objY + objHeight <= y + height;
    }

    // 动态边界限制（直接返回修正后的坐标）
    public int clampX(int targetX, int objWidth) {
        return Math.max(x, Math.min(x + width - objWidth, targetX));
    }
    
    public int clampY(int targetY, int objHeight) {
        return Math.max(y, Math.min(y + height - objHeight, targetY));
    }

    // Getter方法
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    
    // 调试模式开关
    public void toggleDebug() {
        debugMode = !debugMode;
    }
}