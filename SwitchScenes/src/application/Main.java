package application;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class Main extends Application {
	
	FlowPane flowPane1, flowPane2;
	Stage stage;
	Scene scene1, scene2;
	Button btn1, btn2;
	Label label1, label2;
	
	@Override
	public void start(Stage primaryStage) {
		stage = primaryStage;
		
		btn1 = new Button("Click here to go to scene 2");
		btn1.setOnAction(e -> handleButtonAction(e));
		label1 = new Label("This is scene 1");	
		flowPane1 = new FlowPane();
		flowPane1.getChildren().addAll(btn1, label1);
		flowPane1.setPadding(new Insets(20));
		flowPane1.setVgap(20);
		flowPane1.setStyle("-fx-background: red");
		
		
		btn2 = new Button("Click here to go to scene 1");
		btn2.setOnAction(e -> handleButtonAction(e));
		label2 = new Label("This is scene 2");		
		flowPane2 = new FlowPane();
		flowPane2.getChildren().addAll(btn2, label2);
		flowPane2.setPadding(new Insets(20));
		flowPane2.setVgap(20);
		flowPane2.setStyle("-fx-background: tan");
		
		scene1 = new Scene(flowPane1, 250, 100);
		scene2 = new Scene(flowPane2, 250, 100);
		
		stage.setTitle("Switching Scene");
		stage.setScene(scene1);
		stage.show();

	}
	
	public void handleButtonAction(ActionEvent e) {
		if(e.getTarget() == btn2){
			stage.setScene(scene1);
		} else {
			stage.setScene(scene2);
		}
		
	}

	public static void main(String[] args) {
		launch(args);
	}
}
