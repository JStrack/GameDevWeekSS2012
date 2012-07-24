package de.fhtrier.gdw2.sotf.menu;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

import de.fhtrier.gdw.sotf.states.MainMenuState;

public class MenuPage {
	private Widget focus;
	private List<Widget> widgets = new ArrayList<Widget>();
	private MenuPage parent;
	GameContainer container;
	StateBasedGame game;
	MainMenuState menuState;
	
	
	public MenuPage(GameContainer container, StateBasedGame game, MainMenuState menuState, MenuPage parent) {
		this.container = container;
		this.game = game;
		this.menuState = menuState;
		this.parent = parent;
	}
	
	public void addWidget(Widget w) {
		widgets.add(w);
	}
	
	public void render(Graphics g) {
		for(Widget w: widgets)
			w.render(g);
	}
	
	public void close() {
		menuState.setPage(parent);
	}
	
	public void keyReleased(int key, char c) {
		if (key == Input.KEY_ESCAPE) {
			// close menu / switch to parent menu
		}
		
		if(focus != null)
			focus.keyReleased(key, c);
	}
	
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		for(Widget w: widgets) {
			w.mouseMoved(oldx, oldy, newx, newy);
		}
	}

	public void mouseReleased(int button, int x, int y) {
		for(Widget w: widgets) {
			w.mouseReleased(button, x, y);
		}
	}
	
	public void mousePressed(int button, int x, int y) {
		for(Widget w: widgets) {
			w.mousePressed(button, x, y);
			if(w.contains(x, y))
				focus = w;
		}
	}
}
