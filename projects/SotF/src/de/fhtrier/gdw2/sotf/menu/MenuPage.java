package de.fhtrier.gdw2.sotf.menu;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import de.fhtrier.gdw2.sotf.states.MainMenuState;
import de.fhtrier.gdw2.sotf.Interfaces.IActionListener;

/**
 * Menu page
 * 
 * @author Lusito
 */
public class MenuPage {
	protected Widget focus;
	protected List<Widget> widgets = new ArrayList<Widget>();
	protected MenuPage parent;
	GameContainer container;
	StateBasedGame game;
	MainMenuState menuState;
	Image bgImage;
	
	
	public MenuPage(GameContainer container, StateBasedGame game, MainMenuState menuState, MenuPage parent, String bgImage) throws SlickException {
		this.container = container;
		this.game = game;
		this.menuState = menuState;
		this.parent = parent;
		this.bgImage = new Image(bgImage);
	}
	
	public void addWidget(Widget w) {
		widgets.add(w);
	}
	
	public void render(Graphics g) {
		bgImage.draw(0, 0);
		for(Widget w: widgets)
			w.render(g);
	}
	
	public void close() {
		menuState.setPage(parent);
	}
	

	public void addCenteredButton(final String text, float x, float y, Font font, IActionListener listener) throws SlickException {
		float w = font.getWidth(text);
		float h = font.getHeight(text);

		Button button = Button.create(text, x - w/2, y - h/2, w, h)
			.font(font)
			.color(Color.gray)
			.hoverColor(Color.white)
			.pressedColor(Color.red)
			.action(listener);
		addWidget(button);
		button.init(container);
	}
	
	public void addLeftAlignedButton(final String text, float x, float y, Font font, IActionListener listener) throws SlickException {
		float w = font.getWidth(text);
		float h = font.getHeight(text);

		Button button = Button.create(text, x, y, w, h)
			.font(font)
			.color(Color.gray)
			.hoverColor(Color.white)
			.pressedColor(Color.red)
			.action(listener);
		addWidget(button);
		button.init(container);
	}

	
	public void keyReleased(int key, char c) {
		if (key == Input.KEY_ESCAPE) {
			close();
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
