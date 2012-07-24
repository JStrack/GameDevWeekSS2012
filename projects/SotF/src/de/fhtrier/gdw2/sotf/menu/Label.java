package de.fhtrier.gdw2.sotf.menu;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import de.fhtrier.gdw2.sotf.Interfaces.IActionListener;

/**
 * Text label a menu page
 * 
 * @author Lusito
 * @todo file paths need to be adapted when resource loader is ready.
 */
public class Label extends Widget {
	/** The font to write the message with */
	private Font font;
	/** The bounding rect */
	public Rectangle rect = new Rectangle(0, 0, 0, 0);
	/** Text to display */
	public String text = "";
	public Color color;
	public Image image;
	public Align align = Align.LEFT;

	public enum Align {
		LEFT,
		RIGHT,
		CENTER
	}
	
	private Label() {
	}

	public void init(GameContainer container) throws SlickException {
		if(font == null)
			font = new AngelCodeFont("res/fonts/verdana_46.fnt","res/fonts/verdana_46_0.tga");
		if(color == null)
			color = Color.white;
	}

	public void render(Graphics g) {
		g.setFont(font);
		float w = font.getWidth(text);
		float h = font.getHeight(text);
		if(color != null) {
			g.setColor(color);
			switch(align) {
			case LEFT:
				g.drawString(text, rect.getX(), rect.getY());
				break;
			case RIGHT: 
				g.drawString(text, rect.getMaxX() - w, rect.getMaxY() - h);
				break;
			case CENTER: 
				g.drawString(text, rect.getCenterX() - w/2, rect.getCenterY() - h/2);
				break;
			}
		}
		if(image != null) {
			image.draw(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
		}
	}

	@Override
	public boolean contains(int x, int y) {
		return rect.contains(x, y);
	}

	/**
	 * Change the text to display
	 * 
	 * @param value the new value
	 * @return this
	 */
	public Label text(String value) {
		text = value;
		return this;
	}

	/**
	 * Change the position of this label
	 * 
	 * @param x the offset from left in pixels
	 * @param y the offset from top in pixels
	 * @return this
	 */
	public Label position(float x, float y) {
		rect.setLocation(x, y);
		return this;
	}

	/**
	 * Change the size of this label
	 * 
	 * @param width in pixels
	 * @param width in pixels
	 * @return this
	 */
	public Label size(float width, float height) {
		rect.setSize(width, height);
		return this;
	}

	/**
	 * Change the font of the text
	 * 
	 * @param value the new value
	 * @return this
	 */
	public Label font(Font value) {
		font = value;
		return this;
	}

	/**
	 * Change the align of the text
	 * 
	 * @param value the new value
	 * @return this
	 */
	public Label align(Align value) {
		align = value;
		return this;
	}

	/**
	 * Change the text color
	 * 
	 * @param value the new value
	 * @return this
	 */
	public Label color(Color value) {
		color = value;
		return this;
	}

	/**
	 * Change the image
	 * 
	 * @param value the new value
	 * @return this
	 */
	public Label image(Image value) {
		image = value;
		return this;
	}

	/**
	 * Clone this label
	 * 
	 * @return A new Label with the same values as this
	 */
	public Label clone() {
		Label b = Label.create(text, rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());

		b.font = font;
		b.color =  color;
		
		return b;
	}


	/**
	 * Create a new label
	 * 
	 * @param text the text to display
	 * @param x offset from left in pixels
	 * @param y offset from top in pixels
	 * @param width in pixels
	 * @param height in pixels
	 * @return the new label
	 */
	public static Label create(String text, float x, float y, float width, float height) {
		Label label = new Label();
		label.text(text);
		label.position(x,y);
		label.size(width, height);
		return label;
	}
	
	/**
	 * Shows how to create and init a label
	 */
	private void demo() {
		int x=0,y=0,w=0,h=0;
		Label b = Label.create("Hello", x, y, w, h)
			.font(font)
			.color(Color.gray)
			.image(null);
		Label c = b.clone()
			.text("World")
			.position(x,y)
			.size(w,h);
	}
}
