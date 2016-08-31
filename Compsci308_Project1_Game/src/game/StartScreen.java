package game;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StartScreen extends Application implements GameWorld {
	
	private BorderPane root;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		root = new BorderPane();
		
		//start, rules, cheat codes
		VBox selections = new VBox(10);
		selections.setAlignment(Pos.CENTER);
		
		VBox titleBox = new VBox(10);
		GAME_TITLE.getStyleClass().add("gameTitle");
		titleBox.setAlignment(Pos.CENTER);
		titleBox.getChildren().add(GAME_TITLE);
		
		GameButton startBtn = new GameButton("Start");
		GameButton rulesBtn = new GameButton("Rules");
		GameButton cheatBtn = new GameButton("Cheat Codes");

		selections.getChildren().addAll(startBtn.getButton(), rulesBtn.getButton(), cheatBtn.getButton());
		
		root.setTop(titleBox);
		root.setCenter(selections);
		
		Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
		primaryStage.setTitle("Game Start Screen");
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}

}
