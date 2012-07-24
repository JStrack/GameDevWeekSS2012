package de.fhtrier.gdw2.sotf;

import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.tiled.TiledMap;

import de.fhtrier.gdw2.sotf.Interfaces.ITeam;

public class AssetLoader {

	ArrayList<Sound> sounds = new ArrayList<Sound>();
	ArrayList<Image> images = new ArrayList<Image>();
	ArrayList<Animation> animations = new ArrayList<Animation>();
	HashMap<Integer, String> maps = new HashMap<Integer, String>(10);
	TiledMap map;
	Animation playerTop, playerSide;
	SpriteSheet playerSheet;

	public AssetLoader() throws SlickException {
		initializeAssets();

	}

	private void initializeAssets() throws SlickException {
		// Maps anlegen
		maps.put(0, "Testmap.tmx");
		// Sheet für die Bilder der Animation anlegen + Bilder zur Animation
		// hinzufügen
		playerSheet = new SpriteSheet("res/animations/character_top.png", 130,
				120);
		playerTop = new Animation();
		for (int col = 0; col < 15; ++col) {
			playerTop.addFrame(playerSheet.getSprite(col, 0), 80);
		}
		
		playerSide= new Animation();
		playerSide.addFrame(new Image("res/animations/character_side.png"), 80);
		
	}
	
	/**
	 * 
	 * Gibt einen Sound zurück der eine Kollision darstellt
	 * @author Torsten Scholer
	 * @param none
	 */
	public Sound getCollideSound() {
		return null;
	}
	
	/**
	 * 
	 * Gibt einen Sound zurück der ein Essgeräusch darstellt
	 * @author Torsten Scholer
	 * @param none
	 */
	public Sound getEatingSound() {
		return null;
	}

	/**
	 * 
	 * Lädt eine Map und gibt sie zurück.
	 * 
	 * @author <Torsten Scholer>
	 * @param stage - der index des levels
	 */
	public TiledMap loadMap(int stage) throws SlickException {
		map = new TiledMap("res/maps/" + maps.get(stage), "res/maps/");
		return map;
	}

	/**
	 * 
	 * Gibt die Top-Animation eines Characters basierend auf seiner Teamzugehörigkeit zurück
	 * @author Torsten Scholer
	 * @param Iteam team - Das Team, dem der Spieler angehört
	 */
	public Animation getTopAnimation(ITeam team) {
		return playerTop;
	}
	
	/**
	 * 
	 * Gibt die Side-Animation eines Characters basierend auf seiner Teamzugehörigkeit zurück
	 * @author Torsten Scholer
	 * @param Iteam team - Das Team, dem der Spieler angehört
	 */
	public Animation getSideAnimation(ITeam team) {
		return  playerSide;
	}
}
