package highScoreList;

import java.awt.List;
import java.util.HashMap;
import java.util.Random;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class HighScoreList extends Application {

	private BorderPane root;
	private ListView<NameScore> view;
	// private ListView<HashMap<String, Integer>> guy;

	private ListView<String> nameView;
	private ListView<Integer> scoreView;

	ObservableList<String> nameList;
	ObservableList<Integer> scoreList;

	private TextField field;
	private Integer randomScore;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		root = new BorderPane();

		field = new TextField();
		Button okBtn = new Button("OK");
		Button closeBtn = new Button("Close");

		view = new ListView<NameScore>();
		nameView = new ListView<String>();
		// nameView.setId("listnameView-id");
		scoreView = new ListView<Integer>();
		HBox hbox = new HBox();
		hbox.getChildren().addAll(nameView, scoreView);

		root.setTop(field);
		root.setCenter(hbox);

		// root.setCenter(nameView);

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
		// Random method to get a high score, replaced by actual high score
		// later
		Random rand = new Random();
		randomScore = rand.nextInt(101);
		String nameText = field.getText();

		if (view.getItems().size() < 3) {
			addHighScoreName();
		} else if (scoreIsHighScore()) {
			replaceHighScoreName();
		} else {
			//do nothing
			field.clear();
			System.out.println("You suck");
		}

	}

	public boolean scoreIsHighScore() {
		for (Integer elem : scoreView.getItems()) {
			if (randomScore > elem) {
				return true;
			}
		}
		return false;
	}

	public void replaceHighScoreName() {
		String text = field.getText();

		boolean isValid = checkIfValidName(text);

		if (isValid) {
			int min = 0;
			int index = 0;
			scoreList = scoreView.getItems();
			for (int i = 0; i < scoreList.size(); i++) {
				if (scoreList.get(i) < min) {
					min = scoreList.get(i);
					index = i;

				}
			}
			ObservableList<NameScore> list = view.getItems();
			list.remove(index);
			list.add(new NameScore(text, randomScore));
			FXCollections.sort(list);
			FXCollections.reverse(list);
			view.setItems(list);
			
			//should be doing a clear or remove all and instantiating in global field
			nameList = FXCollections.observableArrayList(); 
			scoreList = FXCollections.observableArrayList();

			for (NameScore elem : view.getItems()) {
				nameList.add(elem.getName());
				scoreList.add(elem.getScore());
			}

			nameView.setItems(nameList);
			scoreView.setItems(scoreList);
		}
		
		field.clear();
	}

	public void addHighScoreName() {
		String text = field.getText();

		boolean isValid = checkIfValidName(text);

		//should probably allow duplicate names
		if (isValid && !nameView.getItems().contains(text)) { 
			ObservableList<NameScore> list = view.getItems();
			list.add(new NameScore(text, randomScore));
			FXCollections.sort(list);
			FXCollections.reverse(list);
			view.setItems(list);

			nameList = FXCollections.observableArrayList();
			scoreList = FXCollections.observableArrayList();

			for (NameScore elem : view.getItems()) {
				nameList.add(elem.getName());
				scoreList.add(elem.getScore());
			}

			nameView.setItems(nameList);
			scoreView.setItems(scoreList);
		}
		field.clear();
	}

	public boolean checkIfValidName(String textName) {
		if (textName.length() < 1)
			return false;
		for (int i = 0; i < textName.length(); i++) {
			if (textName.charAt(i) == ' ')
				break;
			if (!Character.isLetter(textName.charAt(i))) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("Incorrect Input");
				alert.setContentText("Please input a string");
				alert.showAndWait();
				return false;
			}
		}
		return true;
	}

}
