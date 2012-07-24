package de.fhtrier.gdw2.sotf.Collision;

import java.util.ArrayList;

import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.tiled.TiledMap;

import de.fhtrier.gdw2.sotf.Interfaces.IEntity;
import de.fhtrier.gdw2.sotf.Interfaces.IPlayer;

/**
 * Klasse zur Abfrage auf Kollisionen.
 * 
 * @author stefanprobst
 */
public class CollisionHandler {
	
	/**
	 * Enthält die Collision-Map.
	 */
	private boolean[][] blocked;
	
	/**
	 * Erstellt aus der TileMap ein zweidimensionales
	 * Array ueber das schnell abgefragt werden kann,
	 * ob ein Kollision mit einem Tile aufgetreten ist.
	 * 
	 * @param map
	 */
	CollisionHandler(TiledMap map) {
		blocked = new boolean[map.getWidth()][map.getHeight()];
		
		for (int xAxis = 0; xAxis < map.getWidth(); xAxis++) {
			for (int yAxis = 0; yAxis < map.getHeight(); yAxis++) {
				int tileID = map.getTileId(xAxis, yAxis, 0);
				String value = map.getTileProperty(tileID, "blocked", "false");
				if ("true".equals(value)) {
					blocked[xAxis][yAxis] = true;
				}
			}
		} 
	}
	
	/**
	 * Abfragen ob ein Entity mit irgendeinem
	 * der Entities kollidiert. Wenn eine Kollision
	 * aufgetreten ist, wird das kollidierte Entity zurückgegeben.
	 * 
	 * @param entity
	 * @param entities
	 * @return boolean
	 */
	public IEntity processCollisions(IEntity entity, ArrayList<IEntity> entities) {
		for (IEntity e : entities) {
			if (entity.getShape().intersects(e.getShape()) && entity.getID() != e.getID()) {
				return e;
			}
		}
		
		return null;
	}
	
	/**
	 * Gibt zurück ob ein Tile begehbar ist.
	 * 
	 * @param player
	 * @param map
	 * @return boolean
	 */
	public boolean isTileBlocked(IPlayer player, TiledMap map) {
		Vector2f position = player.getPosition();
		
		int xBlock = (int)position.x / 24;
		int yBlock = (int)position.y / 24;
		return blocked[xBlock][yBlock];
	}
}