package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HelloWorld extends Application {

	@Override
	public void start(Stage primaryStage) {
		initUI(primaryStage);
	}

	private void initUI(Stage primaryStage) {
		StackPane pane = new StackPane();
		Scene scene = new Scene(pane, 600, 400);

		Label label = new Label("Simple JavaFX RIA Rich Internet Application: ");
		label.setFont(Font.font("Arial", FontWeight.BOLD, 20));

		Button btn = new Button();
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(25));

		hbox.getChildren().addAll(btn, label);
		pane.getChildren().add(hbox);

		buttonFunction(btn);

		scene.getStylesheets().add(this.getClass().getResource("HelloWorld.css").toExternalForm());
		primaryStage.setTitle("Title");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void buttonFunction(Button btn) {
//		btn.setId("button-id");
		Label buttonLabel = new Label("Press on me");
		buttonLabel.getStyleClass().add("btn1--label");
		btn.getStyleClass().add("btn1"); //adds custom style class name
		btn.setGraphic(buttonLabel);
		/*
		 * btn.setOnAction(new EventHandler<ActionEvent>() {
		 * 
		 * @Override public void handle(ActionEvent e){
		 * System.out.println("Button was pressed"); } });
		 * btn.setOnMouseClicked(new EventHandler<MouseEvent>(){
		 * 
		 * @Override public void handle(MouseEvent e){
		 * System.out.println("Mouse Clicked"); } });
		 * 
		 */

		Tooltip tooltip = new Tooltip();
		tooltip.setText("Button Control");
		Tooltip.install(btn, tooltip);

		btn.setOnMouseEntered(e -> {
			System.out.println("Mouse entered button space");
		});

		btn.setOnMouseClicked(e -> { // only works after released (total click)
			System.out.println("Mouse clicked");
		});

		btn.setOnMousePressed(e -> {
			System.out.println("Mouse pressed"); // works on press
		});

		btn.setOnMouseReleased(e -> { // works before mouse clicked, after
										// pressed
			System.out.println("Mouse released");
		});

		// Mouse pressed > Button Pressed(action fire) > mouse released > mouse
		// clicked
		btn.setOnAction(e -> {
			System.out.println("Button Pressed (focus and space, mouse click, or shortcut)");
			// Platform.exit();
		});

		btn.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.B, KeyCombination.SHORTCUT_DOWN),
				new Runnable() {
					@Override
					public void run() {
						btn.fire(); // performs the setOnAction method
					}
				});

	}

	public static void main(String[] args) {
		launch(args);
	}
}
