package game;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
/**
 * Acts as the main screen game buttons. Used by Main.java to create the buttons.
 * 
 * Someone could call GameButton("randomtext"), which would cause this method to 
 * create a game button that has no function. They would need to add in functionality
 * in the switch statement in the contructor. 
 * 
 * ex. GameButton btn = new GameButton("Start");
 * @author jf
 *
 */
public class GameButton extends Button {
	
	private Button button;
	
	public GameButton(String text) {
		switch (text) {
			case "Start":	
				button = new Button("Start");
//				button.setOnAction(e -> initGame());
				break;
			case "Rules":
				button = new Button("Rules");
				button.setOnAction(e -> popupRulesDialog());
				break;
			case "Cheat Codes":
				button = new Button("Cheat Codes");
				button.setOnAction(e -> popupCheatCodesDialog());
				break;
			default:
				break;			
		}
	}
	
	@Deprecated
	public void initGame() {
		System.out.println("start the game");
		GameView game = new GameView();
		game.initGame();
	}
	
	private void popupRulesDialog() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("How to Play");
		alert.setHeaderText("Rules");
		alert.setContentText("Use arrow keys to move your ship back and forth and space to fire!\nYour goal is to destroy as many enemy ships as possible while avoiding their attacks.\nYou only have 30 seconds before the enemy boss shows up!\n(Watch out for your ammo limit, you get two extra bullets on each successful hit)\nIn Boss fight, click on the continue button with the mouse to further the instructions.");
		alert.showAndWait();
	}
	
	private void popupCheatCodesDialog() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText("Cheats");
		alert.setTitle("Cheat Codes");
		alert.setContentText("Press 'd' for infinite lives\nPress 'f' for infinite ammo\nPress 's' to skip to boss battle\nPress 'a' to automatically die lol\nPress 'b' to automatically input missile launch sequence");
		alert.showAndWait();
	}
	
	
	public Button getButton() {
		return button;
	}

}
