package assets;

/**
 * This is the listing of all zombie types we have
 * 
 * Useful for command parsing and spawning
 * @author david
 *
 */
public enum ZombieTypes {
	REG_ZOMBIE;
	
	public static Zombie toZombie(ZombieTypes e) 
	{
		switch (e) {
			case REG_ZOMBIE:
				return new Regular_Zombie();
			default: 
				return null;
		}
	}
}