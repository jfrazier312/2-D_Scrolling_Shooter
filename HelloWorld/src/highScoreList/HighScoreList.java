package highScoreList;

import java.util.Random;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class HighScoreList extends Application {

	private BorderPane root;
	private ListView<NameScore> view;
	// private ListView<HashMap<String, Integer>> guy;

	private ListView<String> nameView;
	private ListView<Integer> scoreView;

	ObservableList<String> nameList;
	ObservableList<Integer> scoreList;

	private static final Text INPUT_MESSAGE = new Text("Input Initials");
	private TextField field;
	private Integer randomScore;
	private static final int MAX_LIST_SIZE = 3;
	private boolean isValid;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		root = new BorderPane();

		VBox vbox = new VBox();
		vbox.setSpacing(5.0);
		field = new TextField();
		// does not work for copy/paste
		field.addEventFilter(KeyEvent.KEY_TYPED, maxLength(3));
		
		vbox.getChildren().addAll(INPUT_MESSAGE, field);
		vbox.setPadding(new Insets(15));
		vbox.setAlignment(Pos.CENTER);

		Button okBtn = new Button("Enter");
		Button closeBtn = new Button("Close");
		HBox buttonsBox = new HBox();
		buttonsBox.setId("buttonsBox-id");
		buttonsBox.getChildren().addAll(okBtn, closeBtn);

		view = new ListView<NameScore>();

		nameView = new ListView<String>();
		nameView.setFocusTraversable(false);

		scoreView = new ListView<Integer>();
		scoreView.setFocusTraversable(false);

		HBox hbox = new HBox();
		hbox.setAlignment(Pos.CENTER);
		hbox.setPadding(new Insets(0, 50, 0, 50));
		hbox.getStyleClass().add("hbox");
		hbox.getChildren().addAll(nameView, scoreView);

		root.setTop(vbox);
		root.setCenter(hbox);
		root.setBottom(buttonsBox);

		// root.setCenter(nameView);

		Scene scene = new Scene(root, 600, 550);

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
		Random rand = new Random();
		randomScore = rand.nextInt(101);

		isValid = checkIfValidName(field.getText());
		if (isValid) {
			if (view.getItems().size() < MAX_LIST_SIZE) {
				addHighScoreName();
			} else if (scoreIsHighScore()) {
				replaceHighScoreName();
			} else {
				// do nothing
				System.out.println("You suck");
			}
		}
		field.clear();
		field.requestFocus();

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
			list.remove(MAX_LIST_SIZE - 1 - index);
			list.add(new NameScore(field.getText(), randomScore));
			FXCollections.sort(list);
			FXCollections.reverse(list);
			view.setItems(list);

			actuallyAddToTheList();
		}
	}

	public void addHighScoreName() {

		// should probably allow duplicate names
		if (isValid) {// && !nameView.getItems().contains(text)) {
			ObservableList<NameScore> list = view.getItems();
			list.add(new NameScore(field.getText(), randomScore));
			FXCollections.sort(list);
			FXCollections.reverse(list);
			view.setItems(list);

			actuallyAddToTheList();
		}
	}

	public boolean checkIfValidName(String textName) {
		if (textName.length() < 1) {
			showIncorrectInputErrorAlert();
			return false;
		}
		for (int i = 0; i < textName.length(); i++) {
			if (textName.charAt(i) == ' ') {
				showIncorrectInputErrorAlert();
				return false;
			}
			if (!Character.isLetter(textName.charAt(i))) {
				showIncorrectInputErrorAlert();
				return false;
			}
		}
		return true;
	}

	public void actuallyAddToTheList() {
		// should be doing a clear or remove all and instantiating in global
		// field
		nameList = FXCollections.observableArrayList();
		scoreList = FXCollections.observableArrayList();

		for (NameScore elem : view.getItems()) {
			nameList.add(elem.getName());
			scoreList.add(elem.getScore());
		}

		nameView.setItems(nameList);
		scoreView.setItems(scoreList);
	}

	public void showIncorrectInputErrorAlert() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText("Incorrect Input");
		alert.setContentText("Please input a string");
		alert.showAndWait();
	}

	public EventHandler<KeyEvent> maxLength(final Integer i) {
		return new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				TextField textfield = (TextField) e.getSource();
				if (textfield.getText().length() >= i) {
					e.consume();
				}
			}
		};

	}

}
