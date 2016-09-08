package game;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application implements GameWorld {

	// public debug, set to true to see print statements in console
	public static boolean DEBUG = false;

	// Static in order to keep across new instances of gameview/bossbattle
	private static BorderPane root;
	private static Stage mainStage;
	private static Scene scene;
	private static HighScoreView hsView;
	private static Main main = new Main();

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

	private static void initGame(GameButton startBtn) {
		// Loads new scenes in background while on start screen
		GameView game = new GameView();
		Scene gameScene = game.initGame();
		BossBattle boss = new BossBattle();

		// init main game scene on "start" button pressed
		startBtn.getButton().setOnAction(e -> main.initMainGame(game, gameScene));

		// continuously runs, called when variable GameView.isGameOver is set to
		// true
		main.isGameOver(game, boss, hsView, startBtn);

		mainStage.setScene(scene);
		mainStage.show();
	}

	/**
	 * Continuously checks whether the boss battle has been won Starts running
	 * after boss battle is started
	 * 
	 * @param game
	 *            - main game instance
	 * @param hsView
	 *            - static high score view
	 * @param startBtn
	 */
	private void isGameWon(GameView game, HighScoreView hsView, GameButton startBtn, BossBattle boss) {
		Timeline timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(1000 / 60), e -> {
			if (boss.getGameOverWon()) {
				timeline.stop();
				createGameOverWon(game, hsView, startBtn);
			} else if (boss.getGameOverLost()) {
				timeline.stop();
				createGameOverLost(startBtn);
			}
		}));
		timeline.play();
	}

	/**
	 * Animates a new game
	 * 
	 * @param game
	 * @param gameScene
	 */
	private void initMainGame(GameView game, Scene gameScene) {
		mainStage.setScene(gameScene);
		mainStage.show();
		game.animateGame();
	}

	/**
	 * Continuously runs in main game scene, checks if you die or timer stops
	 * first.
	 * 
	 * @param game
	 * @param boss
	 * @param hsView
	 * @param startBtn
	 */
	private void isGameOver(GameView game, BossBattle boss, HighScoreView hsView, GameButton startBtn) {
		Timeline timeline = new Timeline();
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(1000 / 60), e -> {
			if (game.getTimer().getTimerDone() || game.isSkipBattle()) {
				if (DEBUG)
					System.out.println("Timer done");
				timeline.stop();
				mainStage.setScene(boss.getScene());
				mainStage.setTitle("Boss Battle!");
				isGameWon(game, hsView, startBtn, boss);
			} else if (game.getGameOver()) {
				timeline.stop();
				createGameOverLost(startBtn);
			}
		}));
		timeline.play();
	}

	/**
	 * Takes you to the high scores view
	 * 
	 * @param game
	 * @param hsView
	 * @param startBtn
	 */
	private void createGameOverWon(GameView game, HighScoreView hsView, GameButton startBtn) {
		mainStage.setTitle("High Scores");

		hsView.getScene().setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER) {
				hsView.handleOkButtonInput(game.getShip().getScore().get());
			}
		});

		hsView.getOkButton().setOnAction(e -> {
			if (hsView.getTextField().isDisabled()) {
				hsView.getTextField().setDisable(false);
				Main.initGame(startBtn);
			} else {
				hsView.handleOkButtonInput(game.getShip().getScore().get());
			}
		});

		hsView.getCloseButton().setOnAction(e -> {
			hsView.getTextField().setDisable(false);
			Main.initGame(startBtn);
		});

		mainStage.setScene(hsView.getScene());
	}

	private void createGameOverLost(GameButton startBtn) {
		BorderPane newRoot = new BorderPane();
		newRoot.getStyleClass().add("gameOverLost");
		newRoot.setStyle("-fx-background-color: black");
		mainStage.setScene(new Scene(newRoot, SCENE_WIDTH, SCENE_HEIGHT));
		// mainStage.getScene().getStylesheets().add(Main.class.getResource("GameStyle.css").toExternalForm());
		mainStage.setTitle("Game Over!");

		Text text = new Text("You have died and thus the world is doomed");
		text.setTextAlignment(TextAlignment.CENTER);
		text.setFill(Color.GHOSTWHITE);

		Button btn = new Button("Retry");
		btn.setOnMouseClicked(e -> Main.initGame(startBtn));

		newRoot.setCenter(text);
		newRoot.setBottom(btn);
		BorderPane.setAlignment(btn, Pos.BOTTOM_CENTER);
	}

	private void styleItems(VBox titleBox, VBox buttons) {
		buttons.setAlignment(Pos.CENTER);
		titleBox.setAlignment(Pos.CENTER);
		titleBox.setPadding(new Insets(60, 0, 0, 0));
		titleBox.getChildren().add(GAME_TITLE);
		GAME_TITLE.getStyleClass().add("gameTitle");
		root.getStyleClass().add("startScreen");
	}

}
