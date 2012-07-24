package de.fhtrier.gdw2.sotf.states;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

//import de.fhtrier.gdw2.sotf.Interfaces.IActionListener;
import de.fhtrier.gdw2.sotf.menu.Button;

public class MainMenuState extends BasicGameState {

    int stateID = -1;

    public int mouseX, mouseY;
    public boolean mouseDown;
    public List<Button> buttons = new ArrayList<Button>();
    private Font font;

    public MainMenuState(int stateID) {
        this.stateID = stateID;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game)
            throws SlickException {
        font = new AngelCodeFont("res/demo2.fnt","res/demo2_00.tga");

        float x = container.getWidth()*0.5f;
        float y = container.getHeight()*0.5f;
        float h = font.getLineHeight() * 1.2f;
        addCenteredButton("Create a Game", x, y - h * 2);
        addCenteredButton("Join a Game", x, y - h * 1);
        addCenteredButton("Options", x, y);
        addCenteredButton("Help", x, y + h * 1);
        addCenteredButton("Exit", x, y + h * 2);
        for(Button b: buttons)
            b.init(container, this);
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g)
            throws SlickException {
        g.clear();
        for(Button b: buttons)
            b.render(g);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta)
            throws SlickException {
        // TODO Auto-generated method stub

        if (container.getInput().isKeyDown(Input.KEY_1)) {
            game.enterState(1);
        }

    }

    @Override
    public int getID() {
        // TODO Auto-generated method stub
        return stateID;
    }


    public void addCenteredButton(final String text, float x, float y) {
        float w = font.getWidth(text);
        float h = font.getHeight(text);

        Button button = Button.create(text, x - w/2, y - h/2, w, h)
            .font(font)
            .color(Color.gray)
            .hoverColor(Color.white)
            .pressedColor(Color.red)
            .action(
                new IActionListener() {
                    public void onAction() {
                        System.out.println("Button '" + text + "' pressed");
                    }
                }
            );
        buttons.add(button);
    }

    public void keyReleased(int key, char c) {
        if (key == Input.KEY_ESCAPE) {
        }
    }

    public void mouseMoved(int oldx, int oldy, int newx, int newy) {
        mouseX = newx;
        mouseY = newy;
    }

    public void mouseReleased(int button, int x, int y) {
        mouseDown = false;
    }

    public void mousePressed(int button, int x, int y) {
        mouseDown = true;
    }
}