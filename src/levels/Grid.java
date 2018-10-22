package levels;

import java.util.LinkedList;
import java.util.Queue;
import assets.Plant;
import assets.Zombie;
 /**
 * Building block for a grid. Contains 1 Plant and N zombies.
 * 
 * @author Derek Shao
 *
 */
public class Grid {
 	private Plant plant;
	private Queue<Zombie> zombies;
	
	public Grid() {
		
		zombies = new LinkedList<Zombie>();
	}
	
	/**
	 * Set the current grid's plant
	 * 
	 * @param newPlant
	 * @return true if setting plant was successful, false otherwise
	 */
	public boolean setPlant(Plant newPlant) {
		
		if (plant != null) {
			plant = newPlant;
			return true;
		}
		
		return false;
	}
	
	/**
	 * Returns the plant occupying this grid
	 * 
	 * @return the plant on this grid
	 */
	public Plant getPlant() {
		
		return plant;
	}
	
	/**
	 * Clear the plant on this grid 
	 *
	 */
	public void removePlant() {
		
		plant = null;
	}
	
	/**
	 * Add a zombie to this grid
	 * 
	 * @param zombie
	 * @return true if zombie was added successfully, false otherwise
	 */
	public boolean addZombie(Zombie zombie) {
		
		return zombies.add(zombie);
	}
	
	
	/**
	 * Get the first zombie that arrived on this grid
	 * 
	 * @return the first zombie on this grid
	 */
	public Zombie getFirstZombie() {
		
		return zombies.peek();
	}
	
	/**
	 * Remove the first zombie on this grid
	 * Likely used when a zombie was killed
	 * 
	 * @return the zombie that was killed, null if no zombies are present
	 */
	public void removeZombie() {
		
		if (!zombies.isEmpty()) {
			zombies.poll();
		}
	}
	
	
	/**
	 * --Likely only for Milestone 1--
	 * Get the number of zombies in the current grid.
	 * 
	 * @return number of zombies
	 */
	public int getNumberOfZombies() {
		
		return zombies.size();
	}
}