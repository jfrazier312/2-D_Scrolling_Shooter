package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

public class GridPanes extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		initUI(primaryStage);
	}

	private void initUI(Stage primaryStage) {
		GridPane root = new GridPane();
		root.setHgap(8);
		root.setVgap(8);
		root.setPadding(new Insets(5));

		ColumnConstraints cons1 = new ColumnConstraints();
		cons1.setHgrow(Priority.NEVER);

		ColumnConstraints cons2 = new ColumnConstraints();
		cons2.setHgrow(Priority.ALWAYS);

		root.getColumnConstraints().addAll(cons1, cons2);

		RowConstraints rcons1 = new RowConstraints();
		rcons1.setVgrow(Priority.NEVER);

		RowConstraints rcons2 = new RowConstraints();
		rcons2.setVgrow(Priority.ALWAYS);

		root.getRowConstraints().addAll(rcons1, rcons2);

		Label lbl = new Label("Name:");
		TextField field = new TextField();
		ListView<String> view = new ListView<String>();
		Button okBtn = new Button("OK");
		Button closeBtn = new Button("Close");

		GridPane.setHalignment(okBtn, HPos.RIGHT);

		root.setGridLinesVisible(true);

		root.add(lbl, 0, 0);
		root.add(field, 1, 0, 3, 1);
		root.add(view, 0, 1, 4, 1);
		root.add(okBtn, 2, 2);
		root.add(closeBtn, 3, 2);

		Scene scene = new Scene(root, 280, 300);

		scene.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER) {
				handleOkButtonInput(field, view);
			}
		});

		okBtn.setOnAction(e -> {
			handleOkButtonInput(field, view);
		});

		closeBtn.setOnAction(e -> {
			Platform.exit();
		});

		primaryStage.setTitle("New folder");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void handleOkButtonInput(TextField field, ListView<String> view) {
		String text = field.getText();
		boolean isChar = true;
		for (int i = 0; i < text.length(); i++) {
			if (!Character.isLetter(text.charAt(i))) {
				field.clear();
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("Incorrect Input");
				alert.setContentText("Please input a string");
				alert.showAndWait();
				isChar = false;
				break;
			}
		}
		if (isChar && !view.getItems().contains(text)) {
			ObservableList<String> list = view.getItems();
			list.add(text);
			FXCollections.sort(list);
			FXCollections.reverse(list);
			view.setItems(list);
		}
		field.clear();
	}

}
