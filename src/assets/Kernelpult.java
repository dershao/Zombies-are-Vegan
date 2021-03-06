package assets;

import engine.Board;
import util.Logger;

import java.io.Serializable;
import java.util.Random;

/**
 * Class for the plant type "Kernelpult" which catapults corn.
 * The corn has a 25% chance of immobilizing zombies.
 * 
 * @author Derek Shao
 *
 */
public class Kernelpult extends Plant implements Serializable {

	private static Logger LOG = new Logger("Kernelpult");
	
	private static final int DEFAULT_HP = HEALTH_MEDIUM;
	private static final int DEFAULT_POWER = ATTACK_MEDIUM;
	private static final int COST = 100;
	private static final PlantTypes PLANT_TYPE = PlantTypes.KERNELPULT;
	
	private static final int IMMOBILIZE_ROLL_RANGE = 100;
	private static final int THRESHHOLD_FOR_IMMOBILIZATION = 75;
	
	public Kernelpult() {
		super(DEFAULT_HP, DEFAULT_POWER, COST);
	}
	
	@Override
	public PlantTypes getPlantType() {
		
		return PLANT_TYPE;
	}

	@Override
	public String toString() {
		
		return "Kernel-Pult";
	}
	
	@Override
	public void attack(Board board) {

		int row = getRow();
		int column = getCol();
		
		Zombie zombieTarget = board.getSingleZombieTarget(row, column);
		
		if (zombieTarget != null) {
			LOG.debug(String.format("Kernelpult at : (%d, %d) attacking Zombie at: (%d, %d)", 
					row, column, zombieTarget.getRow(), zombieTarget.getCol()));
			
			zombieTarget.takeDamage(getPower());
			
			if (!zombieTarget.isAlive()) {
				removeZombie(zombieTarget, board);
			}
			else {
				// if the zombie didn't die, there is a chance for immobilization
				// use a random number generator to check if the next attack
				// can immobilize the zombie target
				Random random = new Random();
				
				if (random.nextInt(IMMOBILIZE_ROLL_RANGE) + 1 > THRESHHOLD_FOR_IMMOBILIZATION) {
					zombieTarget.immobilize();
					LOG.debug(String.format("Zombie target at : (%d, %d)", zombieTarget.getRow(), zombieTarget.getCol()));
				}
			}
		}
	}	
}
