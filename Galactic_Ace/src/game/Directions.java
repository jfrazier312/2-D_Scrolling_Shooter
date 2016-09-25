package game;

// This entire file is part of my masterpiece
// Jordan Frazier

import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;

/**
 * Used for directions. Could have used KeyCode, but wanted to write my own and 
 * add a method for changing the constant to Text type. 
 * @author Jordan Frazier
 *
 */
public enum Directions {

	UP(new Text("UP")),

	RIGHT(new Text("RIGHT")),

	DOWN(new Text("DOWN")),

	LEFT(new Text("LEFT"));
	
	final Text direction;

	Directions(Text direction) {
		this.direction = direction;

	}
	
	public Text toText() {
		return this.direction;
	}

	public KeyCode getKeyCode() {
		switch (this.toString()) {
		case "UP":
			return KeyCode.UP;
		case "RIGHT":
			return KeyCode.RIGHT;
		case "DOWN":
			return KeyCode.DOWN;
		case "LEFT":
			return KeyCode.LEFT;
		default:
			return null;
		}

	}
}
