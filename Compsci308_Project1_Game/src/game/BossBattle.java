package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class BossBattle implements GameWorld {

	private Random random = new Random();
	private Scene bossScene;
	private VBox vbox;
	private List<Text> textList = new ArrayList<>();
	private int textNum = 0;
	private List<Text> inputList = new ArrayList<>();
	private int inputNum = 0;
	private boolean launch = true;
	private static final int SEQUENCE_LENGTH = 5;
	private int launchCounter = 0;
	private List<KeyCode> inputs;
	public static boolean gameOverLost = false;
	public static boolean gameOverWon = false;
	private boolean cheatCodeActive = false;
	
	public BossBattle() {
		gameOverLost = false;
		gameOverWon = false;
		BorderPane root = new BorderPane();
		root.getStyleClass().add("bossBackground");
		bossScene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
		bossScene.getStylesheets().add(BossBattle.class.getResource("GameStyle.css").toExternalForm());

		//Fills lists with dialog and launch sequence instructions
		fillTextList();
		fillInputList();

		Button continueBtn = new Button("Continue");
		Button nextBtn = new Button("Next");
		Button okBtn = new Button("Let's do this");
		vbox = new VBox(10);
		vbox.setPadding(new Insets(50, 0, 70, 0));
		vbox.setAlignment(Pos.CENTER);
		vbox.getChildren().addAll(textList.get(textNum), continueBtn);
		root.setBottom(vbox);
		
		/*
		 * Used setOnMouseClicked instead of setOnAction because user might be pressing space
		 * to fire, and accidentally toggle through dialog too quickly.
		 */
		continueBtn.setOnMouseClicked(e -> {
			vbox.getChildren().removeAll(textList.get(textNum), continueBtn);
			textNum++;
			if (textNum < textList.size()) {
				vbox.getChildren().addAll(textList.get(textNum), continueBtn);
			} else {
				vbox.getChildren().addAll(inputList.get(inputNum), nextBtn);
			}
		});

		nextBtn.setOnMouseClicked(e -> {
			vbox.getChildren().removeAll(inputList.get(inputNum), nextBtn);
			inputNum++;
			if (inputNum < inputList.size() - 1) {
				vbox.getChildren().addAll(inputList.get(inputNum), nextBtn);
			} else if (inputNum == inputList.size() - 1) {
				vbox.getChildren().addAll(inputList.get(inputNum), okBtn);
			}
		});
		
		okBtn.setOnMouseClicked(e -> {
			vbox.getChildren().removeAll(inputList.get(inputNum), okBtn);
			handleLaunchInput();
		});
		
		bossScene.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.B) {
				cheatCodeActive = true;
			}
		});
		
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				checkLaunchBoolean(timer);
			}
		}, 1000 / 60, 1000 / 60);
	}

	public void checkLaunchBoolean(Timer timer) {
		if (!launch) {
//			gameOverLost = true;
		} else if (launchCounter == SEQUENCE_LENGTH || cheatCodeActive) {
			System.out.println("You win!");
			timer.cancel();
			gameOverWon = true;
		}
	}

	public void handleLaunchInput() {
		inputs = translateInputListToKeyCodes();
		bossScene.setOnKeyPressed(e -> {
			if (launchCounter < SEQUENCE_LENGTH) {
				if (e.getCode() == inputs.get(launchCounter)) {
					launch = true;
					launchCounter++;
				} else {
					Button btn = new Button("Accept fate");
					Text text = new Text("Missile Malfunction! Missile exploding before launching!");
					text.setFill(Color.GHOSTWHITE);
					vbox.getChildren().addAll(text, btn);
					System.out.println("You lose");
					btn.setOnAction(event -> {
						gameOverLost  = true;
					});
					launch = false;
				}
			}
		});
	}

	public List<KeyCode> translateInputListToKeyCodes() {
		List<KeyCode> input = new ArrayList<>();
		for (Text text : inputList) {
			switch (text.getText()) {
			case "UP":
				input.add(KeyCode.UP);
				break;
			case "RIGHT":
				input.add(KeyCode.RIGHT);
				break;
			case "DOWN":
				input.add(KeyCode.DOWN);
				break;
			case "LEFT":
				input.add(KeyCode.LEFT);
				break;
			default:
				// do nothing
			}
		}

		return input;
	}

	public void fillInputList() {
		for (int i = 0; i < SEQUENCE_LENGTH; i++) {
			Text direction = getRandomDirection();
			direction.setFill(Color.CADETBLUE);
			inputList.add(direction);
		}
		Text last = new Text("Input the Launch Sequence now!");
		last.setFill(Color.RED);
		inputList.add(last);
	}

	public void fillTextList() {
		textList.add(new Text("The enemy boss is here!"));
		textList.add(new Text("We only have one missile strong enough to defeat the mothership..."));
		textList.add(new Text("That means we only have one chance to do this!"));
		textList.add(new Text("You must memorize the launch sequence as I transmit it and input it into the ship..."));
		textList.add(new Text("Good luck, and Godspeed."));
		for(Text txt : textList) {
			txt.setFill(Color.ORANGERED);
		}
	}

	public Text getRandomDirection() {
		int num = random.nextInt(4);
		Text text;
		switch (num) {
		case 0:
			text = new Text("UP");
			break;
		case 1:
			text = new Text("RIGHT");
			break;
		case 2:
			text = new Text("DOWN");
			break;
		case 3:
			text = new Text("LEFT");
			break;
		default:
			text = new Text();
		}
		return text;
	}
	
	public Scene getScene() {
		return bossScene;
	}

}
