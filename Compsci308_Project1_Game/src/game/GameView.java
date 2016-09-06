package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class GameView implements GameWorld {

	private final Random random = new Random();
	public static boolean isGameOver = false;
	private CheatCodes cheats;
	private static final int SHIP_SPEED = 400;
	private static final int BULLET_SPEED = 2;
	private boolean spaceRepeat = false;
	private Ship myShip;
	private int enemyNumber = 2;
	private static final int MAX_ENEMIES = 7;
	private Scene gameScene;
	private final Text scoreCounter = new Text();
	private List<TranslateTransition> animationList = new ArrayList<>();
	private List<Timeline> timelineList = new ArrayList<>();
	private CountDownTimer timer;
	private Group gameRoot;
	private AnimationTimer shipAnimation;
	private ParallelTransition scrollingBackground;
	
	//Use this to change how long the timer lasts before boss battle triggered
	private static final int GAME_TIME = 10;

	private List<EnemyShip> enemies = new ArrayList<EnemyShip>();

	public GameView() {
		isGameOver = false;
		// Creates game play timer
		myShip = new Ship("images/MainShip.png");
		timer = new CountDownTimer(GAME_TIME, myShip);
		gameRoot = new Group();
	}

	public void animateGame() {
		timer.startCountDown();
		EnemyShip enemy = createEnemy();
		animateEnemy(enemy);
	}

	public Scene initGame() {
		gameScene = new Scene(gameRoot, SCENE_WIDTH, SCENE_HEIGHT);

		Group backgroundGroup = new Group();
		scrollBackground(backgroundGroup);
		gameRoot.getChildren().add(backgroundGroup);
		gameRoot.getChildren().add(timer.getLabel());

		gameRoot.getChildren().add(myShip.getImageView());

		// sets score counter at top and HitPoints
		setScoreCounter();
		LongProperty lastUpdateTime = new SimpleLongProperty();
		shipAnimation = new AnimationTimer() {

			@Override
			public void handle(long timestamp) {

				double deltaX; // TODO: figure how to move correctly better
				double oldX;
				double newX;
				if (lastUpdateTime.get() > 0) {
					final double elapsedSeconds = (timestamp - lastUpdateTime.get()) / 1_000_000_000.0;
					// distance moved = rate * time
					deltaX = myShip.getShipVelocity().get() * elapsedSeconds;
					oldX = myShip.getImageView().getTranslateX();
					newX = Math.max(0, Math.min(SCENE_WIDTH - myShip.getImageView().getFitWidth(), oldX + deltaX));
					myShip.getImageView().setTranslateX(newX);
				} else {
					deltaX = 0;
					oldX = 0;
					newX = 0;
					myShip.getImageView().setTranslateX(newX);
				}
				lastUpdateTime.set(timestamp);
				if (isGameOver || timer.getTimerDone()) {
					stopAllAnimation();
				}
			}
		};

		shipAnimation.start();

		//TODO: Move key inputs to new class for concise code?
		gameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.RIGHT) {
					myShip.setShipVelocity(SHIP_SPEED);
				} else if (event.getCode() == KeyCode.LEFT) {
					myShip.setShipVelocity(-SHIP_SPEED);
				} else if (event.getCode() == KeyCode.SPACE) {
					if (!spaceRepeat && !isGameOver) {
						spaceRepeat = true;
						fireBullet(enemies);
					}
				} else if (event.getCode() == KeyCode.D) {
					getInfiniteLives();
				} else if (event.getCode() == KeyCode.F) {
					getInfiniteAmmo();
				}
			}
		});

		gameScene.setOnKeyReleased(e -> {
			if (e.getCode() == KeyCode.RIGHT) {
				if (myShip.getShipVelocity().get() > 0) {
					myShip.setShipVelocity(0);
				}
			} else if (e.getCode() == KeyCode.LEFT) {
				if (myShip.getShipVelocity().get() < 0) {
					myShip.setShipVelocity(0);
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
		// Creates score counter, hit points, and ammunition counter
		scoreCounter.textProperty().bind(Bindings.concat("Score: ").concat(myShip.getScore()).concat("\nHit Points: ")
				.concat(myShip.getHitPoints()).concat("\nBullets: ").concat(myShip.getAmmo()).concat("\n"));
		scoreCounter.setTextAlignment(TextAlignment.CENTER);
		scoreCounter.setLayoutX(SCENE_WIDTH / 2 - 43);
		scoreCounter.setLayoutY(20);
		scoreCounter.setFill(Color.RED);
		gameRoot.getChildren().add(scoreCounter);
	}

	public void updateScoreCounter() {
		scoreCounter.textProperty().bind(Bindings.concat("Score: ").concat(myShip.getScore()).concat("\nHit Points: ")
				.concat(myShip.getHitPoints()).concat("\nBullets: ").concat(myShip.getAmmo()));
	}

	public void fireBullet(final List<EnemyShip> enemies) {
		Shape bullet = new Circle(2.3, Color.GREENYELLOW);
		if (myShip.getAmmo() <= 0) {
			// do nothing
			System.out.println("Out of ammo!");
		} else {
			myShip.setAmmo(myShip.getAmmo() - 1);
			gameRoot.getChildren().add(bullet);
			TranslateTransition animation = new TranslateTransition(Duration.seconds(BULLET_SPEED), bullet);
			animationList.add(animation);
			animation.setFromX(myShip.getImageView().getTranslateX() + SHIP_WIDTH / 2);
			animation.setFromY(SCENE_HEIGHT - myShip.getImageView().getFitHeight());
			animation.setToX(myShip.getImageView().getTranslateX() + SHIP_WIDTH / 2);
			animation.setToY(-40);

			bullet.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {

				@Override
				public void changed(ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue) {
					for (EnemyShip enemy : new ArrayList<EnemyShip>(enemies)) {
						if (bullet.getBoundsInParent().intersects(enemy.getEnemyShip().getBoundsInParent())) {
							handleBulletDestroyedEnemy(bullet, animation, enemy);
						}
					}
				}
			});
			animation.setOnFinished(e -> {
				animation.stop();
				gameRoot.getChildren().remove(bullet);
			});
			updateScoreCounter();
			animation.play();
		}
	}

	public void handleBulletDestroyedEnemy(Shape bullet, TranslateTransition animation, EnemyShip enemy) {
		System.out.println("HIT ENEMY!");
		cleanUpEnemy(enemy);
		animation.stop();
		gameRoot.getChildren().remove(bullet);
		// Creates two enemies on hit every three hits (offset by 2 to start) and # enemies < MAX_ENEMIES
		if (enemyNumber % 3 == 0 && enemies.size() < MAX_ENEMIES) {
			for (int i = 0; i < 2; i++) {
				animateEnemy(createEnemy());
			}
		} else {
			animateEnemy(createEnemy());
		}
		myShip.incrementPlayerScore();
		addAmmoOnHit();
		enemyNumber++;
	}

	public void moveEnemyShip(EnemyShip enemy) {
		TranslateTransition animation = new TranslateTransition(Duration.seconds(random.nextInt(2) + 1),
				enemy.getEnemyShip());
		animationList.add(animation);
		animation.setFromX(enemy.getEnemyShip().getTranslateX());
		animation.setFromY(enemy.getEnemyShip().getTranslateY());
		animation.setToX(random.nextInt(SCENE_WIDTH) - SHIP_WIDTH);
		animation.setToY(enemy.getEnemyShip().getTranslateY() + random.nextInt(100) + 40);

		checkYBounds(enemy);

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

	public EnemyShip createEnemy() {
		// TODO: need to randomize creation of enemy position
		EnemyShip enemy = new EnemyShip("images/enemyShip.png");
		enemies.add(enemy);
		gameRoot.getChildren().add(enemy.getEnemyShip());
		return enemy;
	}

	public void animateEnemy(EnemyShip enemy) {
		moveEnemyShip(enemy);
		Timeline timeline = new Timeline();
		timelineList.add(timeline);
		KeyFrame key1 = new KeyFrame(Duration.millis(random.nextInt(2000) + 1000),
				e -> enemyFireAnimation2(timeline, enemy));
		timeline.getKeyFrames().add(key1);
		timeline.setCycleCount(1);
		timeline.setOnFinished(e -> {
			if (enemy.getAnimationStop()) {
				timeline.stop();
			} else {
				timeline.play();
			}
		});
		timeline.play();
	}

	public void enemyFireAnimation2(Timeline timeline, EnemyShip enemy) {
		System.out.println("EnemyFireAnimation Method Called");
		if (enemy.getAnimationStop()) {
			timeline.stop();
		} else {
			enemyFire(enemy);
		}
	}

	public void enemyFire(EnemyShip enemy) {
		ImageView ship = enemy.getEnemyShip();
		Shape enemyBullet = new Circle(4, Color.RED);
		gameRoot.getChildren().add(enemyBullet);
		TranslateTransition animation = new TranslateTransition(Duration.seconds(random.nextInt(3) + 1), enemyBullet);
		animationList.add(animation);
		animation.setFromX(ship.getTranslateX() + enemy.getEnemyWidth() / 2);
		animation.setFromY(ship.getTranslateY() + enemy.getEnemyHeight());
		animation.setToX(ship.getTranslateX() + enemy.getEnemyWidth() / 2);
		animation.setToY(SCENE_HEIGHT + 40);
		animation.play();

		enemyBullet.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {

			@Override
			public void changed(ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue) {
				if (enemyBullet.getBoundsInParent().intersects(myShip.getImageView().getBoundsInParent())) {
					System.out.println("ENEMY BULLET HIT ME");
					gameRoot.getChildren().remove(enemyBullet);
					myShip.decrementHitPoints();
					if (myShip.getHitPoints().get() <= 0) {
						gameRoot.getChildren().remove(myShip.getImageView());
						System.out.println("You lost all of your HitPoints");
						isGameOver = true;
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

	public void checkYBounds(EnemyShip enemy) {
		if (enemy.getEnemyShip().getTranslateY() >= (SCENE_HEIGHT)) {
			System.out.println("Removed enemy ship at bottom of screen");
			cleanUpEnemy(enemy);
			EnemyShip en = createEnemy();
			animateEnemy(en);
		}
	}

	public void cleanUpEnemy(EnemyShip enemy) {
		enemy.setAnimationStop(true);
		enemies.remove(enemy);
		gameRoot.getChildren().remove(enemy.getEnemyShip());
	}
	
	//Not used currently
	public void popupGameOverDialog() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setHeaderText("You Died!");
		alert.setContentText("You have died, and thus the world is doomed");
		alert.showAndWait();
		alert.setOnCloseRequest(e -> {
			Platform.exit();
			// lol
		});
	}

	public void stopAllAnimation() {
		shipAnimation.stop();
		scrollingBackground.stop();
		for (TranslateTransition animation : animationList) {
			if (animation != null)
				animation.stop();
		}
		for (Timeline timeline : timelineList) {
			if (timeline != null)
				timeline.stop();
		}
	}

	public void scrollBackground(Group group) {
		ImageView iv = getBackgroundImageView("images/gameBackground.gif");
		ImageView iv2 = getBackgroundImageView("images/gameBackground2.gif");
		iv2.setY(SCENE_HEIGHT);

		group.getChildren().addAll(iv, iv2);

		TranslateTransition animation = getBackgroundTransition(iv);
		TranslateTransition animation2 = getBackgroundTransition(iv2);

		scrollingBackground = new ParallelTransition(animation, animation2);
		scrollingBackground.setCycleCount(Animation.INDEFINITE);
		scrollingBackground.play();
	}

	public TranslateTransition getBackgroundTransition(ImageView iv) {
		TranslateTransition animation = new TranslateTransition(Duration.seconds(5), iv);
		animation.setFromY(0);
		animation.setToY(-1 * SCENE_HEIGHT);
		animation.setInterpolator(Interpolator.LINEAR);
		return animation;
	}

	public ImageView getBackgroundImageView(String text) {
		Image im = new Image(GameView.class.getResourceAsStream(text));
		ImageView iv = new ImageView(im);
		return iv;
	}

	public void getInfiniteLives() {
		myShip.setHitPoints(10000);
		updateScoreCounter();
	}

	public void getInfiniteAmmo() {
		myShip.setAmmo(1000000);
		updateScoreCounter();
	}

	public void addAmmoOnHit() {
		myShip.setAmmo(myShip.getAmmo() + 2);
		updateScoreCounter();
	}

	public Ship getShip() {
		return myShip;
	}
	
	public CountDownTimer getTimer() {
		return timer;
	}

}
