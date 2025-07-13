package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import entity.player;

/**
 * 木桩类，继承自entity基类
 * 用于测试攻击反馈和碰撞检测
 */
public class woodenPost extends entity
{
	// 基础属性
	private int maxHP = 5;     // 最大生命值
	private int hp = maxHP;    // 当前生命值
	private BufferedImage image; // 木桩图像
	private player player;     // 玩家引用，用于加分和奖励
	private final int SIZE = 32; // 木桩尺寸常量，避免硬编码

	/**
	 * 木桩构造函数
	 * @param x 初始X坐标
	 * @param y 初始Y坐标
	 * @param playerInstance 玩家实例引用
	 */
	public woodenPost(int x, int y, player playerInstance)
	{
		// 初始化位置和属性
		this.x = x;
		this.y = y;
		this.hp = maxHP;
		this.player = playerInstance;
		loadImage();
	}

	/**
	 * 加载木桩图像
	 * 如果加载失败，创建一个青色矩形作为替代
	 */
	private void loadImage()
	{
		try 
		{
			image = ImageIO.read(getClass().getResourceAsStream("/monster/monster2.png"));
		}
		catch(IOException e)
		{
			e.printStackTrace();
			// 创建替代图像
			image = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = image.createGraphics();
			g.setColor(Color.cyan);
			g.fillRect(0, 0, SIZE, SIZE);
			g.dispose();
		}
	}

	/**
	 * 木桩受到伤害
	 * 如果生命值降至0，给玩家加分和奖励
	 * @param damage 伤害值
	 */
	public void takeDamage(int damage)
	{
		this.hp -= damage;
		if(this.hp <= 0)
		{
			this.hp = 0;
			if (player != null)
			{
				player.addScore(1000); // 击败木桩加分
				player.addHpFragment(); // 使用统一的方法增加残机碎片
			}
			else
			{
				System.out.println("Player reference is null!");
			}
		}		
	}

	/**
	 * 绘制木桩及其生命值条
	 * @param g2 图形上下文
	 */
	public void draw(Graphics2D g2)
	{
		// 绘制木桩图像
		g2.drawImage(image, x, y, SIZE, SIZE, null);
		
		// 绘制生命值背景条（红色）
		g2.setColor(Color.red);
		g2.fillRect(x, y - 10, SIZE, 5);
		
		// 绘制当前生命值条（绿色）
		g2.setColor(Color.green);
		g2.fillRect(x, y - 10, (int)(SIZE * ((float)hp / maxHP)), 5);
	}

	/**
	 * 获取木桩当前生命值
	 * @return 当前生命值
	 */
	public int getHP()
	{
		return this.hp;
	}
	
	/**
	 * 获取木桩尺寸
	 * @return 木桩尺寸
	 */
	public int getSize()
	{
		return SIZE;
	}
}
