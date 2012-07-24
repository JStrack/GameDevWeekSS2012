package de.fhtrier.gdw2.sotf.AssertLoader;

import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.tiled.TiledMap;

import de.fhtrier.gdw2.sotf.Interfaces.ITeam;

public class AssetLoader
{

    ArrayList<Sound> sounds = new ArrayList<Sound>();

    ArrayList<Image> images = new ArrayList<Image>();

    ArrayList<Animation> animations = new ArrayList<Animation>();

    HashMap<Integer, String> maps = new HashMap<Integer, String>(10);

    TiledMap map;

    public AssetLoader()
    {
        initializeAssets();

    }

    private void initializeAssets()
    {
        maps.put(0, "Testmap.tmx");
    }

    public Sound getCollideSound()
    {
        return null;
    }

    public Sound getEatingSound()
    {
        return null;
    }

    /**
     * 
     * Lädt eine Map und gibt sie zurück.
     * 
     * @author <Torsten Scholer>
     * @param stage
     *            - der index des levels
     */
    public TiledMap loadMap(int stage) throws SlickException
    {
        map = new TiledMap("res/" + maps.get(stage), "res/");
        return map;
    }

    public Image getCharacter(ITeam team)
    {
        return null;
    }
}
