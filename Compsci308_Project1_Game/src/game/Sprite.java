package game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Sprite {

//	private int xPos;
//	private int yPos;
	private ImageView imageView;
//	private Image image;

	public Sprite(String image) {
//		this.xPos = x;
//		this.yPos = y;
		this.imageView = setImage(image);
//		this.image = new Image(Sprite.class.getResourceAsStream(image));
	}

	public ImageView getImageView() {
		return imageView;
	}

//	public Image getImage() {
//		return image;
//	}
//
//	public int getXPosition() {
//		return xPos;
//	}
//
//	public int getYPosition() {
//		return yPos;
//	}

	public ImageView setImage(String text) {
        Image im = new Image(this.getClass().getResourceAsStream(text));
		ImageView iv = new ImageView(im);
		return iv;
	}
}