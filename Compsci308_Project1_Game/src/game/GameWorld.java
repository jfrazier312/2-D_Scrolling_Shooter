package game;

import javafx.scene.text.Text;

/**
 * This interface determines the scene height and width
 * along with the ships dimensions
 * @author Jordan Frazier (jrf30)
 *
 */
public interface GameWorld {
	
	static final Text GAME_TITLE = new Text("Galactic Ace");
	static final int SCENE_WIDTH = 600; 
	static final int SCENE_HEIGHT = 500;

	static final double SHIP_WIDTH = 40;
	static final double SHIP_HEIGHT = 40;

}
