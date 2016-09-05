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
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application implements GameWorld {

	private static BorderPane root;
	private static Stage mainStage;
	private static Scene scene;
	private static HighScoreView hsView;

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

		scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
		scene.getStylesheets().add(Main.class.getResource("GameStyle.css").toExternalForm());
		mainStage.setTitle("Game Start Screen");
		hsView = new HighScoreView();
		initGame(startBtn);
	}

	public static void initGame(GameButton startBtn) {
		GameView game = new GameView();
		Scene gameScene = game.initGame();
		BossBattle boss = new BossBattle();
		Main main = new Main();
		
		// init game on "start" button pressed
		startBtn.getButton().setOnAction(e -> main.initMainGame(game, gameScene));
		// continuously runs, called when variable GameView.isGameOver is set to
		// true
		main.isGameOver(game, boss, hsView, startBtn);
		
		mainStage.setScene(scene);
		mainStage.show();
	}

	public void isGameWon(GameView game, HighScoreView hsView, GameButton startBtn) {
		Timeline timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(1000 / 60), e -> {
			if (BossBattle.gameOverWon) {
				timeline.stop();
				createGameOverWon(game, hsView, startBtn);
			} else if (BossBattle.gameOverLost) {
				timeline.stop();
				createGameOverLost(startBtn);
			}
		}));
		timeline.play();
	}

	public void initMainGame(GameView game, Scene gameScene) {
		mainStage.setScene(gameScene);
		mainStage.show();
		game.animateGame();
	}

	public void isGameOver(GameView game, BossBattle boss, HighScoreView hsView, GameButton startBtn) {
		Timeline timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(1000 / 60), e -> {
			if (CountDownTimer.countDownOver) {
				System.out.println("Timeline over");
				timeline.stop();
				mainStage.setScene(boss.getScene());
				mainStage.setTitle("Boss Battle!");
				isGameWon(game, hsView, startBtn);
			} else if (GameView.isGameOver) {
				timeline.stop();
				createGameOverLost(startBtn);
			}
		}));
		timeline.play();
	}

	public void createGameOverWon(GameView game, HighScoreView hsView, GameButton startBtn) {
		mainStage.setTitle("High Scores");

		hsView.getScene().setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER) {
				hsView.handleOkButtonInput(game.getShip().playerScore.get());
			}
		});

		hsView.getOkButton().setOnAction(e -> {
			hsView.handleOkButtonInput(game.getShip().playerScore.get());
		});

		hsView.getCloseButton().setOnAction(e -> {
			hsView.getTextField().requestFocus();
			hsView.getTextField().setDisable(false);
			Main.initGame(startBtn);
		});

		mainStage.setScene(hsView.getScene());
	}

	public void createGameOverLost(GameButton startBtn) {
		BorderPane newRoot = new BorderPane();
		newRoot.setStyle("-fx-background-color: black;");
		mainStage.setScene(new Scene(newRoot, SCENE_WIDTH, SCENE_HEIGHT));
		mainStage.setTitle("Game Over!");

		Text text = new Text("You have died and thus the world is doomed");
		text.setTextAlignment(TextAlignment.CENTER);
		text.setFill(Color.GHOSTWHITE);

		Button btn = new Button("Close");
		btn.setOnAction(e -> Main.initGame(startBtn));

		newRoot.setCenter(text);
		newRoot.setBottom(btn);
		BorderPane.setAlignment(btn, Pos.BOTTOM_CENTER);
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
