package highScoreList;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class HighScoreList extends Application {
	
	private BorderPane root;
	private ListView<String> view;
	private TextField field;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		root = new BorderPane();
		
		field = new TextField();
		Button okBtn = new Button("OK");
		Button closeBtn = new Button("Close");
		view = new ListView<String>();
		view.setId("listview-id");
		
		root.setTop(field);
		root.setCenter(view);

		Scene scene = new Scene(root, 380, 300);

		field.setAlignment(Pos.CENTER);		

		scene.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER) {
				handleOkButtonInput();
			}
		});

		okBtn.setOnAction(e -> {
			handleOkButtonInput();
		});

		closeBtn.setOnAction(e -> {
			Platform.exit();
		});
		
		primaryStage.setTitle("New folder");
		primaryStage.setScene(scene);
		scene.getStylesheets().add(HighScoreList.class.getResource("HighScoreStyle.css").toExternalForm());
		primaryStage.show();
	}

	public void handleOkButtonInput() {
		if (view.getItems().size() >= 3) {
			replaceHighScoreName();
		}
		else {
			addHighScoreName();
		}
		
	}
	
	public void replaceHighScoreName() {
		
	}
	
	public void addHighScoreName() {
		String text = field.getText();
		boolean isChar = true;
		for (int i = 0; i < text.length(); i++) {
			if(text.charAt(i) == ' ') break;
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
