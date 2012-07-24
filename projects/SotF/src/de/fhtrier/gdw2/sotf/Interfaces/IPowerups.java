package de.fhtrier.gdw2.sotf.Interfaces;

/**
 * Interface zur Verwaltung der Powerups.
 * 
 * @author Kevin Korte
 * @author Attila Djerdj
 * @author Stefan Probst
 */
public interface IPowerups
{

    enum PowerupType
    {
        SPEED, IMMORTAL, SIGHT
    }

    /**
     * Liefert den Typ des Powerups.
     * 
     * @return PowerupType
     */
    public PowerupType getType();

    /**
     * Liefert einen Wert zurück um welchen Faktor das Powerup den akutellen
     * Wert verändert.
     * 
     * @return float
     */
    public float getValue();

    /**
     * Liefert die Dauer des Powerups.
     * 
     * @return int
     */
    public int getDuration();

    /**
     * Legt die Dauer des Powerups fest.
     * 
     * @param duration
     */
    public void setDuration(int duration);
}
