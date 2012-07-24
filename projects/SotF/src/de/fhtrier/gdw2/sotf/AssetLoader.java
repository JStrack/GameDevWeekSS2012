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
	Animation playerAnimation;
	SpriteSheet playerSheet;
	
	public AssetLoader() throws SlickException {
		initializeAssets();
		
	}
	
	private void initializeAssets() throws SlickException{
		//Maps anlegen
		maps.put(0, "Testmap.tmx");
		playerSheet = new SpriteSheet("res/animations/character_top.png", 130, 120);
		playerAnimation = new Animation();
		playerAnimation.setAutoUpdate(true);
		for(int col=0 ; col<15 ; ++col){
			playerAnimation.addFrame(playerSheet.getSprite(col, 0), 80);
		}
		//Image sideView = new Image("res/animations/character_side.png");
		//playerAnimation.addFrame(sideView, 300);
		}
	
	public Sound getCollideSound(){
		return null;
	}
	
	public Sound getEatingSound(){
		return null;
	}
	/**
	 * 
	 * Lädt eine Map und gibt sie zurück.
	 * @author <Torsten Scholer>
	 * @param  stage - der index des levels
	 */
	public TiledMap loadMap(int stage) throws SlickException{
		map = new TiledMap("res/maps/" + maps.get(stage) , "res/maps/");
		return map;
	}
	
	public Animation getAnimation(ITeam team){
		return playerAnimation;
	}
}
