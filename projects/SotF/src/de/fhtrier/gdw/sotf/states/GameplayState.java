package de.fhtrier.gdw.sotf.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.EmptyTransition;

import de.fhtrier.gdw2.sotf.SlickTestGameState;

/**
 * Gameplay state
 * 
 * @author Lusito
 */
public class GameplayState extends BasicGameState {

	int stateID = -1;
	GameContainer container;
	StateBasedGame game;
	
	public GameplayState(int stateID) {
		this.stateID = stateID;
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		this.container = container;
		this.game = game;
		
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		g.setBackground(Color.darkGray);
		
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
	}

	@Override
	public int getID() {
		return stateID;
	}

	public void keyReleased(int key, char c) {
		if(key == Input.KEY_ESCAPE)
			game.enterState(SlickTestGameState.MAINMENUSTATE, new EmptyTransition(), new EmptyTransition());
	}
}
