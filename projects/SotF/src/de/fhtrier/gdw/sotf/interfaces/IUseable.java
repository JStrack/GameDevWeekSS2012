package de.fhtrier.gdw.sotf.interfaces;

import java.util.List;

/**
 * Interface für die benutzbaren Items im Inventar.
 * 
 * @author Kevin Korte
 * @author Attila Djerdj
 * @author Stefan Probst
 */
public interface IUseable {
	
	/**
	 * Liefert eine Liste vom Typ IPowerups
	 * mit den Powerups die der Spieler wirken kann.
	 * 
	 * @return List<IPowerups>
	 */
	public List<IPowerups> getPowerups();
	
	/**
	 * Gibt an ob der Gegenstand vom 
	 * Spieler fallen gelassen werden kann.
	 * 
	 * @return boolean
	 */
	public boolean isDropable();
}
