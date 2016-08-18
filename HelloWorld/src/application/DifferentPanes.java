package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class DifferentPanes extends Application {

	@Override
	public void start(Stage primaryStage){
		
		//Anchor Panes Buttons in bottom corner, follow screen size
		AnchorPane root = new AnchorPane();
	       
        Button okBtn = new Button("OK");
        Button closeBtn = new Button("Close");
        HBox hbox = new HBox(5, okBtn, closeBtn);

        root.getChildren().addAll(hbox);
        
        AnchorPane.setRightAnchor(hbox, 10d);
        AnchorPane.setBottomAnchor(hbox, 10d);

        Scene scene = new Scene(root, 400, 300);

        primaryStage.setTitle("Corner buttons");
        primaryStage.setScene(scene);
        primaryStage.show();
		
		
		// Horizontal Box HBox Row of buttons
		/*
		HBox hbox = new HBox();
		hbox.setSpacing(5); //between children in hbox
		hbox.setPadding(new Insets(10)); //between hbox and other elements
		hbox.setLayoutX(20);
		hbox.setLayoutY(20);
		hbox.setAlignment(Pos.BASELINE_CENTER);
		
		for(int i = 0; i < 5; i++){
			hbox.getChildren().add(new Button(String.valueOf(i)));
		}
		
		Scene scene = new Scene(hbox, 400, 330, Color.BLACK);
		primaryStage.setTitle("HBOX");
		primaryStage.setScene(scene);
		primaryStage.show();
		*/
		
		
		// Absolute positioning with Pane
		/*
		Pane rootPane = new Pane();

		Rectangle rect = new Rectangle(25, 25, 50, 50);
		rect.setFill(Color.AQUA);
		
		Line line = new Line(90, 40, 230, 40);
		line.setStroke(Color.BLACK);
		line.setStrokeWidth(5);
		
		Circle circle = new Circle(130, 130, 30);
		circle.setFill(Color.CHOCOLATE);
		
		rootPane.getChildren().addAll(rect, circle, line);
		
		Scene scene = new Scene(rootPane, 400, 330, Color.WHITESMOKE);
		
		primaryStage.setTitle("Three objects in Pane");
		primaryStage.setScene(scene);
		primaryStage.show();
		*/
		
	}
	
	public static void main(String[] args){
		launch(args);
	}
}
