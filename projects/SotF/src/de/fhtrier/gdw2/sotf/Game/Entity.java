package de.fhtrier.gdw2.sotf.Game;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.Image;

import de.fhtrier.gdw2.sotf.Interfaces.*;

public class Entity implements IEntity
{

    private Vector2f position;

    private float radius;

    private int id;

    private Image image;

    public Entity(Vector2f position, float radius, int id, Image image)
    {
        this.position = position;
        this.radius = radius;
        this.id = id;
        this.image = image;
    }

    @Override
    public Vector2f getPosition()
    {
        return this.position;
    }

    @Override
    public void update(GameContainer gameContainer, int time)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public float getRadius()
    {
        return this.radius;
    }

    @Override
    public int getID()
    {
        return this.id;
    }

    @Override
    public void render(Graphics g)
    {
        g.drawImage(this.image, this.position.getX() - this.radius, this.position.getX() - this.radius, this.radius * 2, this.radius * 2, 0, 0, this.image.getHeight(), this.image.getWidth());
    }

}
