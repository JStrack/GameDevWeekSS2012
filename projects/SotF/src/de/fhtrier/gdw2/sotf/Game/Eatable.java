package de.fhtrier.gdw2.sotf.Game;

import java.util.List;

import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

import de.fhtrier.gdw2.sotf.Interfaces.IEatable;
import de.fhtrier.gdw2.sotf.Interfaces.IPowerups;
import de.fhtrier.gdw2.sotf.Interfaces.IUseable;

/**
 * Ein Eatable
 * @author Kevin Korte
 *
 */
public class Eatable extends Entity implements IEatable {

	private float energy;
	private List<IPowerups> powerupsList;
	private IUseable useable;
	
	/**
	 * Der Konstrukter eines Eatables
	 * 
	 * @param position
	 * @param radius
	 * @param id
	 * @param image
	 * @param energy
	 * @param powerupsList
	 * @param useable
	 */
	public Eatable(Vector2f position, float radius, Image image,
				float energy, List<IPowerups> powerupsList, IUseable useable) {
		super(position, radius, image);
		this.energy = energy;
		this.powerupsList = powerupsList;
		this.useable = useable;
	}

	/**
	 * gibt die Liste der, im Eatable beinhaltenden, Powerups,
	 * die der Spieler erhält, zurück
	 * 
	 * @return List<IPowerups>
	 */
	public List<IPowerups> getPowerups() {
		return powerupsList;
	}

	/**
	 * gibt das Useable zurück, welches der Spieler erhält,
	 * wenn dieser das Eatable isst
	 * 
	 * @return IUseable
	 */
	public IUseable getUseable() {
		return useable;
	}

	/**
	 * gibt die Energie zurück, die der Spieler bekommt,
	 * falls dieser das Eatable isst
	 * 
	 * @return float
	 */
	public float getEnergy() {
		return energy;
	}
}
