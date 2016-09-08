package game;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

public class Explosion {
	
	private Path path;
	
	public Explosion(double x, double y) {
		path = new Path();
		
		path.getElements().add(new MoveTo(55, 0));
		path.getElements().add(new LineTo(67, 36));
		path.getElements().add(new LineTo(109, 36));
		path.getElements().add(new LineTo(73, 54));
		path.getElements().add(new LineTo(83, 96));
		path.getElements().add(new LineTo(55, 72));
		path.getElements().add(new LineTo(27, 96));
		path.getElements().add(new LineTo(37, 54));
		path.getElements().add(new LineTo(1, 36));
		path.getElements().add(new LineTo(43, 36));
		
		
		path.setFill(Color.ORANGERED);
		path.setStroke(Color.RED);
		
		Timeline timeline;
		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.setAutoReverse(true);

		KeyValue keyValueX = new KeyValue(path.scaleXProperty(), 2);
		KeyValue keyValueY = new KeyValue(path.scaleYProperty(), 2);

		KeyFrame keyFrame = new KeyFrame(Duration.millis(2000), keyValueX, keyValueY);
		timeline.getKeyFrames().add(keyFrame);

		timeline.play();
		
	}
	
	public Path getPath() {
		return path;
	}

}
