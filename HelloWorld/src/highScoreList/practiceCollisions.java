package highScoreList;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
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
	
	private List<EnemyShip> enemies = new ArrayList<EnemyShip>();
	private Rectangle rect;
	Group root;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		root = new Group();
		Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
		// 0.5 * SCENE_WIDTH - 0.5 * RECT_WIDTH
		// wont allow you to move to negatives because of the handle in
		// animation
		rect = new Rectangle(0, MAX_Y - 5, RECT_WIDTH, RECT_HEIGHT);
		root.getChildren().add(rect);

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
					final double oldX = rect.getTranslateX();
					final double newX = Math.max(MIN_X, Math.min(MAX_X, oldX + deltaX));
					rect.setTranslateX(newX);
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
		animation.setFromX(rect.getTranslateX() + RECT_WIDTH / 2);
		animation.setFromY(MAX_Y);
		animation.setToX(rect.getTranslateX() + RECT_WIDTH / 2);
		animation.setToY(-20);

		bullet.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {

			@Override
			public void changed(ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue) {
				for (EnemyShip enemy : new ArrayList<EnemyShip>(enemies)) { // why
																			// not
																			// just
																			// use
																			// old
																			// list
					if ((Shape.intersect(enemy.getEnemyShip(), bullet)).getBoundsInLocal().getWidth() != -1) {
						System.out.println("HIT!!!!!!!!!!");
						enemies.remove(enemy);
						root.getChildren().remove(enemy.getEnemyShip());
						animation.stop();
						root.getChildren().remove(bullet);
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

	public void moveEnemyShipRight(EnemyShip enemy) {
//		boolean moveLeft = false;
		TranslateTransition animation = new TranslateTransition(Duration.seconds(2), enemy.getEnemyShip());
		animation.setFromX(enemy.getEnemyShip().getTranslateX());
		animation.setFromY(enemy.getEnemyShip().getTranslateY());
//		if (moveLeft) {
//			animation.setToX(0 + enemy.getEnemyShip().getWidth());
//		} else {
			animation.setToX(random.nextInt(SCENE_WIDTH));
//
//		}
		
		animation.setToY(enemy.getEnemyShip().getTranslateY() + random.nextInt(100));
//		animation.setAutoReverse(false);
//		animation.setCycleCount(Timeline.INDEFINITE);
		animation.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				moveEnemyShipLeft(enemy);
			}
		});
		animation.play();

	}
	
	public void moveEnemyShipLeft(EnemyShip enemy) {
		TranslateTransition animation = new TranslateTransition(Duration.seconds(2), enemy.getEnemyShip());
		animation.setFromX(enemy.getEnemyShip().getTranslateX());
		animation.setFromY(enemy.getEnemyShip().getTranslateY());
		animation.setToX(random.nextInt(SCENE_WIDTH));
		animation.setToY(enemy.getEnemyShip().getTranslateY() + random.nextInt(100));
		
		animation.setOnFinished(e -> {
			moveEnemyShipRight(enemy);
		}); 
		animation.play();

	}

	public void createEnemies() {
		EnemyShip enemy = new EnemyShip(1);
		enemies.add(enemy);
		root.getChildren().add(enemy.getEnemyShip());
		moveEnemyShipRight(enemy);
	}
}
