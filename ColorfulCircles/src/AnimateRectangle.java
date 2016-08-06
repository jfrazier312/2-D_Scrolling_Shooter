import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class AnimateRectangle extends Application {

	private static final int SCENE_WIDTH = 800;
	private static final int SCENE_HEIGHT = 600;
	private static final double KEY_INPUT_SPEED = 5;
	private static final double RECTANGLE_SPEED = 500;

	private static final double RECTANGLE_WIDTH = 50;
	private static final double RECTANGLE_HEIGHT = 50;
	private static final double MIN_X = 0;
	private static final double MAX_X = SCENE_WIDTH - RECTANGLE_WIDTH; // should
																		// bind
																		// to
																		// scene
	// instead with property
	private static final double MIN_Y = 0;
	private static final double MAX_Y = SCENE_HEIGHT - RECTANGLE_HEIGHT; // should
																			// bind
																			// to
																			// scene
	// instead with property
	private static final double GROWTH_RATE = 1.1;

	private Circle circle;
	private Rectangle rect;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Group root = new Group();
		Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT, Color.WHITE);

		rect = new Rectangle();
		rect.setWidth(RECTANGLE_WIDTH);
		rect.setHeight(RECTANGLE_HEIGHT);
		root.getChildren().add(rect);

		primaryStage.setScene(scene);
		primaryStage.show();

		final DoubleProperty rectangleXVelocity = new SimpleDoubleProperty();
		final DoubleProperty rectangleYVelocity = new SimpleDoubleProperty();
		final LongProperty lastUpdateTime = new SimpleLongProperty();
		final AnimationTimer rectangleAnimation = new AnimationTimer() {

			@Override
			public void handle(long timestamp) {
				if (lastUpdateTime.get() > 0) {
					final double elapsedSeconds = (timestamp - lastUpdateTime.get()) / 1_000_000_000.0;

					final double deltaX = elapsedSeconds * rectangleXVelocity.get();
					final double oldX = rect.getTranslateX();
					final double newX = Math.max(MIN_X, Math.min(MAX_X, oldX + deltaX));
					rect.setTranslateX(newX);

					final double deltaY = elapsedSeconds * rectangleYVelocity.get();
					final double oldY = rect.getTranslateY();
					final double newY = Math.max(MIN_Y, Math.min(MAX_Y, oldY + deltaY));
					rect.setTranslateY(newY);
				}
				lastUpdateTime.set(timestamp);
			}
		};
		rectangleAnimation.start();

		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.RIGHT) {
					rectangleXVelocity.set(RECTANGLE_SPEED);
				} else if (event.getCode() == KeyCode.LEFT) {
					rectangleXVelocity.set(-RECTANGLE_SPEED);
				} else if (event.getCode() == KeyCode.UP) {
					rectangleYVelocity.set(-RECTANGLE_SPEED);
				} else if (event.getCode() == KeyCode.DOWN) {
					rectangleYVelocity.set(RECTANGLE_SPEED);
				}
			}
		});

		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.DOWN) {
					if (rectangleYVelocity.get() > 0) {
						rectangleYVelocity.set(0);
					}
				} else if (event.getCode() == KeyCode.UP) {
					if (rectangleYVelocity.get() < 0) {
						rectangleYVelocity.set(0);
					}
				} else if (event.getCode() == KeyCode.LEFT) {
					if (rectangleXVelocity.get() < 0) {
						rectangleXVelocity.set(0);
					}
				} else if (event.getCode() == KeyCode.RIGHT) {
					if (rectangleXVelocity.get() > 0) {
						rectangleXVelocity.set(0);
					}
				}
			}
		});

	}

	/*
	 * @Override public void start(Stage primaryStage) throws Exception {
	 * 
	 * Group group = new Group(); Scene scene = initScene(SCENE_WIDTH,
	 * SCENE_HEIGHT);
	 * 
	 * primaryStage.setScene(scene); primaryStage.show();
	 * 
	 * }
	 * 
	 * public Scene initScene(int width, int height){ Group root = new Group();
	 * Scene scene = new Scene(root, width, height, Color.WHITE);
	 * 
	 * circle = new Circle(); circle.setCenterX(400); circle.setCenterY(150);
	 * circle.setRadius(100); circle.setFill(Color.AQUA); circle.setEffect(new
	 * BoxBlur(10, 10, 3));
	 * 
	 * 
	 * DropShadow ds = new DropShadow(); ds.setColor(Color.BLACK);
	 * ds.setRadius(10); ds.setOffsetX(4.0); ds.setOffsetY(10.0);
	 * 
	 * rect = new Rectangle(); rect.setWidth(600); rect.setHeight(100);
	 * rect.setX(100); rect.setY(350); rect.setFill(Color.DARKKHAKI);
	 * rect.setEffect(new BoxBlur(10, 10, 1)); rect.setEffect(ds);
	 * 
	 * root.getChildren().addAll(circle, rect);
	 * 
	 * scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
	 * 
	 * @Override public void handle(KeyEvent e){ handleKeyInput(e.getCode()); }
	 * }); scene.setOnMouseClicked((e) -> handleMouseInput(e.getX(), e.getY()));
	 * return scene; }
	 * 
	 * // What to do each time a key is pressed private void handleKeyInput
	 * (KeyCode code) { switch (code) { case RIGHT: rect.setX(rect.getX() +
	 * KEY_INPUT_SPEED); break; case LEFT: rect.setX(rect.getX() -
	 * KEY_INPUT_SPEED); break; case UP: rect.setY(rect.getY() -
	 * KEY_INPUT_SPEED); break; case DOWN: rect.setY(rect.getY() +
	 * KEY_INPUT_SPEED); break; default: // do nothing } }
	 * 
	 * private void handleMouseInput (double x, double y) { if
	 * (circle.contains(x, y)) { //this will change the actual size of your
	 * node, so you can click anywhere circle is visible to invoke this method
	 * circle.setRadius(circle.getRadius() * GROWTH_RATE);
	 * 
	 * //Scales object without altering the original bounds layout, so ideal for
	 * scaling the entire node after all effects and transofmrs have taken place
	 * // circle.setScaleX(circle.getScaleX() * GROWTH_RATE); //
	 * circle.setScaleY(circle.getScaleY() * GROWTH_RATE); } }
	 */
}
