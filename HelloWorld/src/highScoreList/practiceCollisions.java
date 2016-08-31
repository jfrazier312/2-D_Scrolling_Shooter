package highScoreList;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
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
import javafx.stage.Stage;
import javafx.util.Duration;

public class practiceCollisions extends Application {

	private final Random random = new Random();

	private static final int RECT_SPEED = 400;
	private static final int BULLET_SPEED = 2;
	private static final int SCENE_WIDTH = 800;
	private static final int SCENE_HEIGHT = 600;
	private static final int RECT_WIDTH = 40;
	private static final int RECT_HEIGHT = 40;
	private static final int MIN_X = 0;
	private static final int MIN_Y = 0;
	private static final int MAX_X = SCENE_WIDTH - RECT_WIDTH;
	private static final int MAX_Y = SCENE_HEIGHT - RECT_HEIGHT;

	private boolean spaceRepeat = false;

	private LongProperty lastUpdateTimeEnemyFire = new SimpleLongProperty();

	private float ENEMY_FIRE_RATE = 3_000_000_000f;
	private static final int SCORE_TO_WIN = 3;

	private final Text scoreCounter = new Text();
	private final IntegerProperty playerScore = new SimpleIntegerProperty();
	private final IntegerProperty playerLives = new SimpleIntegerProperty();

	private List<EnemyShip> enemies = new ArrayList<EnemyShip>();
	private Rectangle myShip;
	private Group root;

	private Stage stage;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		stage = primaryStage;
		root = new Group();
		Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
		// 0.5 * SCENE_WIDTH - 0.5 * RECT_WIDTH
		// wont allow you to move to negatives because of the handle in
		// animation
		myShip = new Rectangle(0, MAX_Y - 5, RECT_WIDTH, RECT_HEIGHT);
		root.getChildren().add(myShip);

		// sets score counter at top and lives
		setPlayerLives();
		setScoreCounter();
		// Creates enemy ships
		createEnemies();

		primaryStage.setTitle("Collision Practice");
		primaryStage.setScene(scene);
		primaryStage.show();

		SimpleDoubleProperty rectangleXVelocity = new SimpleDoubleProperty();
		LongProperty lastUpdateTime = new SimpleLongProperty();
		AnimationTimer rectangleAnimation = new AnimationTimer() {

			@Override
			public void handle(long timestamp) {
				if (lastUpdateTime.get() > 0) {
					final double elapsedSeconds = (timestamp - lastUpdateTime.get()) / 1_000_000_000.0;
					final double deltaX = elapsedSeconds * rectangleXVelocity.get();
					final double oldX = myShip.getTranslateX();
					final double newX = Math.max(MIN_X, Math.min(MAX_X, oldX + deltaX));
					myShip.setTranslateX(newX);
				}
				lastUpdateTime.set(timestamp);
			}
		};
		rectangleAnimation.start();
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.RIGHT) {
					rectangleXVelocity.set(RECT_SPEED);
				} else if (event.getCode() == KeyCode.LEFT) {
					rectangleXVelocity.set(-RECT_SPEED);
				} else if (event.getCode() == KeyCode.SPACE) {
					if (!spaceRepeat) {
						spaceRepeat = true;
						fireBullet(enemies);
					}
				}
			}
		});

		scene.setOnKeyReleased(e -> {
			if (e.getCode() == KeyCode.RIGHT) {
				if (rectangleXVelocity.get() > 0) {
					rectangleXVelocity.set(0);
				}
			} else if (e.getCode() == KeyCode.LEFT) {
				if (rectangleXVelocity.get() < 0) {
					rectangleXVelocity.set(0);
				}
			} else if (e.getCode() == KeyCode.SPACE) {
				spaceRepeat = false;
			}
		});

	}

	public void fireBullet(final List<EnemyShip> enemies) {
		// create new circle at top of my ship, and send it trasnletransition to
		// top of screen
		Shape bullet = new Circle(3, Color.RED);
		root.getChildren().add(bullet);
		TranslateTransition animation = new TranslateTransition(Duration.seconds(BULLET_SPEED), bullet);
		animation.setFromX(myShip.getTranslateX() + RECT_WIDTH / 2);
		animation.setFromY(MAX_Y);
		animation.setToX(myShip.getTranslateX() + RECT_WIDTH / 2);
		animation.setToY(-20);

		bullet.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {

			@Override
			public void changed(ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue) {
				for (EnemyShip enemy : new ArrayList<EnemyShip>(enemies)) {
					if ((Shape.intersect(enemy.getEnemyShip(), bullet)).getBoundsInLocal().getWidth() != -1) {
						System.out.println("HIT!!!!!!!!!!");
						enemies.remove(enemy);
						root.getChildren().remove(enemy.getEnemyShip());
						animation.stop();
						root.getChildren().remove(bullet);
						incrementPlayerScore();
						enemy.setAnimationStop(true);
						createEnemies();
					}
				}
			}
		});

		animation.setOnFinished(e -> {
			root.getChildren().remove(bullet);
		});

		animation.play();
	}

	// either move randomly up and sideways, or if they go all the way down then
	// you lose a life/points
	public void moveEnemyShipRight(EnemyShip enemy) {
		// boolean moveLeft = false;
		TranslateTransition animation = new TranslateTransition(Duration.seconds(2), enemy.getEnemyShip());
		animation.setFromX(enemy.getEnemyShip().getTranslateX());
		animation.setFromY(enemy.getEnemyShip().getTranslateY());
		// if (moveLeft) {
		// animation.setToX(0 + enemy.getEnemyShip().getWidth());
		// } else {
		animation.setToX(random.nextInt(SCENE_WIDTH));
		//
		// }
		animation.setToY(enemy.getEnemyShip().getTranslateY() + random.nextInt(100) + 40);

		checkYBounds(enemy, animation);
		// animation.setAutoReverse(false);
		// animation.setCycleCount(Timeline.INDEFINITE);
		animation.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				// if (enemy.getAnimationStop()) {
				// animation.stop();
				// }
				// animation.stop();
				moveEnemyShipLeft(enemy);
			}
		});
		if (!enemy.getAnimationStop()) {
			animation.play();
		}
	}

	public void moveEnemyShipLeft(EnemyShip enemy) {
		TranslateTransition animation = new TranslateTransition(Duration.seconds(2), enemy.getEnemyShip());
		animation.setFromX(enemy.getEnemyShip().getTranslateX());
		animation.setFromY(enemy.getEnemyShip().getTranslateY());
		animation.setToX(random.nextInt(SCENE_WIDTH));
		animation.setToY(enemy.getEnemyShip().getTranslateY() + random.nextInt(100) + 40);

		checkYBounds(enemy, animation);

		animation.setOnFinished(e -> {
//			if (enemy.getAnimationStop()) {
//				animation.stop();
//			}
			moveEnemyShipRight(enemy);
		});
		if (!enemy.getAnimationStop()) {
			animation.play();
		}
	}

	public void checkYBounds(EnemyShip enemy, TranslateTransition animation) {
		if (enemy.getEnemyShip().getTranslateY() >= (SCENE_HEIGHT)) {
			System.out.println("REMOVED ENEMY SHIP AT BOTTOM OF SCREEN");
			enemy.setAnimationStop(true);
			enemies.remove(enemy);
			root.getChildren().remove(enemy.getEnemyShip());
			createEnemies();
		}
	}

	public void createEnemies() {
		EnemyShip enemy = new EnemyShip(1);
		enemies.add(enemy);
		root.getChildren().add(enemy.getEnemyShip());
		moveEnemyShipRight(enemy);
		Timeline timeline = new Timeline();
		KeyFrame key1 = new KeyFrame(Duration.millis(random.nextInt(2000) + 1000),
				e -> enemyFireAnimation2(enemy));
		timeline.getKeyFrames().add(key1);
		timeline.setCycleCount(1);
		timeline.setOnFinished(e -> {
			if(!enemy.getAnimationStop()) {
				timeline.play();
			}
		});
		timeline.play(); // TODO: HOW DO I STOP THIS ANIMATION
	}
	
	public void enemyFireAnimation2(EnemyShip enemy){
		System.out.println("EnemyFireAnimation Method Called");
		enemyFire(enemy);
	}

	@Deprecated
	public void enemyFireAnimation(EnemyShip enemy) {
		AnimationTimer animation = new AnimationTimer() {
			@Override
			public void handle(long now) {
				if (now - lastUpdateTimeEnemyFire.get() > ENEMY_FIRE_RATE && !enemy.getAnimationStop()) {
					System.out.println("ENEMY FIRE");
					System.out.println(now - lastUpdateTimeEnemyFire.get() + "::::::" + ENEMY_FIRE_RATE);
					enemyFire(enemy);
					lastUpdateTimeEnemyFire.set(now);
				}
			}
		};
		animation.start();
		if (enemy.getAnimationStop()) {
			animation.stop();
		}
	}

	public void enemyFire(EnemyShip enemy) {
		Rectangle ship = enemy.getEnemyShip();
		Shape enemyBullet = new Circle(5, Color.BLUE);
		root.getChildren().add(enemyBullet);
		TranslateTransition animation = new TranslateTransition(Duration.seconds(random.nextInt(3) + 1), enemyBullet);
		animation.setFromX(ship.getTranslateX() + enemy.getEnemyWidth() / 2);
		animation.setFromY(ship.getTranslateY() + enemy.getEnemyHeight());
		animation.setToX(ship.getTranslateX() + enemy.getEnemyWidth() / 2);
		animation.setToY(SCENE_HEIGHT);
		animation.play();

		enemyBullet.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {

			@Override
			public void changed(ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue) {
				if ((Shape.intersect(enemyBullet, myShip)).getBoundsInLocal().getWidth() != -1) {
					System.out.println("ENEMY BULLET HIT ME");
					root.getChildren().remove(enemyBullet);
					decrementPlayerLives();
					if (playerLives.get() <= 0) {
						root.getChildren().remove(myShip);
						System.out.println("You lost all of your lives");
					}
					animation.stop();
				}
			}
		});

		animation.setOnFinished(e -> {
			root.getChildren().remove(enemyBullet);
			animation.stop();
		});

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

}
