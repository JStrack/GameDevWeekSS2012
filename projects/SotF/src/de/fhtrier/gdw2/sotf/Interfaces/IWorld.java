package de.fhtrier.gdw2.sotf.Interfaces;

import java.util.List;

public interface IWorld
{
    /**
     * 
     * @return Returns a List of all Entities in the World.
     */
    public List<IEntity> getEntities();

    /**
     * Deletes the given Entity from the World.
     */
    public void delete(IEntity entity);

    /**
     * @return Adds the given Entity to the World-List
     * 
     */
    public void add(IEntity entity);

}
