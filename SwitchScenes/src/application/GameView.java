package application;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;

public class GameView {
	
	private Scene scene;
	private Group root = new Group();

	public GameView() {
		scene = new Scene(root, 350, 300);
		scene.setFill(Color.BLACK);
	}
	
	public Scene getScene() {
		return scene;
	}
	
	public Group getRoot() {
		return root;
	}
	
}
