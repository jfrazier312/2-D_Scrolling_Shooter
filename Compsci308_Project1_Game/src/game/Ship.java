package game;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Ship extends Sprite implements GameWorld {
	
	private static final int LIVES = 3;
	private final IntegerProperty playerLives = new SimpleIntegerProperty();
	
	public Ship(double width, double height, String image) {
		super(image);
		this.getImageView().setLayoutX(width / 2 - SHIP_WIDTH / 2);
		this.getImageView().setLayoutY(height - SHIP_HEIGHT - 5);
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
