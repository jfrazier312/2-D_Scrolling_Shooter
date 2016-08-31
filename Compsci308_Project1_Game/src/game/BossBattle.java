package game;

import javafx.scene.Group;
import javafx.scene.Scene;

public class BossBattle implements GameWorld {
	
	private Scene bossScene;
	
	public BossBattle() {
		Group root = new Group();
		bossScene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
		
	}
	
	public void initBossBattle() {
		
	}

}
