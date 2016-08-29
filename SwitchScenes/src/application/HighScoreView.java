package application;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;

public class HighScoreView {

	private Scene scene;
	private Group root = new Group();

	public HighScoreView() {
		scene = new Scene(root, 600, 400);
		scene.setFill(Color.AQUA);
	}

	public Scene getScene() {
		return scene;
	}

	public Group getRoot() {
		return root;
	}
}
