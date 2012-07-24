package de.fhtrier.gdw2.sotf.Interfaces;

import org.newdawn.slick.geom.Vector2f;

import java.util.List;

public interface IPlayer extends IEntity
{

    /**
     * @param velo
     *            the Vector that specifies the new velocity of the player
     */
    public void setVelocity(Vector2f velo);

    /**
     * @return the current velocity of the player
     */
    public Vector2f getVelocity();

    /**
     * @param eat
     *            the Eatable item that affects the player
     */
    public void eat(IEatable eat);

    /**
     * @param number
     *            the number of the Usable in the player's Inventory
     * @return the Usable item that is to be placed on the world
     */
    public IUseable use(int number);

    /**
     * @return the Inventory of the player as an array of Useables
     */
    public IUseable[] getInventory();

    /**
     * @return list of Powerups that are currently active on the player
     */
    public List<IPowerups> getPowerups();

    /**
     * @return the team the player belongs to
     */
    public ITeam getTeam();

    /**
     * @return whether the player is alive or dead
     */
    public boolean isDead();

}
