package game;

import java.util.Random;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class EnemyShip extends Rectangle {
	
	private final Random random = new Random();
	private Rectangle enemy;
	private static final double ENEMY_WIDTH = 15;
	private static final double ENEMY_HEIGHT = 15;
	private boolean stopAnimation;
	
	private boolean amAlive = true;
	
	public EnemyShip(int sceneWidth) {
		enemy = new Rectangle(random.nextInt(sceneWidth) + 1, 5, ENEMY_WIDTH, ENEMY_HEIGHT);
		enemy.setFill(Color.BLUE);
		stopAnimation = false;
	}
	
	public Rectangle getEnemyShip() {
		return enemy;
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
	
	public void setAnimationStop(boolean boo) {
		this.stopAnimation = boo;
	}
	
	public boolean getAlive() {
		return amAlive;
	}
	
	public void setAlive(boolean boo) {
		this.amAlive = boo;
	}
}
