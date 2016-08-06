import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TransitionBasics extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Group root = new Group();
		Scene scene = new Scene(root, 800, 600, Color.BLACK);
		primaryStage.setScene(scene);
		
		final Rectangle rect1 = new Rectangle(0, 0, 40, 40);
		rect1.setArcHeight(20);
		rect1.setArcWidth(20);
		rect1.setFill(Color.RED);
		
		
		//Fade in and out transition
//		FadeTransition ft = new FadeTransition(Duration.millis(3000), rect1);
//		ft.setFromValue(1.0);
//		ft.setToValue(0.1);
//		ft.setCycleCount(Timeline.INDEFINITE);
//		ft.setAutoReverse(true);
//		ft.play();
		
		//Path moving rectangle Path transition
		Path path = new Path();
		path.getElements().add(new MoveTo(20,20));
		path.getElements().add(new CubicCurveTo(380, 0, 380, 120, 200, 120));
		path.getElements().add(new CubicCurveTo(0, 120, 0, 240, 380, 240));
		
		PathTransition pathTransition = new PathTransition();
		pathTransition.setDuration(Duration.millis(4000));
		pathTransition.setPath(path);
		pathTransition.setNode(rect1);
		pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
		pathTransition.setCycleCount(Timeline.INDEFINITE);
		pathTransition.setAutoReverse(true);
		pathTransition.play();
		
		
		//sequential vs parallel transitions 
		
		root.getChildren().add(rect1);
		
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
