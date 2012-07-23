package de.fhtrier.gdw2.sotf;

import java.util.List;

public interface ITeam {
 
	/**
	 * 
	 * @return
	 * Returns a List of all Players in the Team.
	 */
	public List<IPlayer> getPlayers();

	/**
	 * 
	 * @return
	 * Returns a List of all Players in the Team that are alive.
	 */
	public List<IPlayer> getAlivePlayers();

	/**
	 * 
	 * @return
	 * Returns the ID of the current Player.
	 */
	public int getID();
	
	/**
	 * 
	 * @return
	 * Returns a boolean. true, if the Teambuff is active, else false.
	 */
	public boolean isTeamBuffActive();
}
