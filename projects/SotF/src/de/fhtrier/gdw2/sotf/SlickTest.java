package de.fhtrier.gdw2.sotf;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class SlickTest extends BasicGame {
	
	Image img;

	public SlickTest() {
		super("SlickTest");
	}

	@Override
	public void render(GameContainer container, Graphics g)
			throws SlickException {
		g.setBackground(Color.black);
		g.clear();
		
		
		g.drawImage(this.img,0,0,100,100,0,0,this.img.getHeight(),this.img.getWidth());
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		this.img = new Image("src/de/fhtrier/gdw2/sotf/erde.jpg");
		
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
