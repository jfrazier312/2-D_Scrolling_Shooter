package game;

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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Text;

public class HighScoreView {

	private static BorderPane root;
	private static Scene highScene;

	private ListView<NameScore> view;
	private ListView<String> nameView;
	private ListView<Integer> scoreView;

	private ObservableList<String> nameList;
	private ObservableList<Integer> scoreList;

	private static final Text INPUT_MESSAGE = new Text("Input Initials:");
	private static final Text HIGHSCORE_MESSAGE = new Text("Congratulations! You defeated the boss!");
	private static final Text HIGH_SCORES = new Text("Highscores");
	private static final int MAX_LIST_SIZE = 3;

	private TextField field;
	private Integer randomScore;
	private boolean isValid;
		
	private Button closeBtn;
	private Button okBtn;
	
	public HighScoreView() {
		field = new TextField();

		root = new BorderPane();

		VBox vbox = new VBox();
		vbox.setSpacing(5.0);
		vbox.getStyleClass().add("vbox");
		
		// restricts all copy paste and numbers, does not
		// allow you to delete or any other things
		// first method to restrict input
		field = new TextField() { 
			@Override
			public void replaceText(int start, int end, String text) {
				if (text.matches("[a-zA-Z]")) {
					super.replaceText(start, end, text);
				}
			}

			@Override
			public void replaceSelection(String text) {
				if (text.matches("[a-zA-Z]")) {
					super.replaceSelection(text);
				}
			}
		};
		// second method to restrict input (third is to add listener)
		field.addEventFilter(KeyEvent.KEY_TYPED, restrictInitialMaxLength(3));

		field.setMaxWidth(100);

		HIGHSCORE_MESSAGE.getStyleClass().add("highscoreMessage");
		HIGHSCORE_MESSAGE.setFill(new LinearGradient(0f, 1f, 1f, 0f, true, CycleMethod.NO_CYCLE,
				new Stop[] { new Stop(0, Color.web("#f8bd55")), new Stop(0.14, Color.web("#c0fe56")),
						new Stop(0.28, Color.web("#5dfbc1")), new Stop(0.43, Color.web("#64c2f8")),
						new Stop(0.57, Color.web("#be4af7")), new Stop(0.71, Color.web("#ed5fc2")),
						new Stop(0.85, Color.web("#ef504c")), new Stop(1, Color.web("#f2660f")), }));
		INPUT_MESSAGE.setFill(Color.GHOSTWHITE);
		HIGH_SCORES.setFill(Color.GHOSTWHITE);
		vbox.getChildren().addAll(HIGHSCORE_MESSAGE, HIGH_SCORES, INPUT_MESSAGE, field);
		vbox.setPadding(new Insets(15));
		vbox.setAlignment(Pos.CENTER);

		okBtn = new Button("Enter");
	    closeBtn = new Button("Retry");
		HBox buttonsBox = new HBox();
		buttonsBox.setId("buttonsBox-id");
		buttonsBox.setStyle("-fx-background-color: black");
		buttonsBox.getChildren().addAll(okBtn, closeBtn);

		view = new ListView<NameScore>();

		nameView = new ListView<String>();
		nameView.setFocusTraversable(false);

		scoreView = new ListView<Integer>();
		scoreView.setFocusTraversable(false);

		HBox hbox = new HBox();
		hbox.setAlignment(Pos.CENTER);
		hbox.setPadding(new Insets(90, 50, 230, 50));
		hbox.getStyleClass().add("hbox");
		hbox.getChildren().addAll(nameView, scoreView);

		root.setTop(vbox);
		root.setCenter(hbox);
		root.setBottom(buttonsBox);

		highScene = new Scene(root, 600, 550);
		highScene.getStylesheets().add(HighScoreView.class.getResource("GameStyle.css").toExternalForm());

		field.setAlignment(Pos.CENTER);
	}

	/**
	 * Handles when initials are entered into high score view
	 * @param playerScore
	 */
	public void handleOkButtonInput(int playerScore) {
		randomScore = playerScore;
		isValid = checkIfValidInitialsInput(field.getText());
		if (isValid) {
			if (view.getItems().size() < MAX_LIST_SIZE) {
				addHighScoreName();
			} else if (scoreIsHighScore()) {
				replaceHighScoreName();
			} else {
				popupNotHighScoreAlert();
			}
		}
		field.clear();
		field.requestFocus();
	}
	

	private void popupNotHighScoreAlert() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText("You did not get a high score");
		alert.setContentText("Select close to try again.");
		alert.showAndWait();
	}

	private boolean scoreIsHighScore() {
		for (Integer elem : scoreView.getItems()) {
			if (randomScore > elem) {
				return true;
			}
		}
		return false;
	}

	private void replaceHighScoreName() {
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

	private void addHighScoreName() {
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

	private boolean checkIfValidInitialsInput(String textName) {
		if (textName.length() < 1 || textName.length() > 3) {
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

	private void actuallyAddToTheList() {
		// should be doing a clear or remove all and instantiating in global?
		nameList = FXCollections.observableArrayList();
		scoreList = FXCollections.observableArrayList();

		for (NameScore elem : view.getItems()) {
			nameList.add(elem.getName());
			scoreList.add(elem.getScore());
		}

		nameView.setItems(nameList);
		scoreView.setItems(scoreList);
		field.setDisable(true);	
	}

	private void showIncorrectInputErrorAlert() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText("Incorrect Input");
		alert.setContentText("Please input one to three initials");
		alert.showAndWait();
	}

	private EventHandler<KeyEvent> restrictInitialMaxLength(final Integer i) {
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
	
	public Button getOkButton() {
		return okBtn;
	}
	
	public Button getCloseButton() {
		return closeBtn;
	}
	
	public TextField getTextField() {
		return field;
	}

	public Scene getScene() {
		return highScene;
	}

}
