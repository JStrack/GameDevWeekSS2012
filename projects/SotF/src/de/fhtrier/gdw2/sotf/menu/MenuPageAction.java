package de.fhtrier.gdw2.sotf.menu;

import de.fhtrier.gdw2.sotf.states.MainMenuState;
import de.fhtrier.gdw2.sotf.Interfaces.IActionListener;

/**
 * Menu page action (switch to another page on an action)
 * 
 * @author Lusito
 */
public class MenuPageAction implements IActionListener {
	MainMenuState menuState;
	private MenuPage page;
	
	public MenuPageAction(MainMenuState menuState, MenuPage page) {
		this.menuState = menuState;
		this.page = page;
	}
	
	public void onAction() {
		menuState.setPage(page);
	}
}