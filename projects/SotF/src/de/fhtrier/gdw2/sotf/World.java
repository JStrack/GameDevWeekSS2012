package de.fhtrier.gdw2.sotf;

import java.util.List;

import org.newdawn.slick.tiled.TiledMap;

/**
 * The World-Class
 * @author Wendelin
 * @param <entities>
 *
 */
public class World<entities> implements IWorld {

	private TiledMap map;
	List<ITeam> teams;
	List<IEntity> entities;
	
	
	World(String mapName)
	{
		map = new TiledMap("res/" + mapName);
		//Enthaelt mapName die Datei-Endung?	
		//weitere Initialisierung im Constructor?
	}
	
	
	@Override
	public List<IEntity> getEntities() {
		return entities;
	}

	@Override
	public void delete(IEntity entity) {
		entities.remove(entity);		
	}

	@Override
	public void add(IEntity entity) {
		entities.add(entity);
		
	}
	

}
