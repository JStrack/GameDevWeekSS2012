package de.fhtrier.gdw2.sotf;

import java.util.List;

public interface IWorld {
/**
 * 
 * @return
 * Returns a List of all Entities in the World.
 */
	public List<IEntity> getEntities();
	
	/**
	 * Deletes the given Entity from the World.
	 * @param
	 * Entity that will be removed
	 */
	public void delete(IEntity);
	
	/**
	 * @param
	 * Entity that will be  added
	 * @return
	 * Adds the given Entity to the World-List
	 * 
	 */
	public void add(IEntity);
	
	
}
