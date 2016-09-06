package game;

import javafx.scene.image.ImageView;

public class EnemyShip extends Sprite implements GameWorld {
	
	private static final double ENEMY_WIDTH = 30;
	private static final double ENEMY_HEIGHT = 30;
	private boolean stopAnimation;
	
	//TODO: Set enemies to randomly spawn
	public EnemyShip(String image) {
		super(image);
		this.getImageView().setX(0);
//		this.getImageView().setX(SCENE_WIDTH / 2 - SHIP_WIDTH / 2); 
		this.getImageView().setY(0);
		this.getImageView().setFitWidth(ENEMY_WIDTH);
		this.getImageView().setFitHeight(ENEMY_HEIGHT);
	}
	
	public ImageView getEnemyShip() {
		return this.getImageView();
	}
	
	public double getEnemyWidth() {
		return ENEMY_WIDTH;
	}
	
	public double getEnemyHeight(){
		return ENEMY_HEIGHT;
	}
	
	public boolean getAnimationStop() {
		return stopAnimation;
	}
	
	public void setAnimationStop(boolean stop) {
		this.stopAnimation = stop;
	}
	

}
