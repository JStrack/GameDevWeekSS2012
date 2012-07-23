package de.fhtrier.gdw2.sotf;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class SlickTest extends BasicGame {

	public SlickTest() {
		super("SlickTest");
	}

	@Override
	public void render(GameContainer container, Graphics g)
			throws SlickException {
		g.setBackground(Color.green);
		g.clear();
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {
		 try {
	            AppGameContainer app = new AppGameContainer(new SlickTest());
	            app.start();
	        } catch (SlickException e) {
	            e.printStackTrace();
	        }		
	}

}
