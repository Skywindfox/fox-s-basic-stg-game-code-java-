package enemy;

import java.awt.image.BufferedImage;

/**
 * 基础敌人类，继承自entity.entity
 * 实现了敌人的基本属性和行为
 */
public class basicEnemy extends entity.entity
{
	//基础值设定
	public int MaxHp , CurrentHp ;
	public int x , y ;
	public double Speed ;
	public boolean isAlive ;
	public int score , powerReword , HpReword ;
	public BufferedImage pic1 , pic2 , pic3 , pic4 ;
	public int attackCooldown , currentAttackTimer ;
	public float moveDrectionX , moveDrectionY ;
	
	
	/**
	 * 基础敌人构造函数
	 * @param x 初始X坐标
	 * @param y 初始Y坐标
	 * @param Speed 移动速度
	 * @param hpMax 最大生命值
	 * @param score 击败后获得的分数
	 * @param powerReword 击败后获得的能量奖励
	 * @param HpReword 击败后获得的生命奖励
	 * @param attackCooldown 攻击冷却时间
	 */
	public basicEnemy(int x , int y , double Speed , int hpMax , int score , int powerReword , int HpReword , int attackCooldown)
	{
		this.x = x;
		this.y = y;
		this.MaxHp = hpMax;  // 修复：使用参数hpMax而不是未定义的hp
		this.CurrentHp = hpMax;  // 修复：使用参数hpMax而不是未定义的hp
		this.Speed = Speed;
		this.score = score;
		this.attackCooldown = attackCooldown;
		this.isAlive = true;
		this.powerReword = powerReword ; 
		this.HpReword = HpReword ;
	}
	
	/**
	 * 敌人受到伤害
	 * @param damage 伤害值
	 * @return 如果敌人死亡则返回true，否则返回false
	 */
	public boolean takeDamage(int damage) {
		CurrentHp -= damage;
		if (CurrentHp <= 0) {
			isAlive = false;
			return true; // 敌人已死亡
		}
		return false; // 敌人仍然存活
	}
	
	/**
	 * 更新敌人状态
	 */
	public void update() {
		if (!isAlive) return;
		
		// 移动逻辑
		x += moveDrectionX * Speed;
		y += moveDrectionY * Speed;
		
		// 攻击冷却逻辑
		if (currentAttackTimer > 0) {
			currentAttackTimer--;
		}
	}
}
