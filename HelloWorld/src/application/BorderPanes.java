package application;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class BorderPanes extends Application {

	private static final double PREF_SIZE = 60;
	private BorderPane root;

	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		root = new BorderPane();

		root.setTop(getTopLabel());
		root.setBottom(getBottomLabel());
		root.setLeft(getLeftLabel());
		root.setRight(getRightLabel());
		root.setCenter(getCenterLabel());

		Scene scene = new Scene(root, 350, 300);

		primaryStage.setTitle("borderpane");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	private Label getTopLabel() {
		Label label = new MyLabel("Top");
		label.setPrefHeight(PREF_SIZE);
		label.prefWidthProperty().bind(root.widthProperty());
		label.setStyle("-fx-border-style: dotted; -fx-border-width: 0 0 1 0;"
				+ "-fx-border-color: gray; -fx-font-weight: bold");
		return label;
	}

	private Label getBottomLabel() {
		Label label = new MyLabel("Bottom");
		label.setPrefHeight(PREF_SIZE);
		label.prefWidthProperty().bind(root.widthProperty());
		label.setStyle("-fx-border-style: dotted; -fx-border-width: 1 0 0 0;"
				+ "-fx-border-color: gray; -fx-font-weight: bold");
		return label;
	}

	private Label getLeftLabel() {
		Label label = new MyLabel("Left Label");
		label.setPrefWidth(PREF_SIZE);
		label.prefHeightProperty().bind(root.heightProperty().subtract(2 * PREF_SIZE));
		label.setStyle("-fx-border-style: dotted; -fx-border-width: 0 1 0 0;"
				+ "-fx-border-color: gray; -fx-font-weight: bold");
		return label;
	}

	private Label getRightLabel() {
		Label label = new MyLabel("Right");
		label.setPrefWidth(PREF_SIZE);
		label.prefHeightProperty().bind(root.heightProperty().subtract(2 * PREF_SIZE));
		label.setStyle("-fx-border-style: dotted; -fx-border-width: 0 0 0 1;"
				+ "-fx-border-color: gray; -fx-font-weight: bold");
		return label;
	}

	private Label getCenterLabel() {
		Label label = new MyLabel("Center");
		label.setStyle("-fx-font-weight: bold");
		label.prefHeightProperty().bind(root.heightProperty().subtract(2 * PREF_SIZE));
		label.prefWidthProperty().bind(root.widthProperty().subtract(2 * PREF_SIZE));
		return label;
	}

	private class MyLabel extends Label {
		public MyLabel(String text) {
			super(text);
			setAlignment(Pos.BASELINE_CENTER);
		}
	}

}
