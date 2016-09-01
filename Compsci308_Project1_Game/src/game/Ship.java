package game;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Ship extends Sprite implements GameWorld {
	
	private static final int LIVES = 3;
	private final IntegerProperty playerHitPoints = new SimpleIntegerProperty();
	public final IntegerProperty playerScore = new SimpleIntegerProperty();
	private static final int AMMO_LIMIT = 10;
	private int AMMO = AMMO_LIMIT;

	
	public Ship(String image) {
		super(image);
		this.getImageView().setX(0);
//		this.getImageView().setX(SCENE_WIDTH / 2 - SHIP_WIDTH / 2); 
		this.getImageView().setY(SCENE_HEIGHT - SHIP_HEIGHT - 5);
		this.getImageView().setFitWidth(SHIP_WIDTH);
		this.getImageView().setFitHeight(SHIP_HEIGHT);
		this.playerHitPoints.set(LIVES);
		
	}
	
	public int getAmmo() { 
		return AMMO;
	}
	
	public void setAmmo(int i) {
		this.AMMO = i;
	}

	public void decrementHitPoints() {
		playerHitPoints.set(playerHitPoints.get() - 1);
	}
	
	public void setHitPoints(int i) {
		playerHitPoints.set(playerHitPoints.get() + i);
	}
	
	public IntegerProperty getHitPoints() {
		return playerHitPoints;
	}
	
	public void incrementPlayerScore() {
		playerScore.set(playerScore.get() + 1);
	}

}
