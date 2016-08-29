package highScoreList;

import java.util.Random;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class EnemyShip extends Rectangle {
	
	private final Random random = new Random();
	private Rectangle enemy;
	
	public EnemyShip(int sceneWidth) {
		enemy = new Rectangle(random.nextInt(sceneWidth), 5, 15, 15);
		enemy.setFill(Color.BLUE);
	}
	
	public Rectangle getEnemyShip() {
		return enemy;
	}
}
