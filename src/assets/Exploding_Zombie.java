package assets;

import java.io.Serializable;

import engine.Board;

/**
 * Class for Exploding Zombies that damages on units on a grid
 * when attacking plant. 
 * 
 * @author Michael Patsula
 *
 */
public class Exploding_Zombie extends Zombie implements Serializable {

	private static final int DEFAULT_SPEED = SPEED_LOW;
	private static final int DEFAULT_POWER = ATTACK_INSTANT; //this value is irrelevant. The zombie will instantly kill plant
	private static final int DEFAULT_HP = HEALTH_LOW;
	private static final ZombieTypes ZOMBIE_TYPE = ZombieTypes.EXP_ZOMBIE;
	
	public Exploding_Zombie()	{
		super(DEFAULT_SPEED, DEFAULT_POWER, DEFAULT_HP);
	}
	
	@Override 
	public String toString() {
		
		return ZOMBIE_TYPE.toString();
	}

	public ZombieTypes getZombieType() {
		return ZOMBIE_TYPE;
	}

	@Override
	public int getDefaultSpeed() {
		return DEFAULT_SPEED;
	} 
	
	/**
	 * Zombie attack method.
	 */
	@Override
	public void attack(Board board) {
		Plant plantTarget = board.getPlant(getRow(), getCol());
		plantTarget.takeDamage(plantTarget.getHP());
		board.removePlant(plantTarget.getRow(), plantTarget.getCol());
		
		//Game must remove this zombie after attack
	}

}
