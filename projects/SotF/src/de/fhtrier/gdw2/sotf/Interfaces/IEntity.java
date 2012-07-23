package de.fhtrier.gdw2.sotf.Interfaces;

import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.*;

public interface IEntity {
	
	
	/**
	 * @return the current position of the player as Vector
	 */
	public abstract Vector2f getPosition();
	
	
	/**
	 * @param time milliseconds passed since last update
	 */
	public abstract void update(int time);
	
	
	/**
	 * @return the current entity's radius
	 */
	public abstract float getRadius();
	
	
	/**
	 * @return the entity's ID
	 */
	public abstract int getID();
	
	
	/**
	 * @param g the graphics context the entity has to be drawn in
	 */
	public abstract void render(Graphics g);

}
