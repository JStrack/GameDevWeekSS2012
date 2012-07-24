package de.fhtrier.gdw.sotf.states;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.EmptyTransition;

import de.fhtrier.gdw2.sotf.SlickTestGameState;
import de.fhtrier.gdw2.sotf.Interfaces.IActionListener;
import de.fhtrier.gdw2.sotf.menu.Button;
import de.fhtrier.gdw2.sotf.menu.MenuPage;

/**
 * Menu state
 * 
 * @author Lusito
 */
public class MainMenuState extends BasicGameState {
	
	int stateID = -1;
	
	private Font font;
	MenuPage rootPage;
	MenuPage currentPage;
	
	public MainMenuState(int stateID) {
		this.stateID = stateID;
	}

	@Override
	public void init(final GameContainer container, final StateBasedGame game)
			throws SlickException {
		font = new AngelCodeFont("res/demo2.fnt","res/demo2_00.tga");

		rootPage = currentPage = new MenuPage(container, game, this, null);
		
		float x = 100;
		float y = 200;
		float h = font.getLineHeight() * 1.2f;
		addLeftAlignedButton(container, "Create a Game", x, y - h * 2,
				new IActionListener() {
					public void onAction() {
						game.enterState(SlickTestGameState.GAMEPLAYSTATE, new EmptyTransition(), new EmptyTransition());
					}
				});
		addLeftAlignedButton(container, "Join a Game", x, y - h * 1,
				new IActionListener() {
			public void onAction() {
				System.out.println("Join not present yet");
			}
		});
		addLeftAlignedButton(container, "Options", x, y,
				new IActionListener() {
			public void onAction() {
				System.out.println("Options not present yet");
			}
		});
		addLeftAlignedButton(container, "Help", x, y + h * 1,
				new IActionListener() {
			public void onAction() {
				System.out.println("Help not present yet");
			}
		});
		addLeftAlignedButton(container, "Exit", x, y + h * 2,
				new IActionListener() {
			public void onAction() {
				System.exit(0); // todo
			}
		});
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
	
	
	public void setPage(MenuPage page) {
		if(page == null) {
			currentPage = rootPage;
			// close menu/switch to game state?
		} else {
			currentPage = page;
		}
	}

	public void addCenteredButton(GameContainer container, final String text, float x, float y, IActionListener listener) throws SlickException {
		float w = font.getWidth(text);
		float h = font.getHeight(text);

		Button button = Button.create(text, x - w/2, y - h/2, w, h)
			.font(font)
			.color(Color.gray)
			.hoverColor(Color.white)
			.pressedColor(Color.red)
			.action(listener);
		currentPage.addWidget(button);
		button.init(container);
	}
	
	public void addLeftAlignedButton(GameContainer container, final String text, float x, float y, IActionListener listener) throws SlickException {
		float w = font.getWidth(text);
		float h = font.getHeight(text);

		Button button = Button.create(text, x, y, w, h)
			.font(font)
			.color(Color.gray)
			.hoverColor(Color.white)
			.pressedColor(Color.red)
			.action(listener);
		currentPage.addWidget(button);
		button.init(container);
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