package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import mygame.GamePad;

/**
 * 子弹类，实现玩家发射的子弹逻辑
 * 包含移动、绘制和状态管理
 */
public class bullet 
{
	// 子弹基础属性
	public int x, y;           // 子弹位置坐标
	public int speed;          // 子弹移动速度
	public boolean active;     // 子弹是否有效
	private BufferedImage image; // 子弹图像
	private GamePad gp;        // 游戏面板引用
	
	/**
	 * 子弹构造函数
	 * @param gp 游戏面板引用
	 * @param startX 初始X坐标
	 * @param startY 初始Y坐标
	 */
	public bullet(GamePad gp, int startX, int startY)
	{
		this.gp = gp;
		this.x = startX;
		this.y = startY;
        this.speed = 20;      // 默认速度
        this.active = true;   // 初始状态为活跃
        getBulletImage();     // 加载子弹图像
	}
	
	/**
	 * 加载子弹图像资源
	 * 如果加载失败，创建一个红色矩形作为替代
	 */
	private void getBulletImage() {
        try {
            // 使用资源流加载图像
            image = ImageIO.read(getClass().getResourceAsStream("/player/bullets.png"));
            if (image == null) {
                throw new IOException("无法加载子弹图片：/player/bullets.png");
            }
        } catch (IOException e) {
            System.err.println("子弹图片加载失败，使用替代图形");
            e.printStackTrace();
            
            // 创建红色矩形作为替代图像
            image = new BufferedImage(8, 16, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.setColor(Color.RED);
            g.fillRect(0, 0, 8, 16);
            g.dispose();
        }
    }
	
	/**
	 * 更新子弹状态
	 * 移动子弹并检查是否超出屏幕边界
	 */
	public void update()
	{
		if(active)
		{
			y -= speed;  // 向上移动
			
			// 如果子弹超出屏幕上边界，将其设为非活跃状态
			if(y < 0)
			{
				active = false;
			}
		}
	}
	
	/**
	 * 绘制子弹
	 * @param g2 图形上下文
	 */
	public void draw(Graphics2D g2)
	{
		if(active)
		{
			// 绘制子弹图像，大小为游戏面板瓦片大小的1/4宽和1/2高
			g2.drawImage(image, x, y, gp.TileSize/4, gp.TileSize/2, null);
		}
	}
}
