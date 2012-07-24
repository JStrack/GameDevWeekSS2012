package de.fhtrier.gdw2.sotf.menu;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import de.fhtrier.gdw2.sotf.states.MainMenuState;
import de.fhtrier.gdw2.sotf.Interfaces.IActionListener;

/**
 * Text & image button for a menu page
 * 
 * @author Lusito
 * @todo file paths need to be adapted when resource loader is ready.
 */
public class Button
{
    /** Temp */
    private MainMenuState menuState;

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

    /**
     * Mouse states
     */
    public enum State
    {
        DEFAULT, HOVER, PRESSED
    }

    private Button()
    {
        int numStates = State.values().length;
        colors = new Color[numStates];
        images = new Image[numStates];
    }

    /**
     * @see org.newdawn.slick.state.BasicGameState#init(org.newdawn.slick.GameContainer,
     *      org.newdawn.slick.state.StateBasedGame)
     */
    public void init(GameContainer container, MainMenuState menuState) throws SlickException
    {
        this.menuState = menuState;
        if (font == null)
            font = new AngelCodeFont("res/demo2.fnt", "res/demo2_00.tga");
    }

    /**
     * @see org.newdawn.slick.state.BasicGameState#render(org.newdawn.slick.GameContainer,
     *      org.newdawn.slick.state.StateBasedGame, org.newdawn.slick.Graphics)
     */
    public void render(Graphics g)
    {
        g.setFont(font);
        float w = font.getWidth(text);
        float h = font.getHeight(text);
        State state = getState();
        Color color = colors[state.ordinal()];
        Image image = images[state.ordinal()];
        if (color == null)
            color = colors[State.DEFAULT.ordinal()];
        if (image == null)
            image = images[State.DEFAULT.ordinal()];
        if (color != null)
        {
            g.setColor(color);
            g.drawString(text, rect.getCenterX() - w / 2, rect.getCenterY() - h / 2);
        }
        if (image != null)
        {
            image.draw(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
        }
    }

    /**
     * Get the current state of the button
     * 
     * @return a State
     */
    public State getState()
    {
        if (!rect.contains(menuState.mouseX, menuState.mouseY))
            return State.DEFAULT;
        if (menuState.mouseDown)
            return State.PRESSED;
        return State.HOVER;
    }

    /**
     * Change the text to display
     * 
     * @param value
     *            the new value
     * @return this
     */
    public Button text(String value)
    {
        text = value;
        return this;
    }

    /**
     * Change the position of this button
     * 
     * @param x
     *            the offset from left in pixels
     * @param y
     *            the offset from top in pixels
     * @return this
     */
    public Button position(float x, float y)
    {
        rect.setLocation(x, y);
        return this;
    }

    /**
     * Change the size of this button
     * 
     * @param width
     *            in pixels
     * @param width
     *            in pixels
     * @return this
     */
    public Button size(float width, float height)
    {
        rect.setSize(width, height);
        return this;
    }

    /**
     * Change the font of the text
     * 
     * @param value
     *            the new value
     * @return this
     */
    public Button font(Font value)
    {
        font = value;
        return this;
    }

    /**
     * Change the text color for State.DEFAULT
     * 
     * @param value
     *            the new value
     * @return this
     */
    public Button color(Color value)
    {
        colors[State.DEFAULT.ordinal()] = value;
        return this;
    }

    /**
     * Change the text color for State.HOVER
     * 
     * @param value
     *            the new value
     * @return this
     */
    public Button hoverColor(Color value)
    {
        colors[State.HOVER.ordinal()] = value;
        return this;
    }

    /**
     * Change the text color for State.PRESSED
     * 
     * @param value
     *            the new value
     * @return this
     */
    public Button pressedColor(Color value)
    {
        colors[State.PRESSED.ordinal()] = value;
        return this;
    }

    /**
     * Change the image for State.DEFAULT
     * 
     * @param value
     *            the new value
     * @return this
     */
    public Button image(Image value)
    {
        images[State.DEFAULT.ordinal()] = value;
        return this;
    }

    /**
     * Change the image for State.HOVER
     * 
     * @param value
     *            the new value
     * @return this
     */
    public Button hoverImage(Image value)
    {
        images[State.HOVER.ordinal()] = value;
        return this;
    }

    /**
     * Change the image for State.PRESSED
     * 
     * @param value
     *            the new value
     * @return this
     */
    public Button pressedImage(Image value)
    {
        images[State.PRESSED.ordinal()] = value;
        return this;
    }

    /**
     * Change the action for State.PRESSED
     * 
     * @param value
     *            the new value
     * @return this
     */
    public Button action(IActionListener listener)
    {
        return this;
    }

    /**
     * Clone this button
     * 
     * @return A new Button with the same values as this
     */
    public Button clone()
    {
        Button b = Button.create(text, rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());

        b.font = font;

        int numStates = State.values().length;
        for (int i = 0; i < numStates; i++)
        {
            b.colors[i] = colors[i];
            b.images[i] = images[i];
        }

        return b;
    }

    /**
     * Create a new button
     * 
     * @param text
     *            the text to display
     * @param x
     *            offset from left in pixels
     * @param y
     *            offset from top in pixels
     * @param width
     *            in pixels
     * @param height
     *            in pixels
     * @return the new button
     */
    public static Button create(String text, float x, float y, float width, float height)
    {
        Button button = new Button();
        button.text(text);
        button.position(x, y);
        button.size(width, height);
        return button;
    }

    /**
     * Shows how to create and init a button
     */
    private void demo()
    {
        int x = 0, y = 0, w = 0, h = 0;
        Button b = Button.create("Hello", x, y, w, h).font(font).color(Color.gray).hoverColor(Color.white).pressedColor(Color.red).image(null).hoverImage(null).pressedImage(null).action(new IActionListener()
        {
            public void onAction()
            {

            }
        });
        Button c = b.clone().text("World").position(x, y).size(w, h).action(new IActionListener()
        {
            public void onAction()
            {

            }
        });
    }
}
