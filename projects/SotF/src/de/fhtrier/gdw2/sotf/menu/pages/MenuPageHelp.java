package de.fhtrier.gdw2.sotf.menu.pages;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import de.fhtrier.gdw2.sotf.states.MainMenuState;
import de.fhtrier.gdw2.sotf.Interfaces.IActionListener;
import de.fhtrier.gdw2.sotf.menu.MenuPage;

/**
 * Menu page: Help
 * 
 * @author Lusito
 * @todo file paths need to be adapted when resource loader is ready.
 */
public class MenuPageHelp extends MenuPage {
	public MenuPageHelp(final GameContainer container, final StateBasedGame game, final MainMenuState menuState, MenuPage parent)
			throws SlickException {
		super(container, game, menuState, parent, "res/menu/bg_root.png");

		Font font = new AngelCodeFont("res/fonts/verdana_46.fnt","res/fonts/verdana_46_0.tga");
		float x = 100;
		float y = 480;
		float h = font.getLineHeight() * 1.2f;
		addLeftAlignedButton("Back", x, y - h * 2, font,
				new IActionListener() {
					public void onAction() {
						close();
					}
				});
	}
}
