package learningacademy;

/* class AppPane is the parent class 
 * that models a Pane, which will be the screen
 * the user will be looking at. Since every app 
 * has a similar set up, there will be many 
 * classes that inherit from this class.
 */

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.*;

import java.util.ArrayList;

import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import learningacademy.Creatable;

public abstract class AppPane extends BorderPane implements Creatable {
	/* how many solutions are displayed at a time */
	protected static final byte SET = 5;
	
	/* sets the timer */
	protected Button setTimer;
		
	/* resets and timer and the app */
	protected Button reset;
		
	/* shuffles the words and thus the definitions */
	protected Button shuffle;
		
	/* game mode where user can solve the problems without any burden */
	protected Button freeStyle;
		
	/* game mode where user solves as many problems in a time limit */
	protected Button timeAttack;
		
	/* This goes back to welcome (home) screen
	 * This is overridden from interface Creatable
	 */
	protected Button homeScreen;
		
	/* the timer */
	protected Timer timer;
		
	/* pane where the action (user uses the app) takes place */
	protected Pane action;
	
	/* the answer and solution pair */
	protected ArrayList<AnswerSolution> pair;
	
	/* the title of the app */
	private String title;
	
	public AppPane(String title) {
		this.title = title;
	}
		
	@Override
	public void setUpUserInterface() {
		action = new Pane();
		
		setUserStats(0, 0, "", 0, false);
			
		/* buttons to control the app */
		setTimer = new Button("Set Timer!");
		setButtonStyle(setTimer);
			
		shuffle = new Button("Shuffle!");
		setButtonStyle(shuffle);
			
		reset = new Button("Reset");
		setButtonStyle(reset);
			
		freeStyle = new Button("Freestyle!");
		setButtonStyle(freeStyle);
			
		timeAttack = new Button("Time Attack!");
		setButtonStyle(timeAttack);
			
		HBox buttonHolder = new HBox(50, setTimer, shuffle, reset, freeStyle,
				timeAttack);
		buttonHolder.setAlignment(Pos.CENTER);
			
		timer = new Timer();
			
		BorderPane topLayer = new BorderPane();
			
		homeScreen = new Button("Back to Home");
		setButtonStyle(homeScreen);
			
		topLayer.setLeft(homeScreen);
		topLayer.setRight(timer);
			
		/* title */
		Text title = new Text(this.title);
		title.setFont(Font.font("Algerian", FontWeight.BOLD, 50));
		title.setFill(Color.BLUE);
		title.setUnderline(true);
		topLayer.setCenter(title);
			
		setTop(topLayer);
		setBottom(buttonHolder);
		
		setStyle("-fx-background-color: lightgreen");
	}//end setUpUserInterface
		
	@Override
	public void setButtonStyle(Button b) {
		b.setStyle("-fx-font-size: 20; -fx-border-width: 3;" + 
		"-fx-font-family: Segoe UI; -fx-border-color: red;" +
		"-fx-background-color: lightblue");
	}
		
	/* This sets the user statistics to the right of the BorderPane.
	 * The statistics include the setsCompleted in general and
	 * the sets completed in Time Attack mode. 
	 * It also includes the users high score in Time Attack */
	@Override
	public void setUserStats(int setsCompleted, int timeAttackSetsCompleted,
		String time, int topScore, boolean timeAttack) {
		VBox statsHolder = new VBox(15);
			
		Label stats = new Label();
		stats.setFont(Font.font("Segoe UI", FontWeight.BOLD, 30));
		stats.setText("Number of sets completed: " + setsCompleted);
			
		/* go message once the user plays the Time Attack mode */
		Text go = new Text("TIME ATTACK!\nGO!!!!");
		go.setFont(Font.font("Segoe UI", FontWeight.BOLD, 50));
		go.setFill(Color.RED);
			
		Label timeAttackStats = new Label("Time Attack sets completed: " + 
		timeAttackSetsCompleted);
		timeAttackStats.setFont(Font.font("Segoe UI", FontWeight.BOLD, 30));
			
		Label highScore = new Label();
		highScore.setFont(Font.font("Segoe UI", FontWeight.BOLD, 30));
		highScore.setText("Time Attack:\nYour time: " + time + "\nHigh Score: " + 
		topScore);
			
		statsHolder.getChildren().addAll(stats, highScore, go, timeAttackStats);
		statsHolder.setStyle("-fx-border-color: yellow;" +
		"-fx-border-width: 3");
			
		if (timeAttack) {
			go.setVisible(true);
			timeAttackStats.setVisible(true);
		}
		else {
			go.setVisible(false);
			timeAttackStats.setVisible(false);
		}
		setRight(statsHolder);
	}//end setUserStats
	
	@Override
	public void disableButtons() {
		setTimer.setDisable(true);
		shuffle.setDisable(true);
		freeStyle.setDisable(true);
		timeAttack.setDisable(true);
	}
	
	@Override
	public void enableButtons() {
		setTimer.setDisable(false);
		shuffle.setDisable(false);
		freeStyle.setDisable(false);
		timeAttack.setDisable(false);
	}
	
	@Override
	public byte SET() {
		return SET;
	}
		
	@Override
	public Button homeScreen() {
		return homeScreen;
	}
	
	@Override
	public Button setTimer() {
		return setTimer;
	}
	
	@Override
	public Button reset() {
		return reset;
	}
	
	@Override
	public Button shuffle() {
		return shuffle;
	}
	
	@Override
	public Timer timer() {
		return timer;
	}
	
	@Override
	public Button freeStyle() {
		return freeStyle;
	}
	
	@Override
	public Button timeAttack() {
		return timeAttack;
	}
	
	@Override
	public ArrayList<AnswerSolution> getPairs() {
		return pair;
	}
	
	public static void main(String[] args) {
		System.out.println("AppPane");
	}
}//end abstract class AppPane
