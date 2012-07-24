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
 * Text & image button for a menu page
 * 
 * @author Lusito
 * @todo file paths need to be adapted when resource loader is ready.
 */
public class Button extends Widget {
	/** The font to write the message with */
	private Font font;
	/** The bounding rect */
	public Rectangle rect = new Rectangle(0, 0, 0, 0);
	/** Text to display */
	public String text = "";
	/** A color for each state (null to ignore) */
	public Color colors[];
	/** An image for each state (null to ignore) */
	public Image images[];
	/** An action listener */
	public IActionListener listener;
	public State state = State.DEFAULT;
	public Align align = Align.CENTER;
	
	/**
	 * Mouse states
	 */
	public enum State {
		DEFAULT,
		HOVER,
		PRESSED
	}
	
	public enum Align {
		LEFT,
		RIGHT,
		CENTER
	}
	
	private Button() {
		int numStates = State.values().length;
		colors = new Color[numStates];
		images = new Image[numStates];
	}

	public void init(GameContainer container) throws SlickException {
		if(font == null)
			font = new AngelCodeFont("res/fonts/verdana_46.fnt","res/fonts/verdana_46_0.tga");
	}

	public void render(Graphics g) {
		g.setFont(font);
		float w = font.getWidth(text);
		float h = font.getHeight(text);
		Color color = colors[state.ordinal()];
		Image image = images[state.ordinal()];
		if(color == null)
			color = colors[State.DEFAULT.ordinal()];
		if(image == null)
			image = images[State.DEFAULT.ordinal()];
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
	
	@Override
	public void keyReleased(int key, char c) {
		if(listener != null && key == Input.KEY_ENTER) {
			listener.onAction();
		}
	}

	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		if(state != State.PRESSED) {
			if(contains(newx, newy))
				state = State.HOVER;
			else
				state = State.DEFAULT;
		}
	}

	@Override
	public void mouseReleased(int button, int x, int y) {
		if(listener != null && contains(x, y)) {
			listener.onAction();
		}
		
		if(contains(x, y))
			state = State.HOVER;
		else
			state = State.DEFAULT;
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		if(contains(x, y))
			state = State.PRESSED;
		else
			state = State.DEFAULT;
	}
	
	/**
	 * Change the text to display
	 * 
	 * @param value the new value
	 * @return this
	 */
	public Button text(String value) {
		text = value;
		return this;
	}

	/**
	 * Change the position of this button
	 * 
	 * @param x the offset from left in pixels
	 * @param y the offset from top in pixels
	 * @return this
	 */
	public Button position(float x, float y) {
		rect.setLocation(x, y);
		return this;
	}

	/**
	 * Change the size of this button
	 * 
	 * @param width in pixels
	 * @param width in pixels
	 * @return this
	 */
	public Button size(float width, float height) {
		rect.setSize(width, height);
		return this;
	}

	/**
	 * Change the font of the text
	 * 
	 * @param value the new value
	 * @return this
	 */
	public Button font(Font value) {
		font = value;
		return this;
	}

	/**
	 * Change the align of the text
	 * 
	 * @param value the new value
	 * @return this
	 */
	public Button align(Align value) {
		align = value;
		return this;
	}

	/**
	 * Change the text color for State.DEFAULT
	 * 
	 * @param value the new value
	 * @return this
	 */
	public Button color(Color value) {
		colors[State.DEFAULT.ordinal()] = value;
		return this;
	}

	/**
	 * Change the text color for State.HOVER
	 * 
	 * @param value the new value
	 * @return this
	 */
	public Button hoverColor(Color value) {
		colors[State.HOVER.ordinal()] = value;
		return this;
	}

	/**
	 * Change the text color for State.PRESSED
	 * 
	 * @param value the new value
	 * @return this
	 */
	public Button pressedColor(Color value) {
		colors[State.PRESSED.ordinal()] = value;
		return this;
	}

	/**
	 * Change the image for State.DEFAULT
	 * 
	 * @param value the new value
	 * @return this
	 */
	public Button image(Image value) {
		images[State.DEFAULT.ordinal()] = value;
		return this;
	}

	/**
	 * Change the image for State.HOVER
	 * 
	 * @param value the new value
	 * @return this
	 */
	public Button hoverImage(Image value) {
		images[State.HOVER.ordinal()] = value;
		return this;
	}

	/**
	 * Change the image for State.PRESSED
	 * 
	 * @param value the new value
	 * @return this
	 */
	public Button pressedImage(Image value) {
		images[State.PRESSED.ordinal()] = value;
		return this;
	}

	/**
	 * Change the action for State.PRESSED
	 * 
	 * @param value the new value
	 * @return this
	 */
	public Button action(IActionListener value) {
		listener = value;
		return this;
	}
	
	/**
	 * Clone this button
	 * 
	 * @return A new Button with the same values as this
	 */
	public Button clone() {
		Button b = Button.create(text, rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());

		b.font = font;
		
		int numStates = State.values().length;
		for(int i=0; i<numStates; i++) {
			b.colors[i] = colors[i];
			b.images[i] = images[i];
		}
		
		return b;
	}


	/**
	 * Create a new button
	 * 
	 * @param text the text to display
	 * @param x offset from left in pixels
	 * @param y offset from top in pixels
	 * @param width in pixels
	 * @param height in pixels
	 * @return the new button
	 */
	public static Button create(String text, float x, float y, float width, float height) {
		Button button = new Button();
		button.text(text);
		button.position(x,y);
		button.size(width, height);
		return button;
	}
	
	/**
	 * Shows how to create and init a button
	 */
	private void demo() {
		int x=0,y=0,w=0,h=0;
		Button b = Button.create("Hello", x, y, w, h)
			.font(font)
			.color(Color.gray)
			.hoverColor(Color.white)
			.pressedColor(Color.red)
			.image(null)
			.hoverImage(null)
			.pressedImage(null)
			.action(
				new IActionListener() {
					public void onAction() {
						
					}
				}
			);
		Button c = b.clone()
			.text("World")
			.position(x,y)
			.size(w,h)
			.action(
				new IActionListener() {
					public void onAction() {
			
					}
				}
			);
	}
}
