package de.fhtrier.gdw2.sotf.Game;

import java.util.List;

import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

import de.fhtrier.gdw2.sotf.Interfaces.*;

public class Player extends Entity implements IPlayer{
	
	private Vector2f velocity;
	private IUseable[] inventory;
	private List<IPowerups> powerups;
	private ITeam team;

	public Player(Vector2f position, float radius, int id, Image image) {
		super(position, radius, id, image);
	}

	@Override
	public void setVelocity(Vector2f velo) {
		this.velocity = velo;
	}

	@Override
	public Vector2f getVelocity() {
		return this.velocity;
	}

	@Override
	public void eat(IEatable eat) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IUseable use(int number) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IUseable[] getInventory() {
		return this.inventory;
	}

	@Override
	public List<IPowerups> getPowerups() {
		return this.powerups;
	}

	@Override
	public ITeam getTeam() {
		return this.team;
	}

	@Override
	public boolean isDead() {
		return this.getRadius() <= 0; // woher kommt die MinSize?
	}

}
