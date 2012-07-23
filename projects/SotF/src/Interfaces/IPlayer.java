package Interfaces;

import org.newdawn.slick.geom.Vector2f;
import java.util.List;

public interface IPlayer extends IEntity {
	
	
	/**
	 * @param velo the Vector that specifies the new velocity of the player
	 */
	public abstract void setVelocity(Vector2f velo);
	
	
	/**
	 * @return the current velocity of the player
	 */
	public abstract Vector2f getVelocity();
	
	
	/**
	 * @param eat the Eatable item that affects the player
	 */
	public abstract void eat(IEatable eat);
	
	
	/**
	 * @param number the number of the Usable in the player's Inventory
	 * @return the Usable item that is to be placed on the world
	 */
	public abstract IUsable use(int number);
	
	
	/**
	 * @return the Inventory of the player as an array of Usables
	 */
	public abstract IUsable[] getInventory();
	
	
	/**
	 * @return list of Powerups that are currently active on the player
	 */
	public abstract List<IPowerup> getPowerups();
	
	
	/**
	 * @return the team the player belongs to
	 */
	public abstract ITeam getTeam();
	
	
	/**
	 * @return whether the player is alive or dead
	 */
	public abstract boolean isDead();

}
