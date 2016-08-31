package game;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class GameView implements GameWorld {

	private Scene gameScene;
	private final Text scoreCounter = new Text();
	private final IntegerProperty playerScore = new SimpleIntegerProperty();
	private final IntegerProperty playerLives = new SimpleIntegerProperty();

	private List<EnemyShip> enemies = new ArrayList<EnemyShip>();

	public void initGame() {
		// Creates game scene
		Group root = new Group();
		gameScene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);

		// Creates game play timer
		CountDownTimer timer = new CountDownTimer(10);
		root.getChildren().add(timer.getLabel());

		// Creates your ship
		Ship myShip = new Ship(SCENE_WIDTH, SCENE_HEIGHT, "MainShip.png");
		root.getChildren().add(myShip.getImageView());

		// sets score counter at top and lives
		setPlayerLives();
		setScoreCounter();
		// Creates enemy ships
		createEnemies();

		StartScreen.mainStage.setScene(gameScene);
		StartScreen.mainStage.show();
	}

	public Scene getGameScene() {
		return gameScene;
	}

	public void setScoreCounter() {
		scoreCounter.textProperty()
				.bind(Bindings.concat("Score: ").concat(playerScore).concat("\nLives: ").concat(playerLives));
		scoreCounter.setTextAlignment(TextAlignment.CENTER);
		scoreCounter.setLayoutX(SCENE_WIDTH / 2);
		scoreCounter.setLayoutY(20);
		scoreCounter.setFill(Color.RED);
		root.getChildren().add(scoreCounter);
	}

	public void incrementPlayerScore() {
		playerScore.set(playerScore.get() + 1);
		if (playerScore.get() >= SCORE_TO_WIN) {
			HighScoreView hsView = new HighScoreView(playerScore.get());
			stage.setScene(hsView.getScene());
		}
	}

	public void decrementPlayerLives() {
		playerLives.set(playerLives.get() - 1);
	}

	public void setPlayerLives() {
		playerLives.set(3);
	}

	public void createEnemies() {
		EnemyShip enemy = new EnemyShip(1);
		enemies.add(enemy);
		root.getChildren().add(enemy.getEnemyShip());
		moveEnemyShipRight(enemy);
		Timeline timeline = new Timeline();
		KeyFrame key1 = new KeyFrame(Duration.millis(random.nextInt(2000) + 1000), e -> enemyFireAnimation2(enemy));
		timeline.getKeyFrames().add(key1);
		timeline.setCycleCount(1);
		timeline.setOnFinished(e -> {
			if (!enemy.getAnimationStop()) {
				timeline.play();
			}
		});
		timeline.play(); // TODO: HOW DO I STOP THIS ANIMATION
	}

}
