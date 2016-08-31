package game;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Ship extends Sprite implements GameWorld {
	
	private static final int LIVES = 3;
	private final IntegerProperty playerLives = new SimpleIntegerProperty();
	
	public Ship(String image) {
		super(image);
		this.getImageView().setX(0);
//		this.getImageView().setX(SCENE_WIDTH / 2 - SHIP_WIDTH / 2); 
		this.getImageView().setY(SCENE_HEIGHT - SHIP_HEIGHT - 5);
		this.getImageView().setFitWidth(SHIP_WIDTH);
		this.getImageView().setFitHeight(SHIP_HEIGHT);
		this.playerLives.set(LIVES);
	}
	

	public void decrementPlayerLives() {
		playerLives.set(playerLives.get() - 1);
	}
	
	public IntegerProperty getLives() {
		return playerLives;
	}

}
