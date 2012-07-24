package de.fhtrier.gdw2.sotf.Interfaces;

import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.*;

public interface IEntity
{

    /**
     * @return the current position of the player as Vector
     */
    public Vector2f getPosition();

    /**
     * @param time
     *            ??????
     */
    public void update(GameContainer gamecontainer, int deltaTime);

    /**
     * @return the current entity's radius
     */
    public float getRadius();

    /**
     * @return the entity's ID
     */
    public int getID();

    /**
     * @param g
     *            the graphics context the entity has to be drawn in
     */
    public void render(Graphics g);
}
