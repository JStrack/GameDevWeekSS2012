package de.fhtrier.gdw2.sotf.Game;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

import de.fhtrier.gdw2.sotf.Interfaces.IEatable;
import de.fhtrier.gdw2.sotf.Interfaces.IEntity;
import de.fhtrier.gdw2.sotf.Interfaces.IPlayer;
import de.fhtrier.gdw2.sotf.Interfaces.IUseable;

/**
 * World zeichnet die Welt und verwaltet auch die Entitys, World ist also der Gamecontainer
 * @author Robin Dick
 */
public class World {
	
	private ArrayList<IPlayer> players;
	private ArrayList<IEatable> eatables;
	private ArrayList<IUseable> useables;
	private ArrayList<IEntity> entitys;
	
	private TiledMap map;
	
	/**
	 * Erstellt eine neue Welt
	 */
	public World() {
		players = new ArrayList<IPlayer>();
		eatables = new ArrayList<IEatable>();
		useables = new ArrayList<IUseable>();
		entitys = new ArrayList<IEntity>();
	}
	
	/**
	 * Rendert alle Entitys und die Welt
	 * @param gc GameContainer der von Slick vorgegeben wird
	 * @param g Graphics welches von Slick vorgegeben wird
	 */
	public void render(GameContainer gc, Graphics g)
			throws SlickException {
		map.render(0, 0);
		
		for (IEntity e : entitys) {
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
		for (IEntity e : entitys) {
			e.update(gc, delta);
		}
	}
	
	/**
	 * Einen Spieler zur Welt hinzufügen
	 * @param e Das Player-Entity
	 */
	public void add(IPlayer e) {
		players.add(e);
		entitys.add(e);
	}
	
	/**
	 * Einen Spieler aus der Welt entfernen
	 * @param e Das Player-Entity
	 */
	public void remove(IPlayer e) {
		players.remove(e);
		entitys.remove(e);
	}
	
	/**
	 * Ein Eatable zur Welt hinzufügen
	 * @param e Das Eatable-Entity
	 */
	public void add(IEatable e) {
		eatables.add(e);
		entitys.add(e);
	}
	
	/**
	 * Ein Eatable aus der Welt entfernen
	 * @param e Das Eatable-Entity
	 */
	public void remove(IEatable e) {
		eatables.remove(e);
		entitys.remove(e);
	}
	
	/**
	 * Ein Useable zur Welt hinzufügen
	 * @param e Das Useable-Entity
	 */
	public void add(IUseable e) {
		useables.add(e);
		entitys.add(e);
	}
	
	/**
	 * Ein Useable aus der Welt entfernen
	 * @param e Das Useable-Entity
	 */
	public void remove(IUseable e) {
		useables.remove(e);
		entitys.remove(e);
	}
}
