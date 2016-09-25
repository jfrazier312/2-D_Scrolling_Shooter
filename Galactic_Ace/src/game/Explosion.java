package game;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;
/**
 * This class is intended to become the animation for explosion of enemy boss 
 * and your ship. Currently not implemented. 
 * Explosion exp = new Explosion(20.0, 30.0);
 * @author Jordan Frazier
 *
 */
public class Explosion {
	
	private Path path;
	
	public Explosion(double x, double y) {
		path = new Path();
		
		path.getElements().add(new MoveTo(x, y));
		path.getElements().add(new LineTo(x + 67, y + 36));
		path.getElements().add(new LineTo(x + 109, y + 36));
		path.getElements().add(new LineTo(x + 73, y + 54));
		path.getElements().add(new LineTo(x + 83, y + 96));
		path.getElements().add(new LineTo(x + 55, y + 72));
		path.getElements().add(new LineTo(x + 27, y + 96));
		path.getElements().add(new LineTo(x + 37, y + 54));
		path.getElements().add(new LineTo(x + 1, y + 36));
		path.getElements().add(new LineTo(x + 43, 36));
		path.setScaleX(.2);
		path.setScaleY(.2);
		
		
		path.setFill(Color.ORANGERED);
		path.setStroke(Color.RED);
		
		Timeline timeline;
		timeline = new Timeline();
		timeline.setCycleCount(1);

		KeyValue keyValueX = new KeyValue(path.scaleXProperty(), 3);
		KeyValue keyValueY = new KeyValue(path.scaleYProperty(), 3);

		KeyFrame keyFrame = new KeyFrame(Duration.millis(2000), keyValueX, keyValueY);
		timeline.getKeyFrames().add(keyFrame);

		timeline.play();
		
	}
	
	public Path getPath() {
		return path;
	}

}
