package game;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
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

/**
 * Main class to control game, determines when to switch scenes.
 * Uses booleans in timelines to determine switch to boss battle and highscoreview.
 * Starts game when GameButton("Start") is pressed.
 * 
 * @author Jordan Frazier
 *
 */
public class Main extends Application implements GameWorld {

	// public static debug, set to true to see print statements in console when playing
	public static boolean DEBUG = false;

	private BorderPane root;
	private Stage mainStage;
	private Scene scene;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		mainStage = primaryStage;
		root = new BorderPane();
		HighScoreView hsView = new HighScoreView();

		// start, rules, cheat codes buttons
		GameButton startBtn = createButtons();

		scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
		scene.getStylesheets().add(Main.class.getResource("GameStyle.css").toExternalForm());
		mainStage.setTitle("Game Start Screen");
		initGame(startBtn, hsView);
	}

	/**
	 * Creates buttons and sets them as children of root
	 * @return gamebutton that starts main game
	 */
	private GameButton createButtons() {
		VBox selections = new VBox(10);
		VBox titleBox = new VBox(10);
		styleItems(titleBox, selections);

		GameButton startBtn = new GameButton("Start");
		GameButton rulesBtn = new GameButton("Rules");
		GameButton cheatBtn = new GameButton("Cheat Codes");

		selections.getChildren().addAll(startBtn.getButton(), rulesBtn.getButton(), cheatBtn.getButton());

		root.setTop(titleBox);
		root.setCenter(selections);
		return startBtn;
	}

	/**
	 * Loads the start screen and other scenes in the background.
	 * @param startBtn button that starts game instance
	 * @param hsView the high score view
	 */
	private void initGame(GameButton startBtn, HighScoreView hsView) {
		// Loads new scenes in background while on start screen
		GameView game = new GameView();
		Scene gameScene = game.initGame();
		BossBattle boss = new BossBattle();

		startBtn.getButton().setOnAction(e -> initMainGame(game, gameScene));

		// continuously runs, executes when variable GameView.isGameOver is true
		isGameOver(game, boss, hsView, startBtn);

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
				createGameOverLost(startBtn, hsView);
			}
		}));
		timeline.play();
	}

	/**
	 * Animates a new game
	 * 
	 * @param game the game instance
	 * @param gameScene the game's scene
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
	 * @param game game instance
	 * @param boss boss battle instance
	 * @param hsView highscore view instance
	 * @param startBtn button that starts game instance
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
				createGameOverLost(startBtn, hsView);
			}
		}));
		timeline.play();
	}

	/**
	 * Takes you to the high scores view and sets behavior of the buttons
	 * 
	 * @param game current game instance
	 * @param hsView high score view (preloaded)
	 * @param startBtn button that starts game instance
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
				initGame(startBtn, hsView);
			} else {
				hsView.handleOkButtonInput(game.getShip().getScore().get());
			}
		});

		hsView.getCloseButton().setOnAction(e -> {
			hsView.getTextField().setDisable(false);
			initGame(startBtn, hsView);
		});

		mainStage.setScene(hsView.getScene());
	}

	private void createGameOverLost(GameButton startBtn, HighScoreView hsView) {
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
		btn.setOnMouseClicked(e -> initGame(startBtn, hsView));

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
