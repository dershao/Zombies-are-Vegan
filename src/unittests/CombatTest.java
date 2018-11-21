package unittests;

import static org.junit.Assert.assertFalse;
import engine.*;
import levels.LevelInfo;
import levels.LevelLoader;
import assets.*;

import org.junit.jupiter.api.Test;

/**
 * Tests Combat Class
 * @author Tanisha Garg
 *
 */
public class CombatTest {
	
	/**
	 * tests if pantAttack() kills zombies
	 */
	@Test
	public void testPlantAttack() {
		Board b = new Board(1, 8);
		Combat c = new Combat(b.getBoard());
		b.displayBoard();
		Peashooter p = new Peashooter();
		Regular_Zombie z = new Regular_Zombie();
		b.placePlant(p, 0, 0);
		b.placeZombie(z, 0, 7);
		
		int turnsRequiredToKillZombies = z.getHP()/p.getPower();
		if (z.getHP()%p.getHP() != 0) turnsRequiredToKillZombies++; //Rounding off turnsRequiredToKillZombies if remainder is not zero
		
		//attack zombie until dead
		for(int i = 0; i<turnsRequiredToKillZombies; i++) {
			c.plantAttack(p); 
		}
		assertFalse("Zombie is dead", z.isAlive());
	}
	
	/**
	 * tests if zombieAttack() kills plants
	 */
	@Test
	public void testZombieAttack() {
		LevelInfo lvl = LevelLoader.getLevel(1);
		Game game = new Game(lvl);
		Combat c = new Combat(new Grid[1][1]);
		Board b = new Board(1,8);
		Peashooter p = new Peashooter();
		Regular_Zombie z = new Regular_Zombie();
		b.placePlant(p, 0, 0);
		b.placeZombie(z, 0, 7);
		
		int turnsRequiredToKillZombies = p.getHP()/z.getPower();
		if (p.getHP()%z.getHP() != 0) turnsRequiredToKillZombies++;
		
		if(b.onZombieMove(z,game)==false){ //if zombie reached the plant unit
			for(int i = 0; i<turnsRequiredToKillZombies; i++) {
				c.zombieAttack(z, p); //kill plant until dead
			}
		}
		assertFalse("Plant is dead", p.isAlive());
	}
	
}
