package highScoreList;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class EnemyShip extends Rectangle {
	
	private Rectangle enemy;
	
	public EnemyShip() {
		enemy = new Rectangle(0, 5, 15, 15);
		enemy.setFill(Color.BLUE);
	}
	
	public Rectangle getEnemyShip() {
		return enemy;
	}
}
