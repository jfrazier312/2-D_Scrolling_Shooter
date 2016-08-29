package application;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MyGame extends Application {

	private Stage stage;
	private HighScoreView highscore;
	private GameView gameView;

	@Override
	public void start(Stage primaryStage) throws Exception {

		highscore = new HighScoreView();
		gameView = new GameView();
		stage = primaryStage;

		Button btn1 = new Button("Click to go to highscore view");
		btn1.setOnAction(e -> handleButtonAction(e, btn1));
		Button btn2 = new Button("Click to go to game view");
		btn2.setOnAction(e -> handleButtonAction(e, btn2));

		gameView.getRoot().getChildren().add(btn1);
		highscore.getRoot().getChildren().add(btn2);

		stage.setTitle("Switch between classes");
		stage.setScene(gameView.getScene());
		stage.show();
	}

	public void handleButtonAction(ActionEvent e, Button btn) {
		if (e.getTarget() == btn && stage.getScene() == gameView.getScene()) {
			stage.setScene(highscore.getScene());
		} else {
			stage.setScene(gameView.getScene());
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

}
