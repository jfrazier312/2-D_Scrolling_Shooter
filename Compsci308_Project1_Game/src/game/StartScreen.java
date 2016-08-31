package game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class StartScreen extends Application implements GameWorld {

	private BorderPane root;
	private Stage mainStage;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		mainStage = primaryStage;
		root = new BorderPane();

		// start, rules, cheat codes
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

		startBtn.getButton().setOnAction(e -> initMainGame());

		isGameOverLost();
	}

	public void initMainGame() {
		GameView game = new GameView();
		Scene gameScene = game.initGame();
		mainStage.setScene(gameScene);
		mainStage.show();

	}

	//Doesn't work bc of animations
	public void isGameOverLost() {
		Timeline timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(1000 / 60), e -> {
			if (GameView.isGameOver) {
//				Platform.exit();
				
				//bugs out because animations are still running
//				HighScoreView highView = new HighScoreView(GameView.playerScore.get());
//				mainStage.setScene(highView.getScene());
			}
		}));
		timeline.play();

	}

}
