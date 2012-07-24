package de.fhtrier.gdw2.sotf.Game;

import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

import de.fhtrier.gdw2.sotf.Interfaces.*;

public class Player extends Entity implements IPlayer{
    
    private Vector2f velocity;
    private IUseable[] inventory;
    private List<IPowerups> powerups;
    private ITeam team;

	/**
	 * Enth�lt die ID des lokalen Spielers
	 */
	public static int localPlayerId;

	public Player(Vector2f position, float radius, boolean isLocalPlayer, Image image) {
		super(position, radius, image);
		if (isLocalPlayer)
			localPlayerId = this.getID();
	}

    @Override
    public void setVelocity(Vector2f velo) {
        this.velocity = velo;
    }

	@Override
	public Vector2f getVelocity() {
		return this.velocity;
	}
	
	/**
	 * Steuerung des Spielers
	 * @author Robin Dick
	 * @param gc Der Slick-GameContainer
	 * @param delta Millisekunden seit dem letzten Update-Schritt
	 */
	private void controll(GameContainer gc, int delta) {
		float changeX = 0f;
		float changeY = 0f;
		
		float deltaMovement = Constants.PLAYER_MOVEMENT_SPEED / (1000f * delta);
		
		if (gc.getInput().isKeyDown(Input.KEY_W)) {
			changeY -= deltaMovement; 
		}
		if (gc.getInput().isKeyDown(Input.KEY_S)) {
			changeY += deltaMovement; 
		}
		if (gc.getInput().isKeyDown(Input.KEY_A)) {
			changeX -= deltaMovement; 
		}
		if (gc.getInput().isKeyDown(Input.KEY_D)) {
			changeX += deltaMovement; 
		}
		
		// TODO: Levelgrenzen einhalten!
		float x = this.getPosition().getX() + changeX;
		float y = this.getPosition().getY() + changeY;
		
		this.setPosition(x, y);
	}
	
	@Override
	public void update(GameContainer gameContainer, int time) {
		// Steuerung
		if (isLocalPlayer())
			controll(gameContainer,time);
		
		if (powerups != null) {
			// Duration der Power-ups anpassen/pr�fen
			for (int i = 0; i <= this.powerups.size(); i++) {
				this.powerups.get(i).setDuration(
						this.powerups.get(i).getDuration() - time);
	
				// abgelaufene Powerups entfernen
				if (this.powerups.get(i).getDuration() <= 0) {
					// effekt entfernen
					// TODO
					// Powerup aus Liste l�schen
					this.powerups.remove(i);
				}
			}
		}
	}
	
	/**
	 * Liefert zur�ck, ob der Spieler grade gegessen werden kann oder
	 * immortal-Effekt auf den Spieler zutreffen
	 * 
	 * @return
	 */
	public boolean isEatable() {
		for (int i = 0; i < this.powerups.size(); i++) {
			if (this.powerups.get(i).getType() == IPowerups.PowerupType.IMMORTAL)
				return false;
		}
		return true;
	}

    @Override
    public void eat(IEatable eat) {

        // Wachsen des Spielers
        this.setRadius(this.getRadius() + eat.getEnergy());

        // Usable zu Inventar hinzuf�gen
        IUseable use = eat.getUseable();
        if (use != null) {
            // TODO
        }

        // Powerups hinzuf�gen und Effekt anwenden
        List<IPowerups> plist = eat.getPowerups();
        if (plist != null) {
            for (int i = 0; i < plist.size(); i++) {
                // Powerup zu Liste hinzuf�gen
                this.powerups.add(plist.get(i));

                // Effekt auf Spieler anwenden
                switch (plist.get(i).getType()) {
                case SPEED:
                    break;
                case IMMORTAL:
                    break;
                case SIGHT:
                    break;
                }
            }
        }

    }

    @Override
    public IUseable use(int number) {

        if (number >= 0 && number < this.inventory.length)
            return this.inventory[number];
        else
            return null;
    }

    @Override
    public IUseable[] getInventory() {
        return this.inventory;
    }

    @Override
    public List<IPowerups> getPowerups() {
        return this.powerups;
    }

    @Override
    public ITeam getTeam() {
        return this.team;
    }

    @Override
    public boolean isDead() {
        return this.getRadius() <= 0; // TODO: woher kommt die MinSize?
    }
    
    @Override
    public IUseable getUseable() {
        // TODO Auto-generated method stub
        return null;
    }

	@Override
	public float getEnergy() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public boolean isLocalPlayer() {
		return this.getID()==this.localPlayerId;
	}

}
