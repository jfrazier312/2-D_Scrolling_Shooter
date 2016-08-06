import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TimelineAnimation extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		Group root = new Group();
		Scene scene = new Scene(root, 800, 600, Color.WHITE);
		primaryStage.setScene(scene);
		
		final Rectangle rectBasicTimeline = new Rectangle(50, 50, 100, 50);
		rectBasicTimeline.setFill(Color.RED);
		
		root.getChildren().add(rectBasicTimeline);
		
		final Timeline timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.setAutoReverse(true);
		final KeyValue kv = new KeyValue(rectBasicTimeline.xProperty(), 300);
		final KeyFrame kf = new KeyFrame(Duration.millis(500), kv);
		timeline.getKeyFrames().add(kf);
		timeline.play();
		
		primaryStage.show();
	}
	
	public static void main(String[] args){
		launch(args);
	}
	

}
