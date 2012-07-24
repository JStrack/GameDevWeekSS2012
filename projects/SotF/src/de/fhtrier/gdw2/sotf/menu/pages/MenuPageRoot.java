package de.fhtrier.gdw2.sotf.menu.pages;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.EmptyTransition;

import de.fhtrier.gdw2.sotf.states.MainMenuState;
import de.fhtrier.gdw2.sotf.SlickTestGameState;
import de.fhtrier.gdw2.sotf.Interfaces.IActionListener;
import de.fhtrier.gdw2.sotf.menu.MenuPage;
import de.fhtrier.gdw2.sotf.menu.MenuPageAction;

/**
 * Menu page: Root Menu
 * 
 * @author Lusito
 * @todo file paths need to be adapted when resource loader is ready.
 */
public class MenuPageRoot extends MenuPage {
	private MenuPage create;
	private MenuPage join;
	private MenuPage options;
	private MenuPage help;
	private MenuPage credits;
	
	public MenuPageRoot(final GameContainer container, final StateBasedGame game, final MainMenuState menuState)
			throws SlickException {
		super(container, game, menuState, null, "res/menu/bg_root.png");

		create = new MenuPageCreate(container, game, menuState, this);
		join = new MenuPageJoin(container, game, menuState, this);
		options = new MenuPageOptions(container, game, menuState, this);
		help = new MenuPageHelp(container, game, menuState, this);
		credits = new MenuPageCredits(container, game, menuState, this);

		Font font = new AngelCodeFont("res/fonts/verdana_46.fnt","res/fonts/verdana_46_0.tga");
		float x = 100;
		float y = 480;
		float h = font.getLineHeight() * 1.2f;
		addLeftAlignedButton("Spiel erstellen", x, y - h * 2, font, new MenuPageAction(menuState, create));
		addLeftAlignedButton("Spiel beitreten", x, y - h * 1, font, new MenuPageAction(menuState, join));
		addLeftAlignedButton("Optionen", x, y, font, new MenuPageAction(menuState, options));
		addLeftAlignedButton("Hilfe", x, y + h * 1, font, new MenuPageAction(menuState, help));
		addLeftAlignedButton("Credits", x, y + h * 2, font, new MenuPageAction(menuState, credits));
		
		addCenteredButton("Exit", 943, 710, font,
			new IActionListener() {
				public void onAction() {
					System.exit(0); // todo
				}
			});
	}
}
