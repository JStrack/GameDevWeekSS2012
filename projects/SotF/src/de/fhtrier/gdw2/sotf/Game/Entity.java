package de.fhtrier.gdw2.sotf.Game;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.Image;

import de.fhtrier.gdw2.sotf.Interfaces.*;

public class Entity implements IEntity
{

    private Vector2f position;

    private float radius;

    private int id;

    private Image image;

    // unsichtbares Polyon für Kollisionerkennung
    private Circle shape;
	private static int eid = 0;

	
	public Entity(Vector2f position, float radius, Image image)
    {
        this.position = position;
        this.radius = radius;
		this.id = eid++;
        this.image = image;

        // Shape zur Kollisionserkennung erzeugen
        this.shape = new Circle(this.position.getX(), this.position.getY(), this.radius);
    }

    public void setPosition(Vector2f pos)
    {
        this.position = pos;
        this.shape.setLocation(pos);
    }

    public void setPosition(float x, float y)
    {
        this.position.set(x, y);
        this.shape.setLocation(x, y);
    }

    @Override
    public void update(GameContainer gameContainer, int time)
    {
    }

    @Override
    public float getRadius()
    {
        return this.radius;
    }

    public void setRadius(float r)
    {
        this.radius = r;
        this.shape.setRadius(this.radius);
    }

    @Override
    public Vector2f getPosition()
    {
        return this.position;
    }

    @Override
    public int getID()
    {
        return this.id;
    }

    @Override
    public void render(Graphics g)
    {
        g.drawImage(this.image, this.position.getX() - this.radius, this.position.getY() - this.radius, this.position.getX() + this.radius * 2, this.position.getY() + this.radius * 2, 0, 0, this.image.getHeight(), this.image.getWidth());
    }

    @Override
    public Circle getShape()
    {
        return this.shape;
    }

}
