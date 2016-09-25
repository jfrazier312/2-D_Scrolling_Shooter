package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

// This entire file is part of my masterpiece
// Jordan Frazier

/**
 * Creates a boss battle scene. This class is called when the player has
 * survived the GameView scene for as long as the count down timer is set to.
 * 
 * @author Jordan Frazier (jrf30)
 *
 */
public class BossBattle implements GameWorld {

	private Scene bossScene;
	private VBox vbox;

	private List<Text> inputList = new ArrayList<>();
	private List<KeyCode> inputs = new ArrayList<>();

	private static final int SEQUENCE_LENGTH = 6;
	private int currentSequence = 2;
	private int inputNum = 0;
	private int textNum = 0;

	private boolean gameOverLost = false;
	private boolean gameOverWon = false;
	private boolean cheatCodeActive = false;

	public BossBattle() {
		// Must reset to false so Main.isGameWon() timeline on Main does not get
		// triggered prematurely
		gameOverLost = false;
		gameOverWon = false;

		List<Text> textList = new ArrayList<>();
		BorderPane root = setRootAndScene();
		fillDialogList(textList);
		createLaunchTimerAndButtons(textList, root);
	}

	/**
	 * Sets the root and scene for the boss battle
	 */
	private BorderPane setRootAndScene() {
		BorderPane bp = new BorderPane();
		bp.getStyleClass().add("bossBackground");
		bossScene = new Scene(bp, SCENE_WIDTH, SCENE_HEIGHT);
		bossScene.getStylesheets().add(BossBattle.class.getResource("GameStyle.css").toExternalForm());
		return bp;
	}

	/**
	 * Fills dialog list with text
	 */
	private void fillDialogList(List<Text> textList) {
		textList.add(new Text("The enemy boss is here!"));
		textList.add(new Text("We only have one missile strong enough to defeat the mothership..."));
		textList.add(new Text("That means we only have one chance to do this!"));
		textList.add(new Text("You must memorize the launch sequence as I transmit it and input it into the ship..."));
		textList.add(new Text("Good luck, and Godspeed."));
		for (Text txt : textList) {
			txt.setFill(Color.ORANGERED);
		}
	}

	/**
	 * Creates launch timer, buttons, and sets button actions
	 * 
	 * @param textList
	 *            The text list of dialog
	 * @param root
	 *            the BorderPane
	 */
	private void createLaunchTimerAndButtons(List<Text> textList, BorderPane root) {
		final SimpleIntegerProperty launchCounter = new SimpleIntegerProperty();
		Timer timer = createLaunchCheckerTimer(launchCounter);
		Button continueBtn = new Button("Continue");
		Button nextBtn = new Button("Next");
		Button okBtn = new Button("Let's do this");

		createVBox();
		vbox.getChildren().addAll(textList.get(textNum), continueBtn);
		root.setBottom(vbox);

		setButtonActions(nextBtn, okBtn, continueBtn, timer, launchCounter, textList);
	}

	/**
	 * Creates VBox to hold buttons and text
	 * 
	 */
	private void createVBox() {
		vbox = new VBox(10);
		vbox.setPadding(new Insets(50, 0, 70, 0));
		vbox.setAlignment(Pos.CENTER);
	}

	/**
	 * Creates timer that continuously checks if the launch boolean is true
	 * 
	 * @return timer
	 */
	private Timer createLaunchCheckerTimer(SimpleIntegerProperty launchCounter) {
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				Platform.runLater(new Runnable() {
					public void run() {
						checkLaunchBoolean(timer, launchCounter);
					}
				});
			}
		}, 1000 / 60, 1000 / 60);
		return timer;
	}

	/**
	 * Set Button actions on Mouse Clicked instead of On Action in case user is
	 * pressing spacebar to fire in game view, and scene shifts to boss scene.
	 * 
	 * Method sets buttons to toggle through input and launch sequence.
	 * 
	 * @param nextBtn
	 *            the next button
	 * @param okBtn
	 *            the ok button
	 * @param continueBtn
	 *            the continue button
	 * @param timer
	 *            the timer that checks for launch
	 * @param launchCounter
	 *            counts when launch is ready
	 */
	private void setButtonActions(Button nextBtn, Button okBtn, Button continueBtn, Timer timer,
			SimpleIntegerProperty launchCounter, List<Text> textList) {
		setNextButtonActions(nextBtn, okBtn);
		setContinueButtonActions(nextBtn, continueBtn, textList);
		setOkButtonActions(nextBtn, okBtn, timer, launchCounter);
		// If 'b' is pressed, cheat code is activated and triggers automatic win
		setCheatCodeAction();
	}

	private void setCheatCodeAction() {
		bossScene.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.B) {
				cheatCodeActive = true;
			}
		});
	}

	private void setOkButtonActions(Button nextBtn, Button okBtn, Timer timer, SimpleIntegerProperty launchCounter) {
		okBtn.setOnMouseClicked(e -> {
			vbox.getChildren().removeAll(inputList.get(inputNum), okBtn);
			handleLaunchInput(timer, nextBtn, launchCounter);
		});
	}

	private void setNextButtonActions(Button nextBtn, Button okBtn) {
		nextBtn.setOnMouseClicked(e -> {
			vbox.getChildren().removeAll(inputList.get(inputNum), nextBtn);
			inputNum++;
			if (inputNum < inputList.size() - 1) {
				vbox.getChildren().addAll(inputList.get(inputNum), nextBtn);
			} else if (inputNum == inputList.size() - 1) {
				vbox.getChildren().addAll(inputList.get(inputNum), okBtn);
			}
		});
	}

	private void setContinueButtonActions(Button nextBtn, Button continueBtn, List<Text> textList) {
		continueBtn.setOnMouseClicked(e -> {
			vbox.getChildren().removeAll(textList.get(textNum), continueBtn);
			textNum++;
			if (textNum < textList.size()) {
				vbox.getChildren().addAll(textList.get(textNum), continueBtn);
			} else {
				fillInputList(currentSequence);
				vbox.getChildren().addAll(inputList.get(inputNum), nextBtn);
			}
		});
	}

	/**
	 * Checks whether the launch sequence is ready to fire and win the game
	 * 
	 * @param timer
	 *            The timer checking the boolean
	 * @param launchCounter
	 *            the SimpleIntegerProperty to hold the length of launchCounter
	 */
	private void checkLaunchBoolean(Timer timer, SimpleIntegerProperty launchCounter) {
		if (launchCounter.get() == SEQUENCE_LENGTH || cheatCodeActive) {
			if (Main.DEBUG)
				System.out.println("You win!");
			timer.cancel();
			Button btn = createMissileFiringDialog();
			btn.setOnAction(e -> {
				gameOverWon = true;
			});
		}
	}

	/**
	 * Creates text and button for missile firing
	 * 
	 * @return button button to select after defeating boss
	 */
	private Button createMissileFiringDialog() {
		Text text = new Text("Missile sequence accepted. Launching missile!");
		text.setFill(Color.GHOSTWHITE);
		Button btn = new Button("Fire");
		vbox.getChildren().remove(0, vbox.getChildren().size());
		vbox.getChildren().addAll(text, btn);
		return btn;
	}

	/**
	 * Resets input list and key codes, so player can get new instructions
	 * 
	 * @param nextBtn
	 *            toggle through inputs
	 * @param launchCounter
	 *            holds length of current sequence, resets to 0
	 */
	private void resetInputList(Button nextBtn, SimpleIntegerProperty launchCounter) {
		launchCounter.set(0);
		inputNum = 0;
		
		currentSequence++;
		fillInputList(currentSequence);
		vbox.getChildren().addAll(inputList.get(inputNum), nextBtn);
	}

	/**
	 * Handles the missile sequence input logic
	 * 
	 * @param timer
	 *            timer to check boolean
	 * @param nextBtn
	 *            toggles through input
	 * @param launchCounter
	 *            holds length of current sequence
	 */
	private void handleLaunchInput(Timer timer, Button nextBtn, SimpleIntegerProperty launchCounter) {
		// blocker used to ensure multiple incorrect keys don't trigger incorrectLaunchInput() more than once
		final SimpleBooleanProperty blocker = new SimpleBooleanProperty();
		blocker.set(true);

		// Checks whether player inputs keys in correct order
		bossScene.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.B) {
				cheatCodeActive = true;
			} else if (launchCounter.get() < SEQUENCE_LENGTH) {
				if (e.getCode() == inputs.get(launchCounter.get())) {
					launchCounter.set(launchCounter.get() + 1);
					if ((launchCounter.get() == currentSequence) && (launchCounter.get() != SEQUENCE_LENGTH)) {
						resetInputList(nextBtn, launchCounter);
					}
				} else if (blocker.get() && e.getCode() != KeyCode.B) {
					blocker.set(false);
					timer.cancel();
					bossScene.setOnKeyPressed(ev -> {
						if (Main.DEBUG)
							System.out.println("Stop pressing keys. You are dead.");
					});
					incorrectLaunchInput();
				}
			}
		});
	}

	/**
	 * Method is called when player inputs incorrect input sequence. Displays
	 * button to signal game is over.
	 */
	private void incorrectLaunchInput() {
		if (Main.DEBUG)
			System.out.println("You lose");

		Button btn = new Button("Accept fate");
		Text text = new Text("Missile Malfunction! Missile exploding before launching!");
		text.setFill(Color.GHOSTWHITE);
		vbox.getChildren().addAll(text, btn);
		btn.setOnAction(e -> {
			gameOverLost = true;
		});
	}

	/**
	 * Translates the text input list to KeyCodes that can be compared to event handlers
	 * 
	 * @return returns a list of keycodes
	 */
	@SuppressWarnings("unused")
	private List<KeyCode> translateInputListToKeyCodes() {
		List<KeyCode> inputs = new ArrayList<>();
		for (Text text : inputList) {
			switch (text.getText()) {
			case "UP":
				inputs.add(KeyCode.UP);
				break;
			case "RIGHT":
				inputs.add(KeyCode.RIGHT);
				break;
			case "DOWN":
				inputs.add(KeyCode.DOWN);
				break;
			case "LEFT":
				inputs.add(KeyCode.LEFT);
				break;
			default:
				// do nothing
			}
		}
		return inputs;

	}

	/**
	 * Fills input list with keycodes text
	 * 
	 * @param len
	 *            - size of input list
	 */
	private void fillInputList(int len) {
		inputList.clear();
		inputs.clear();
		Random random = new Random();
		for (int i = 0; i < len; i++) {
			Text direction = getRandomDirection(random.nextInt(4));
			direction.setFill(Color.CADETBLUE);
			inputList.add(direction);
		}
		Text last = new Text("Input the Launch Sequence now!");
		last.setFill(Color.RED);
		inputList.add(last);
	}

	/**
	 * Gets random directions for input
	 * 
	 * @param randomNum
	 *            a random number
	 * @return Text containing direction
	 */
	private Text getRandomDirection(int randomNum) {
		Text text;
		switch (randomNum) {
		case 0:
			text = Directions.UP.toText();
			inputs.add(Directions.UP.getKeyCode());
			break;
		case 1:
			text = Directions.RIGHT.toText();
			inputs.add(Directions.RIGHT.getKeyCode());
			break;
		case 2:
			text = Directions.DOWN.toText();
			inputs.add(Directions.DOWN.getKeyCode());
			break;
		case 3:
			text = Directions.LEFT.toText();
			inputs.add(Directions.LEFT.getKeyCode());
			break;
		default:
			text = new Text();
		}
		return text;
	}

	public boolean getGameOverLost() {
		return gameOverLost;
	}

	public boolean getGameOverWon() {
		return gameOverWon;
	}

	public Scene getScene() {
		return bossScene;
	}

}
