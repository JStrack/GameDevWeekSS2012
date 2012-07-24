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
     * Liefert das Useable zur�ck falls das Eatable ein Useable enth�lt.
     * 
     * @return IUseable
     */
    public IUseable getUseable();

    /**
     * Liefert zur�ck um wieviel der Spieler w�chst.
     * 
     * @return float
     */
    public float getEnergy();
}
