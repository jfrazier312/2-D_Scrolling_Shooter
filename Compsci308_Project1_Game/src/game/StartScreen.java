package game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
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

		// start, rules, cheat codes buttons
		VBox selections = new VBox(10);
		VBox titleBox = new VBox(10);
		styleItems(titleBox, selections);

		GameButton startBtn = new GameButton("Start");
		GameButton rulesBtn = new GameButton("Rules");
		GameButton cheatBtn = new GameButton("Cheat Codes");

		selections.getChildren().addAll(startBtn.getButton(), rulesBtn.getButton(), cheatBtn.getButton());

		root.setTop(titleBox);
		root.setCenter(selections);

		Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
		scene.getStylesheets().add(StartScreen.class.getResource("GameStyle.css").toExternalForm());
		primaryStage.setTitle("Game Start Screen");
		primaryStage.setScene(scene);
		primaryStage.show();

		// set up game screen behind scenes
		GameView game = new GameView();
		Scene gameScene = game.initGame();
		BossBattle boss = new BossBattle();

		// init game on "start" button pressed
		startBtn.getButton().setOnAction(e -> initMainGame(game, gameScene));

		// continuously runs, called when variable GameView.isGameOver is set to
		// true
		isGameOver(game, boss);
		isGameWon(game);
	}

	public void isGameWon(GameView game) {
		Timeline timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(1000 / 60), e -> {
			if (BossBattle.gameOverWon) {
				timeline.stop();
				HighScoreView hsview = new HighScoreView(game.getShip().playerScore.get());
				Stage stage = new Stage();
				stage.setScene(hsview.getScene());
				stage.show();
			}
		}));
		timeline.play();
	}

	public void initMainGame(GameView game, Scene gameScene) {
		mainStage.setScene(gameScene);
		mainStage.show();
		game.animateGame();
	}

	public void isGameOver(GameView game, BossBattle boss) {
		Timeline timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(1000 / 60), e -> {
			if (CountDownTimer.countDownOver) {
				timeline.stop();
				Stage stage = new Stage();
				stage.setScene(boss.getScene());
				stage.show();
				mainStage.close();
			} else if (GameView.isGameOver) {
				timeline.stop();
				createGameOverLost();
			}
		}));
		timeline.play();
	}

	public void createGameOverWon(GameView game) {
		HighScoreView highView = new HighScoreView(game.getShip().playerScore.get());
		mainStage.close();
		Stage stage = new Stage();
		stage.setScene(highView.getScene());
		stage.show();
	}

	public void createGameOverLost() {
		Stage stage = new Stage();
		BorderPane newRoot = new BorderPane();
		newRoot.setStyle("	-fx-background-color: black;");
		stage.setScene(new Scene(newRoot, SCENE_WIDTH, SCENE_HEIGHT));

		Text text = new Text("You have died and thus the world is doomed");
		text.setTextAlignment(TextAlignment.CENTER);
		text.setFill(Color.GHOSTWHITE);

		Button btn = new Button("Close");
		btn.setOnAction(e -> Platform.exit());

		newRoot.setCenter(text);
		newRoot.setBottom(btn);
		BorderPane.setAlignment(btn, Pos.BOTTOM_CENTER);
		stage.show();
		mainStage.close();
	}

	public void styleItems(VBox titleBox, VBox buttons) {
		buttons.setAlignment(Pos.CENTER);
		titleBox.setAlignment(Pos.CENTER);
		titleBox.setPadding(new Insets(60, 0, 0, 0));
		titleBox.getChildren().add(GAME_TITLE);
		GAME_TITLE.getStyleClass().add("gameTitle");
		root.getStyleClass().add("startScreen");
	}

}
