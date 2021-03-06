package de.fhtrier.gdw2.sotf.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.EmptyTransition;

import de.fhtrier.gdw2.sotf.SlickTestGameState;
import de.fhtrier.gdw2.sotf.Game.Player;
import de.fhtrier.gdw2.sotf.Game.World;

/**
 * @author Kevin Korte
 * @author Attila Djerdj
 * @author Stefan Probst
 * 
 */
public class GameplayState extends BasicGameState
{

    // Default State ID Wert -1;
    int stateID = -1;

	GameContainer container;
	StateBasedGame game;
    World world;

    /**
     * Konstruktor
     * 
     * @param stateID
     */
    public GameplayState(int stateID)
    {
        this.stateID = stateID;
    }

    /**
     * Initialisierung
     * 
     * @param GameContainer
     * @param StateBasedGame
     */
    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException
    {
		this.container = container;
		this.game = game;
		
    	world = new World();
        // by Robin:
        // Player p = new Player(new Vector2f(32,32), 32, 1, new
        // Image("Kreis.png")); // TODO: delete
        // world.add(p);
    }

    @Override
    /**
     * @param GameContainer
     * @param StatedBasedGame
     * @param Graphics
     */
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		g.setBackground(Color.darkGray);
        //world.render(container, g);

    }

    @Override
    /**
     * @param GameContainer
     * @param StateBasedGame
     * @param int
     */
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException
    {
        world.update(container, delta);
    }

    @Override
    /**
     * @return state ID
     */
    public int getID()
    {
        // TODO Auto-generated method stub
        return stateID;
    }

	public void keyReleased(int key, char c) {
		if(key == Input.KEY_ESCAPE)
			game.enterState(SlickTestGameState.MAINMENUSTATE, new EmptyTransition(), new EmptyTransition());
	}

}
