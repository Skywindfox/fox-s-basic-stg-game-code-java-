package entity;

import java.awt.image.BufferedImage;

/**
 * 实体基类，作为游戏中所有实体对象的基础
 * 定义了共有的属性如位置、生命值、图像资源等
 */
public class entity 
{
	// 基础属性
	public int x, y;       // 实体位置坐标
	public int hp;         // 生命值
	public int speed;      // 移动速度（已规范化命名，原为Speed）
	
	// 图像资源
	public BufferedImage stand1, stand2, stand3, stand4;  // 站立动画帧
	public BufferedImage left1, left2, left3, left4;      // 向左移动动画帧
	public BufferedImage right1, right2, right3, right4;  // 向右移动动画帧
	
	// 状态标识
	public String direction;  // 朝向：stand, left, right
	public String behavior;   // 行为状态：free, attack, etc.
	
	/**
	 * 默认构造函数
	 */
	public entity() {
		// 默认初始化
		this.x = 0;
		this.y = 0;
		this.hp = 1;
		this.speed = 1;
		this.direction = "stand";
		this.behavior = "free";
	}
	
	/**
	 * 带参数的构造函数
	 * @param x 初始X坐标
	 * @param y 初始Y坐标
	 * @param hp 初始生命值
	 * @param speed 初始速度
	 */
	public entity(int x, int y, int hp, int speed) {
		this.x = x;
		this.y = y;
		this.hp = hp;
		this.speed = speed;
		this.direction = "stand";
		this.behavior = "free";
	}
}
