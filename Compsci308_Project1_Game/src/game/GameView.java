package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class GameView implements GameWorld {
	
	// TODO: Collision with enemy ship
	// TODO: Fix starting position with ship

	private final Random random = new Random();

	private static final int SHIP_SPEED = 400;
	private static final int BULLET_SPEED = 2;

	private boolean spaceRepeat = false;
	private Ship myShip;

	private Scene gameScene;
	private final Text scoreCounter = new Text();
	private final IntegerProperty playerScore = new SimpleIntegerProperty();

	private Group gameRoot;

	private List<EnemyShip> enemies = new ArrayList<EnemyShip>();

	public Scene initGame() {
		// Creates game scene
		gameRoot = new Group();
		gameScene = new Scene(gameRoot, SCENE_WIDTH, SCENE_HEIGHT);

		// Creates game play timer
		CountDownTimer timer = new CountDownTimer(10);
		gameRoot.getChildren().add(timer.getLabel());

		// Creates your ship
		myShip = new Ship(SCENE_WIDTH, SCENE_HEIGHT, "MainShip.png");
		gameRoot.getChildren().add(myShip.getImageView());

		// sets score counter at top and lives
		setScoreCounter();
		// Creates enemy ships
		createEnemies();

		SimpleDoubleProperty shipXVelocity = new SimpleDoubleProperty();
		LongProperty lastUpdateTime = new SimpleLongProperty();
		AnimationTimer shipAnimation = new AnimationTimer() {

			@Override
			public void handle(long timestamp) {
				double deltaX; //TODO: figure how to move correctly better algorithm
				double oldX;
				double newX;
				if (lastUpdateTime.get() > 0) {
					final double elapsedSeconds = (timestamp - lastUpdateTime.get()) / 1_000_000_000.0;
					//distance moved = rate * time
					deltaX = shipXVelocity.get() * elapsedSeconds;
					oldX = myShip.getImageView().getTranslateX();
					newX = Math.max(0,
							Math.min(SCENE_WIDTH - myShip.getImageView().getFitWidth(), oldX + deltaX));
					myShip.getImageView().setTranslateX(newX);
				} else {
					deltaX = 0;
					oldX = 0;
					newX = 0;
					myShip.getImageView().setTranslateX(newX);
				}
				lastUpdateTime.set(timestamp);
			}
		};
		shipAnimation.start();
		gameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.RIGHT) {
					shipXVelocity.set(SHIP_SPEED);
				} else if (event.getCode() == KeyCode.LEFT) {
					shipXVelocity.set(-SHIP_SPEED);
				} else if (event.getCode() == KeyCode.SPACE) {
					if (!spaceRepeat) {
						spaceRepeat = true;
						fireBullet(enemies);
					}
				}
			}
		});

		gameScene.setOnKeyReleased(e -> {
			if (e.getCode() == KeyCode.RIGHT) {
				if (shipXVelocity.get() > 0) {
					shipXVelocity.set(0);
				}
			} else if (e.getCode() == KeyCode.LEFT) {
				if (shipXVelocity.get() < 0) {
					shipXVelocity.set(0);
				}
			} else if (e.getCode() == KeyCode.SPACE) {
				spaceRepeat = false;
			}
		});
		
		return gameScene;
	}

	public Scene getGameScene() {
		return gameScene;
	}

	public void setScoreCounter() {
		scoreCounter.textProperty()
				.bind(Bindings.concat("Score: ").concat(playerScore).concat("\nLives: ").concat(myShip.getLives()));
		scoreCounter.setTextAlignment(TextAlignment.CENTER);
		scoreCounter.setLayoutX(SCENE_WIDTH / 2);
		scoreCounter.setLayoutY(20);
		scoreCounter.setFill(Color.RED);
		gameRoot.getChildren().add(scoreCounter);
	}

	public void incrementPlayerScore() {
		playerScore.set(playerScore.get() + 1);
	}

	public void fireBullet(final List<EnemyShip> enemies) {
		// create new circle bullet at top of my ship, and send it
		// translatetransition to
		// top of screen
		Shape bullet = new Circle(3, Color.RED);
		gameRoot.getChildren().add(bullet);
		TranslateTransition animation = new TranslateTransition(Duration.seconds(BULLET_SPEED), bullet);
		animation.setFromX(myShip.getImageView().getTranslateX() + SHIP_WIDTH / 2);
		animation.setFromY(SCENE_HEIGHT - myShip.getImageView().getFitHeight());
		animation.setToX(myShip.getImageView().getTranslateX() + SHIP_WIDTH / 2);
		animation.setToY(0);

		bullet.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {

			@Override
			public void changed(ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue) {
				for (EnemyShip enemy : new ArrayList<EnemyShip>(enemies)) {
					if ((Shape.intersect(enemy.getEnemyShip(), bullet)).getBoundsInLocal().getWidth() != -1) {
						System.out.println("HIT!!!!!!!!!!");
						enemies.remove(enemy);
						gameRoot.getChildren().remove(enemy.getEnemyShip());
						animation.stop();
						gameRoot.getChildren().remove(bullet);
						incrementPlayerScore();
						enemy.setAnimationStop(true);
						createEnemies();
					}
				}
			}
		});
		animation.setOnFinished(e -> {
			gameRoot.getChildren().remove(bullet);
		});
		animation.play();
	}

	public void moveEnemyShip(EnemyShip enemy) {
		TranslateTransition animation = new TranslateTransition(Duration.seconds(random.nextInt(2) + 1),
				enemy.getEnemyShip());
		animation.setFromX(enemy.getEnemyShip().getTranslateX());
		animation.setFromY(enemy.getEnemyShip().getTranslateY());
		animation.setToX(random.nextInt(SCENE_WIDTH));
		animation.setToY(enemy.getEnemyShip().getTranslateY() + random.nextInt(100) + 40);

		checkYBounds(enemy, animation);

		animation.setOnFinished(e -> {
			if (enemy.getAnimationStop()) {
				animation.stop();
			}
			moveEnemyShip(enemy);
		});
		if (!enemy.getAnimationStop()) {
			animation.play();
		}
	}

	public void createEnemies() {
		EnemyShip enemy = new EnemyShip(1);
		enemies.add(enemy);
		gameRoot.getChildren().add(enemy.getEnemyShip());
		moveEnemyShip(enemy);
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

	public void enemyFireAnimation2(EnemyShip enemy) {
		System.out.println("EnemyFireAnimation Method Called");
		enemyFire(enemy);
	}

	public void enemyFire(EnemyShip enemy) {
		Rectangle ship = enemy.getEnemyShip();
		Shape enemyBullet = new Circle(5, Color.BLUE);
		gameRoot.getChildren().add(enemyBullet);
		TranslateTransition animation = new TranslateTransition(Duration.seconds(random.nextInt(3) + 1), enemyBullet);
		animation.setFromX(ship.getTranslateX() + enemy.getEnemyWidth() / 2);
		animation.setFromY(ship.getTranslateY() + enemy.getEnemyHeight());
		animation.setToX(ship.getTranslateX() + enemy.getEnemyWidth() / 2);
		animation.setToY(SCENE_HEIGHT);
		animation.play();

		enemyBullet.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {

			@Override
			public void changed(ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue) {
				// if ((Shape.intersect(enemyBullet,
				// myShip.getImageView())).getBoundsInLocal().getWidth() != -1)
				// {
				if (enemyBullet.getBoundsInParent().intersects(myShip.getImageView().getBoundsInParent())) {
					System.out.println("ENEMY BULLET HIT ME");
					gameRoot.getChildren().remove(enemyBullet);
					myShip.decrementPlayerLives();
					if (myShip.getLives().get() <= 0) {
						gameRoot.getChildren().remove(myShip);
						System.out.println("You lost all of your lives");
					}
					animation.stop();
				}
			}
		});

		animation.setOnFinished(e -> {
			gameRoot.getChildren().remove(enemyBullet);
			animation.stop();
		});

	}

	public void checkYBounds(EnemyShip enemy, TranslateTransition animation) {
		if (enemy.getEnemyShip().getTranslateY() >= (SCENE_HEIGHT)) {
			System.out.println("REMOVED ENEMY SHIP AT BOTTOM OF SCREEN");
			enemy.setAnimationStop(true);
			enemies.remove(enemy);
			gameRoot.getChildren().remove(enemy.getEnemyShip());
			createEnemies();
		}
	}

}
