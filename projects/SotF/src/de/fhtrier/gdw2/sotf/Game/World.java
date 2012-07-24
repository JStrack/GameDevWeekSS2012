package de.fhtrier.gdw2.sotf.Game;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

import de.fhtrier.gdw2.sotf.AssertLoader.AssetLoader;
import de.fhtrier.gdw2.sotf.Interfaces.IEatable;
import de.fhtrier.gdw2.sotf.Interfaces.IEntity;
import de.fhtrier.gdw2.sotf.Interfaces.IPlayer;
import de.fhtrier.gdw2.sotf.Interfaces.IUseable;

/**
 * World zeichnet die Welt und verwaltet auch die Entitys, World ist also der Gamecontainer
 * @author Robin Dick
 */
public class World {
	
	/**
	 * enthält alle Spieler in der Welt
	 */
	private ArrayList<IPlayer> players;
	/**
	 * enthält alle essbaren Items in der Welt
	 */
	private ArrayList<IEatable> eatables;
	/**
	 * enthält alle benutzbaren Items in der Welt
	 */
	private ArrayList<IUseable> useables;
	/**
	 * enthält alle Entities
	 */
	private ArrayList<IEntity> entities;
	
	/**
	 * enthält den AssetLoader
	 */
	private AssetLoader assetLoader;
	
	/**
	 * enthält die darzustellende TiledMap
	 */
	private TiledMap map;
	
	/**
	 * Erstellt eine neue Welt
	 * @throws SlickException 
	 */
	public World() throws SlickException {
		players = new ArrayList<IPlayer>();
		eatables = new ArrayList<IEatable>();
		useables = new ArrayList<IUseable>();
		assetLoader= new AssetLoader();
		map=assetLoader.loadMap(0);
	}
	
	/**
	 * Rendert alle Entitys und die Welt
	 * @param gc GameContainer der von Slick vorgegeben wird
	 * @param g Graphics welches von Slick vorgegeben wird
	 */
	public void render(GameContainer gc, Graphics g)
			throws SlickException {
		map.render(0, 0);
		
		for (IEntity e : entities) {
			e.render(g);
		}
	}

	/**
	 * Aktualisiert die Map und alle Entitys in ihr
	 * @param gc GameContainer der von Slick vorgegeben wird
	 * @param delta Die Anzahl der Millisekunden seit dem letzten Update
	 */
	public void update(GameContainer gc, int delta)
			throws SlickException {
		for (IEntity e : entities) {
			e.update(gc, delta);
		}
	}
	
	/**
	 * Gibt die Spieler in der Welt zurück
	 * @return IPlayer in der Welt
	 */
	public ArrayList<IPlayer> getPlayer() {
		return players;
	}
	
	/**
	 * Gibt die Eatables in der Welt zurück
	 * @return IEatables in der Welt
	 */
	public ArrayList<IEatable> getEatables() {
		return eatables;
	}
	
	/**
	 * Gibt die Useables in der Welt zurück
	 * @return IUseables in der Welt
	 */
	public ArrayList<IUseable> getUseables() {
		return useables;
	}
	
	/**
	 * Gibt alle IEntities aus der Welt zurück
	 * @return alle IEntities aus der Welt
	 */
	public ArrayList<IEntity> getEntities() {
		return entities;
	}
	
	/**
	 * Einen Spieler zur Welt hinzufügen
	 * @param e Das Player-Entity
	 */
	public void add(IPlayer e) {
		players.add(e);
		entities.add(e);
	}
	
	/**
	 * Einen Spieler aus der Welt entfernen
	 * @param e Das Player-Entity
	 */
	public void remove(IPlayer e) {
		players.remove(e);
		entities.remove(e);
	}
	
	/**
	 * Ein Eatable zur Welt hinzufügen
	 * @param e Das Eatable-Entity
	 */
	public void add(IEatable e) {
		eatables.add(e);
		entities.add(e);
	}
	
	/**
	 * Ein Eatable aus der Welt entfernen
	 * @param e Das Eatable-Entity
	 */
	public void remove(IEatable e) {
		eatables.remove(e);
		entities.remove(e);
	}
	
	/**
	 * Ein Useable zur Welt hinzufügen
	 * @param e Das Useable-Entity
	 */
	public void add(IUseable e) {
		useables.add(e);
		entities.add(e);
	}
	
	/**
	 * Ein Useable aus der Welt entfernen
	 * @param e Das Useable-Entity
	 */
	public void remove(IUseable e) {
		useables.remove(e);
		entities.remove(e);
	}

	/**
	 * Gibt den AssetLoader der Welt zurück
	 * @return AssetLoader der Welt
	 */
	public AssetLoader getAssetLoader() {
		return assetLoader;
	}
}
