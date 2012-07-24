package de.fhtrier.gdw2.sotf.Game;

/**
 * @author Ben Raus
 * @author Wendelin Lehr
 * @author Susanne Kühn
 */
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

import de.fhtrier.gdw2.sotf.Interfaces.*;

public class Player extends Entity implements IPlayer, IEatable {

	private Vector2f velocity;
	private IUseable[] inventory;
	private List<IPowerups> powerups;
	private ITeam team;
	private Animation animation;

	
	public Player(Vector2f position, float radius, int id, ITeam team, Animation animation) {
		super(position, radius, id, null);
		
		this.animation = animation;
		this.team = team;
		this.inventory = new IUseable[1];
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
	public void update(GameContainer gameContainetr, int time) {

		// Duration der Power-ups anpassen/prüfen
		for (int i = 0; i <= this.powerups.size(); i++) {
			this.powerups.get(i).setDuration(
					this.powerups.get(i).getDuration() - time);

			// abgelaufene Powerups entfernen
			if (this.powerups.get(i).getDuration() <= 0) {
				
				// Effekt entfernen
				switch (this.powerups.get(i).getType()) {
				case SPEED:
					this.velocity.scale(1/this.powerups.get(i).getValue());	// prozentualer Wert zur Skalierung erwartet
					break;
				case IMMORTAL:	// es werden keine Attribute des Players verändert -> Prüfung erolgt in isEatable()-Methode
					break;
				case SIGHT:
					break;
				}
				
				// Powerup aus Liste löschen
				this.powerups.remove(i);
			}
		}

	}

	/**
	 * Liefert zurück, ob der Spieler grade gegessen werden kann oder
	 * immortal-Effekt auf den Spieler zutreffen
	 * 
	 * @return
	 */
	public boolean isEatable() {
		for (int i = 0; i < this.powerups.size(); i++) {
			if (this.powerups.get(i).getType() == IPowerups.PowerupType.IMMORTAL)
				return false;
		}
		return true;
	}

	@Override
	public void eat(IEatable eat) {

		// Wachsen des Spielers
		if(this.getRadius() + eat.getEnergy() > Constants.MAX_SIZE)	
			this.setRadius(Constants.MAX_SIZE);
		else
			this.setRadius(this.getRadius() + eat.getEnergy());

		// Usable zu Inventar hinzufügen
		IUseable use = eat.getUseable();
		if (use != null) {
			//@TODO hhh
		}

		// Powerups hinzufügen und Effekt anwenden
		List<IPowerups> plist = eat.getPowerups();
		if (plist != null) {
			for (int i = 0; i < plist.size(); i++) {
				// Powerup zu Liste hinzufügen
				this.powerups.add(plist.get(i));

				// Effekt auf Spieler anwenden
				switch (plist.get(i).getType()) {
				case SPEED:
					this.velocity.scale(plist.get(i).getValue());	// prozentualer Wert zur Skalierung erwartet
					break;
				case IMMORTAL:	// es werden keine Attribute des Players verändert -> Prüfung erolgt in isEatable()-Methode
					break;
				case SIGHT:  
					break;
				}
			}
		}

	}

	@Override
	public IUseable use(int number) {

		if (number >= 0 && number < this.inventory.length)
			return this.inventory[number];
		else
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
		return this.getRadius() < Constants.MIN_SIZE; 
	}

	@Override
	public IUseable getUseable() {
		return null;
	}

	@Override
	public float getEnergy() {
		return this.getRadius();	// Frage an das Game-Design: Faktor?
	}
	
	@Override
	public void render(Graphics g)
	{
		// TODO Animation anzeigen
	}

}