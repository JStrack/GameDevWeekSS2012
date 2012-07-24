package de.fhtrier.gdw2.sotf.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import de.fhtrier.gdw2.sotf.menu.MenuPage;
import de.fhtrier.gdw2.sotf.menu.pages.MenuPageRoot;

/**
 * Menu state
 * 
 * @author Lusito
 */
public class MainMenuState extends BasicGameState {
	
	int stateID = -1;
	
	MenuPage rootPage;
	MenuPage currentPage;
	
	public MainMenuState(int stateID) {
		this.stateID = stateID;
	}

	@Override
	public void init(final GameContainer container, final StateBasedGame game)
			throws SlickException {
		rootPage = currentPage = new MenuPageRoot(container, game, this);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		g.clear();
		g.setBackground(Color.black);
		if(currentPage != null) {
			currentPage.render(g);
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
	}

	@Override
	public int getID() {
		return stateID;
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		setPage(null);
	}

	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException {
	}

	public void setPage(MenuPage page) {
		if(page == null) {
			currentPage = rootPage;
			// close menu/switch to game state?
		} else {
			currentPage = page;
		}
	}

	public void keyReleased(int key, char c) {
		if(currentPage != null)
			currentPage.keyReleased(key, c);
	}
	
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		if(currentPage != null)
			currentPage.mouseMoved(oldx, oldy, newx, newy);
	}

	public void mouseReleased(int button, int x, int y) {
		if(currentPage != null)
			currentPage.mouseReleased(button, x, y);
	}
	
	public void mousePressed(int button, int x, int y) {
		if(currentPage != null)
			currentPage.mousePressed(button, x, y);
	}
}