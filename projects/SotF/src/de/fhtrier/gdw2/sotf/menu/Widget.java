package de.fhtrier.gdw2.sotf.menu;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

/**
 * Menu widget
 * 
 * @author Lusito
 */
public abstract class Widget {
	public void keyReleased(int key, char c) {
	}
	
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
	}
	
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
	}

	public void mouseReleased(int button, int x, int y) {
	}
	
	public void mousePressed(int button, int x, int y) {
	}
	
	public abstract boolean contains(int x, int y);
	public abstract void init(GameContainer container) throws SlickException;
	public abstract void render(Graphics g);
}
