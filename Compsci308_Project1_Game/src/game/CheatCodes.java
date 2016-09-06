package game;

import javafx.beans.binding.Bindings;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;

public class CheatCodes extends GameView {
	
	public CheatCodes(Scene scene, Ship ship, Text text) {
		
		scene.setOnKeyPressed(e -> {
			if(e.getCode() == KeyCode.D){
				getInfiniteLives(ship, text);
			} else if (e.getCode() == KeyCode.F){
				getInfiniteAmmo(ship, text);
			}
		});
		
	}
	
	public void getInfiniteLives(Ship ship, Text text) {
		ship.setHitPoints(10000);
		text.textProperty().bind(Bindings.concat("Score: ").concat(ship.getScore()).concat("\nHit Points: ").concat(ship.getHitPoints()));
	}
	
	public void getInfiniteAmmo(Ship ship, Text text) {
		ship.setHitPoints(10000);
		text.textProperty().bind(Bindings.concat("Score: ").concat(ship.getScore()).concat("\nHit Points: ").concat(ship.getHitPoints()));
	}
	
}
