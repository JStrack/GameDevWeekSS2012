
package de.fhtrier.gdw2.sotf;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import de.fhtrier.gdw.sotf.states.GameplayState;
import de.fhtrier.gdw.sotf.states.MainMenuState;

public class SlickTestGameState extends StateBasedGame {

	public static final int MAINMENUSTATE = 0;
	public static final int GAMEPLAYSTATE = 1;
	
	public SlickTestGameState() {
		super("SlickTest");
	}
	
	public static void main(String[] args) {
		try {
		    AppGameContainer app = new AppGameContainer(new SlickTestGameState());
		    app.start();
		} catch (SlickException e) {
		    e.printStackTrace();
		}	
	}

	@Override
	public void initStatesList(GameContainer arg0) throws SlickException {
		this.addState(new MainMenuState(MAINMENUSTATE));
		this.addState(new GameplayState(GAMEPLAYSTATE));
	}

}