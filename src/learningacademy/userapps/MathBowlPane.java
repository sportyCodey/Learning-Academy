package learningacademy.userapps;

/* class MathBowlPane extends
 * AppPane, and it adds objects
 * to the action Pane. This will be
 * the class that will set up the 
 * GUI on the screen with help
 * from it's parent. This app lets
 * the user shoot answers to the
 * correct solution. The user can also
 * skip a question if they're stuck.
 */

import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.shape.Line;
import javafx.scene.text.*;
import javafx.scene.paint.Color;
import java.util.*;

import learningacademy.AnswerSolution;
import learningacademy.AppPane;

public class MathBowlPane extends AppPane {
	/* The stating position of the choicePresent.
	 * Although this can't be incremented, a non-final
	 * local variable will copy this value and 
	 * will be incremented so the choicePresent
	 * can move locations. 
	 */
	private static final int LOCATIONXINCREMENTBY = 160;
	
	/* this is where the first equation's x position
	 * will be located
	 */
	private static final int FIRSTXLOCATIONEQUATION = 20;
	
	/* this is where the last equation's x position
	 * will be located
	 */
	private static final int LASTXLOCATIONEQUATION = 660;
	
	/* this is where the starting x location is
	 * for the choicePresent
	 */
	private static final int STARTINGXLOCATIONCHOICE = 340;
	
	/* this is where the y location of the 
	 * choicePresent will remain until shot
	 */
	private static final int LOCATIONYCHOICE = 350;
	
	/* this will be updated so the choicePresent
	 * won't restart to STARTINGXLOCATIONCHOICE every
	 * time repaintActionPane() is invoked
	 */
	private double xLocationChoice = STARTINGXLOCATIONCHOICE;
	
	/* the color of the choicePresent */
	private String color = "";
	
	/* in this case the equations are the solutions since
	 * the user is trying to match the correct answer to the
	 * right equation
	 */
	private RectangleWithText[] solutions;
	
	/* the current choice that is present on the screen
	 * for the user to solve the equation
	 */
	private RectangleWithText choicePresent;
	
	/* the text inside the choicePresent */
	private Text answerText;
	
	/* the left and right beams that will guide the shot to the equation */
	private Line leftGuide;
	private Line rightGuide;
	
	/* the strikes that go over the equation when the user 
	 * solves it
	 */
	private Line[] strikes;
	
	/* this shows which equations have been solved 
	 * to determine which strike should be over 
	 * an equation
	 */
	private boolean[] solved;
	
	/* this is used to put the equations at the 
	 * top in a random order
	 */
	private int[] randomIndices;
	
	/* buttons to move the answer to the right and the left */
	private Button left = new Button("<=");
	private Button right = new Button("=>");
	
	/* button to skip to the next answer */
	private Button skip = new Button("Skip");
	
	/* button to fire the choicePresent */
	private Button fire = new Button("Fire!");
	
	/* this is used to keep track of what choice
	 * is currently displayed for the user
	 * to solve the equation
	 */
	private int choiceIndex = 0;
	
	/* This is used to keep track of what 
	 * equation the user is currently 
	 * shooting at. It starts at index 2
	 * because that's where the choicePresent
	 * starts at.
	 */
	private int equationIndex = 2;
	
	public MathBowlPane() {
		super("Math Bowl!");
		strikes = new Line[SET];
		setUpUserInterface();
		setUpAnswersAndEquations();
		repaintActionPane();
	}
	
	@Override
	public void repaintActionPane() {
		action.getChildren().clear();
		
		/* Sets up the RectangleWithText for the equations on top.
		 * Random indices are used so the order displayed is at 
		 * random */
		int x = FIRSTXLOCATIONEQUATION;
		int y = 20;
		for (int i = 0; i < SET; ++i, x += LOCATIONXINCREMENTBY) {
			int j = randomIndices[i]; //j can be an index here
			Text t = new Text(pair.get(j).getSolution());
			t.setFont(Font.font("Segoe UI", FontWeight.BOLD, 30));
			
			solutions[i] = new RectangleWithText(x, y, 140, 60);
			solutions[i].setFill(Color.BEIGE);
			solutions[i].setStroke(Color.BLACK);
			solutions[i].setStrokeWidth(3);
			
			/*line through the rectangle when the equation has
			 * been solved
			 */
			strikes[i] = new Line(solutions[i].getX(), solutions[i].
					getY(), solutions[i].getX() + solutions[i].getWidth(),
					solutions[i].getY() + solutions[i].getHeight());
			strikes[i].setStrokeWidth(3);
			strikes[i].setStroke(Color.CADETBLUE);
			
			/* determines if the strike should show or not */
			if (solved[i])
				strikes[i].setVisible(true);
			else
				strikes[i].setVisible(false);
			
			solutions[i].setText(t);
			t.xProperty().bind(solutions[i].xProperty().add(30));
			t.yProperty().bind(solutions[i].yProperty().add(30));
			action.getChildren().addAll(solutions[i], t, strikes[i]);
		}//end for loop
		
		setButtonStyle(left);
		setButtonStyle(right);
		skip.setStyle("-fx-font-size: 40; -fx-font-family: Segoe UI;" +
		"-fx-background-color: thistle; -fx-border-width: 3;" +
				"-fx-border-color: black");
				
		skip.setTranslateX(100);
		skip.setTranslateY(430);
		
		fire.setStyle("-fx-font-size: 40;" +
				"-fx-font-family: SimSun;" +
				"-fx-background-color: red");
					
		setUpChoicePresent();
		choicePresent.setFill(Color.BLUEVIOLET);
		color = "BLUEVIOLET";
				
		action.getChildren().addAll(choicePresent, answerText, 
				leftGuide, rightGuide);
		
		/* holds the left button, skip button,
		 * and right button */
		HBox buttonHolder = new HBox(5);
		buttonHolder.getChildren().addAll(left, fire, right);
		buttonHolder.setTranslateX(266);
		buttonHolder.setTranslateY(425);
		
		action.getChildren().addAll(buttonHolder, skip);
		
		setCenter(action);
	}//end repaintActionPane
	
	/* this is so we can update the x location of
	 * the choicePresent
	 */
	public void setXLocationChoice(double x) {
		this.xLocationChoice = x;
	}
	
	public void setSolved(int index, boolean value) {
		solved[index] = value;
	}
	
	/* creates a new choicePresent and answerText
	 * and sets the color of the choicePresent
	 * to light gray, which signifies
	 * this choice as already been 
	 * solved
	 */
	public void setChoicePresentFill() {
		xLocationChoice = choicePresent.getX();
		action.getChildren().removeAll(choicePresent, answerText,
				leftGuide, rightGuide);
		
		setUpChoicePresent();	
		choicePresent.setFill(Color.LIGHTGRAY);
		color = "LIGHTGRAY";
		
		action.getChildren().addAll(choicePresent, answerText,
				leftGuide, rightGuide);
	}
	
	public void skipAnswer() {
		xLocationChoice = choicePresent.getX();
		
		++choiceIndex;
		
		if (choiceIndex > SET - 1)
			choiceIndex = 0;
	
		repaintActionPane();
	}
	
	/* moves the answer to the left */
	public void moveLeft() {
		if (choicePresent.getX() - LOCATIONXINCREMENTBY >= FIRSTXLOCATIONEQUATION) { 
			choicePresent.setX(choicePresent.getX() - LOCATIONXINCREMENTBY);
			--equationIndex;
		}
	}
	
	/* moves the answer to the right */
	public void moveRight() {
		if (choicePresent.getX() + LOCATIONXINCREMENTBY <= LASTXLOCATIONEQUATION) {
			choicePresent.setX(choicePresent.getX() + LOCATIONXINCREMENTBY);
			++equationIndex;
		}
	}
	
	/* Sets up the answer/solution pair. This
	 * also initializes important arrays. This method
	 * will also be called when the user needs
	 * a new set or when the app is reset. */
	public void setUpAnswersAndEquations() {
		/* resetting important variables */
		pair = new ArrayList<>();
		solutions = new RectangleWithText[SET];
		randomIndices = new int[SET];
		solved = new boolean[SET];
		choiceIndex = 0;
		equationIndex = 2;
		xLocationChoice = STARTINGXLOCATIONCHOICE;
		initializeRandomIndices();
		/* end resetting important variables */
		
		String[] equations = new String[SET];
		
		for (int i = 0; i < SET; ++i) {
			int num1 = randomNumber();
			int num2 = randomNumber();
			
			/* exchange if necessary */
			if (num1 < num2) {
				int temp = num1;
				num1 = num2;
				num2 = temp;
			}
			
			String operation = randomOperation();
			
			equations[i] = Integer.toString(num1) + " " +
					operation + " " + Integer.toString(num2);
			
			/* the answer that will go in answer/solution pair */
			int answer = 0;
			if (operation.equals("+")) {
				answer = num1 + num2;
			}
			else {
				answer = num1 - num2;
			}
			
			pair.add(new AnswerSolution(Integer.toString(answer),
					equations[i]));
		}//end for loop
	}//end setUpAnswersAndEquations
	
	public Button skip() {
		return skip;
	}
	
	public Button left() {
		return left;
	}
	
	public Button right() {
		return right;
	}
	
	public Button fire() {
		return fire;
	}
	
	public RectangleWithText choicePresent() {
		return choicePresent;
	}
	
	/* returns all the solutions */
	public RectangleWithText[] solutions() {
		return solutions;
	}
	
	public Text answerText() {
		return answerText;
	}
	
	public int equationIndex() {
		return equationIndex;
	}
	
	public int choiceIndex() {
		return choiceIndex;
	}
	
	public int STARTINGXLOCATIONCHOICE() {
		return STARTINGXLOCATIONCHOICE;
	}
	
	public String color() {
		return color;
	}
	
	public boolean[] solved() {
		return solved;
	}
	
	/* sets up choicePresent and objects around it */
	private void setUpChoicePresent() {
		choicePresent = new RectangleWithText(140, 60);
	
		choicePresent.setStroke(Color.BLACK);
		choicePresent.setStrokeWidth(3);
		
		answerText = new Text();
		
		answerText.setText(pair.get(choiceIndex).getAnswer());
		answerText.setFill(Color.WHITE);
		answerText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 30));
		answerText.xProperty().bind(choicePresent.xProperty().add(55));
		answerText.yProperty().bind(choicePresent.yProperty().add(38));
		
		choicePresent.setText(answerText);

		choicePresent.setX(xLocationChoice);
		choicePresent.setY(LOCATIONYCHOICE);
		
		leftGuide = new Line();
		rightGuide = new Line();
		
		setUpBeam(leftGuide, true);
		setUpBeam(rightGuide, false);
	}//end setUpChoicePresent
	
	/* spits out a random number for the equations */
	private int randomNumber() {
		return (int)(Math.random() * 20);
	}
	
	/* spits out either a + or a - String for the equations */
	private String randomOperation() {
		int num = randomNumber();
		
		if (num < 10)
			return "+";
		else
			return "-";
	}
	
	/* fills the array randomIndices with
	 * random numbers from 0 - SET - 1;
	 */
	private void initializeRandomIndices() {
		for (int i = 0; i < SET; ++i) {
			randomIndices[i] = -1;
		}
		
		boolean initializing = true;
		int counter = 0;
		while (initializing) {
			int rand = (int)(Math.random() * SET);
			
			if (!duplicate(rand, randomIndices)) {
				randomIndices[counter++] = rand;
			}
			
			if (counter > SET - 1)
				initializing = false;
		}
	}//end initializeRandomIndices
	
	/* this makes sure the random numbers that go in
	 * randomIndices are 0 - SET - 1 with no duplicates
	 */
	private boolean duplicate(int key, int[] searchArr) {
		for (int i = 0; i < searchArr.length; ++i) {
			if (searchArr[i] == key)
				return true;
		}
		return false;
	}
	
	/* Sets up the beams that guide the answer to the equation.
	 * Binding is also set up here.
	 * solutions[2] is used in binding, but 
	 * it really doesn't matter which one is used. 
	 */
	private void setUpBeam(Line beam, boolean left) {
		beam.setStrokeWidth(2);
		beam.setStroke(Color.BLUE);
		beam.getStrokeDashArray().addAll(25d, 20d, 5d, 20d);
		
		if (left) {
			beam.startXProperty().bind(choicePresent.xProperty());
			beam.startYProperty().bind(choicePresent.yProperty());
			beam.endXProperty().bind(choicePresent.xProperty());
			beam.endYProperty().bind(solutions[2].yProperty().add(
					solutions[2].getHeight()));
		}
		else {
			beam.startXProperty().bind(choicePresent.xProperty().add(
					choicePresent.widthProperty()));
			beam.startYProperty().bind(choicePresent.yProperty());
			beam.endXProperty().bind(choicePresent.xProperty().add(
					choicePresent.widthProperty()));
			beam.endYProperty().bind(solutions[2].yProperty().add(
					solutions[2].getHeight()));
		}
	}//end setUpBeam

	public static void main(String[] args) {
		System.out.println("MathBowlPane");
	}
}//end class MathBowlPane
