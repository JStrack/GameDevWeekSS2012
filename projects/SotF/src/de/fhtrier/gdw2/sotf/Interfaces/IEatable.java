package de.fhtrier.gdw2.sotf.Interfaces;

import java.util.List;

/**
 * Interface zur Verwaltung eines Eatable.
 * 
 * @author Kevin Korte
 * @author Attila Djerdj
 * @author Stefan Probst
 */
public interface IEatable extends IEntity
{

    /**
     * Liefert eine Liste vom Typ IPowerups mit den Powerups die auf den Spieler
     * wirken, wenn er das Item isst.
     * 
     * @return List<IPowerups>
     */
    public List<IPowerups> getPowerups();

    /**
     * Liefert das Useable zurück falls das Eatable ein Useable enthält.
     * 
     * @return IUseable
     */
    public IUseable getUseable();

    /**
     * Liefert zurück um wieviel der Spieler wächst.
     * 
     * @return float
     */
    public float getEnergy();
}
