package game;

import javafx.scene.image.ImageView;

public class Ship extends Sprite implements GameWorld {

	private ImageView myShip;

	private static final double SHIP_WIDTH = 40;
	private static final double SHIP_HEIGHT = 40;

	public Ship(double width, double height, String image) {
		super(image);
		this.getImageView().setX(width / 2 - SHIP_WIDTH / 2);
		this.getImageView().setY(height - SHIP_HEIGHT - 5);
		this.getImageView().setFitWidth(SHIP_WIDTH);
		this.getImageView().setFitHeight(SHIP_HEIGHT);
	}

}
