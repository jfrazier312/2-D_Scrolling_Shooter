package application;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("JavaFX Welcome");
		
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));
		
		Text scenetitle = new Text("Welcome");
		scenetitle.setId("welcome-id");
//		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20)); //this is inline style, better to do CSS
		grid.add(scenetitle, 0, 0, 2, 1);

		Label userName = new Label("User Name:");
		grid.add(userName, 0, 1);

		TextField userTextField = new TextField();
		grid.add(userTextField, 1, 1);

		Label pw = new Label("Password:");
		grid.add(pw, 0, 2);

		PasswordField pwBox = new PasswordField();
		grid.add(pwBox, 1, 2);
		
		//Set Grid lines visible
		grid.setGridLinesVisible(false);
		
		Button btn = new Button("Sign In");
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(btn);
		grid.add(hbBtn, 1, 3);
		
		final Text actionTarget = new Text();
		grid.add(actionTarget, 1, 5);
		
		btn.setOnAction((ActionEvent actionEvent) -> {
			actionTarget.setId("action-id");
			actionTarget.setText("Sign in button pressed");
//			actionTarget.setFill(Color.FIREBRICK); //this is inline style, better to do CSS
		});
		/*
		btn.setOnAction(new EventHandler<ActionEvent>() {		
			@Override
			public void handle(ActionEvent actionEvent){
				actionTarget.setText("Sign In button pressed");
				actionTarget.setFill(Color.FIREBRICK);	
			}
		});
		*/
		
		Scene scene = new Scene(grid, 300, 275);
		primaryStage.setScene(scene);
		scene.getStylesheets().add(Main.class.getResource("Login.css").toExternalForm());
		primaryStage.show();
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}