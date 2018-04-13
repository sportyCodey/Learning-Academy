package learningacademy.userapps;

/* class DisplayMathBowl extends
 * DisplayApp. This class will add
 * some more functionality, which
 * controls the GUI and animation
 * for the corresponding app Pane.
 */

import javafx.scene.shape.Line;
import javafx.animation.*;
import javafx.event.*;
import javafx.util.Duration;
import java.util.ArrayList;
import java.io.*;

import learningacademy.AnswerSolution;
import learningacademy.DisplayApp;

public class DisplayMathBowl extends DisplayApp {
	/* animation to shoot the rectangle */
	private PathTransition shootRectangle;
	
	/* animation to shoot the text inside the rectangle */
	private PathTransition shootText;
	
	/* EventHandler that will reset the location of 
	 * both the rectangle and the text when the animation
	 * is finished
	 */
	private EventHandler<ActionEvent> resetRectangleWithText;
	
	/* the current solutions displayed to the user */
	private RectangleWithText[] solutions;
	
	/* the answer/solution pair from the Math
	 * Bowl app
	 */
	private ArrayList<AnswerSolution> pair;
	
	/* keeps track of the indices of the
	 * correct answers so  far
	 */
	private ArrayList<Integer> correctSoFar;
	
	/* the solved equations */
	private boolean solved[];
	
	public DisplayMathBowl(RandomAccessFile file) throws IOException {
		super(file, "math");
		resetArrays();
		setUpEventHandler();
		setUpAnimation();
		useGUI();
	}
	
	@Override
	public void useGUI() {
		super.useGUI();
		
		/* when the user clicks on the Shuffle button */
		mathBowlScreen.shuffle().setOnAction(e -> {
			startNewSet();
			correct = 0;
		});
		
		/* when the user clicks on these buttons */
		mathBowlScreen.skip().setOnAction(e -> {
			if (!timeAttackSetUp) {
				if (noMovement()) {
					mathBowlScreen.skipAnswer();
					alreadyAnswered();
				}
			}
		});
		
		mathBowlScreen.left().setOnAction(e -> {
			if (!timeAttackSetUp) {
				if (noMovement()) {
					mathBowlScreen.moveLeft();
				}
			}
		});
		
		mathBowlScreen.right().setOnAction(e -> {
			if (!timeAttackSetUp) {
				if (noMovement()) {
					mathBowlScreen.moveRight();
				}
			}
		});
		
		/* when the user shoots the answer to the equation */
		mathBowlScreen.fire().setOnAction(e -> {
			if (!timeAttackSetUp) {
				if (noMovement()) { 
					setUpAnimation();
					playIfPossible();
				}
			}
		});
	}//end useGUI
	
	@Override
	public void timeAttackPlay() {
		mathBowlScreen.setXLocationChoice(
				mathBowlScreen.STARTINGXLOCATIONCHOICE());
		super.timeAttackPlay();
	}
	
	
	@Override
	public void resetApp() {
		mathBowlScreen.setUpAnswersAndEquations();
		resetArrays();
		super.resetApp();
	}

	/* starts a new set of answers/solutions for the user */
	private void startNewSet() {
		mathBowlScreen.setUpAnswersAndEquations();
		correct = 0;
		resetArrays();
		mathBowlScreen.repaintActionPane();
		
	}
	
	/* resets the arrays */
	private void resetArrays() {
		pair = mathBowlScreen.getPairs();
		solutions = mathBowlScreen.solutions();
		correctSoFar = new ArrayList<>();
		solved = mathBowlScreen.solved();
	}
	
	/* plays the animation if the choicePresent hasn't
	 * been answered yet
	 */
	private void playIfPossible() {
		if (!mathBowlScreen.color().equals("LIGHTGRAY") &&
				!solved[mathBowlScreen.equationIndex()]) {
			shootRectangle.play();
			shootText.play();
		}
	}
	
	/* checks if the animation is running or not */
	private boolean noMovement() {
		if (shootRectangle.getStatus() != Animation.Status.RUNNING) {
			return true;
		}
		return false;
	}
	
	/* initializes the EventHandler which will
	 * be called when the animation is done */
	private void setUpEventHandler() {
		resetRectangleWithText = e -> {
			if (isCorrect()) {
				++correct;
				if (setCompletion()) {
					return;
				}
				
				int index = mathBowlScreen.choiceIndex();
				correctSoFar.add(index);
				
				int equationIndex = mathBowlScreen.equationIndex();
				
				/* mark that this equation has been solved 
				 * so the strike can be drawn on the
				 * rectangle holding the equation
				 */
				mathBowlScreen.setSolved(equationIndex, true);
				
				mathBowlScreen.skipAnswer();
				
				if (alreadyAnswered()) {
					return;
				}
			}//end if
			
			/* getting and setting the current x location
			 * value of the choicePresent so it 
			 * can be updated and displayed for the user
			 */
			double x = mathBowlScreen.choicePresent().getX();
			mathBowlScreen.setXLocationChoice(x);
			mathBowlScreen.repaintActionPane();
		};
	}//end setUpEventHandler
	
	/* checks if the user has completed a set of
	 * equations
	 */
	private boolean setCompletion() {
		if (correct == mathBowlScreen.SET()) {
			++completions;
			
			if(timeAttackPlay) {
				++timeAttackCount;
			}
			
			mathBowlScreen.setUserStats(completions,
					timeAttackCount, time, currentHighScore,
					timeAttackPlay);
			
			startNewSet();
			return true;
		}
		return false;
	}
	
	/* checks if the current choicePresented has already 
	 * been used and solved
	 */
	private boolean alreadyAnswered() {
		int index = mathBowlScreen.choiceIndex();
		
		if (correctSoFar.contains(index)) {
			mathBowlScreen.setChoicePresentFill();
			return true;
		}
		return false;
	}//end checkIfAlreadyAnswered
	
	/* checks if the user got the correct answer or not */
	private boolean isCorrect() {
		/* the answer the user gave */
		String answer = mathBowlScreen.choicePresent().
				getTextObject().getText();
		
		/* the current index of the equation the user
		 * just shot at
		 */
		 int equationIndex = mathBowlScreen.equationIndex();
		
		/* the index of the pair that matches
		 * the equation
		 */
		int pairIndex = findIndex(equationIndex);
	
		if (answer.equals(pair.get(pairIndex).getAnswer())) {
			return true;
		}
		return false;
	}//end isCorrect
	
	/* finds the index of the pair that matches
	 * the equation
	 */
	public int findIndex(int equationIndex) {
		for (int i = 0; i < pair.size(); ++i) {
			if (pair.get(i).getSolution().equals(
					solutions[equationIndex].getTextObject().
					getText())) {
				return i;
			}
		}
		return -1;
	}
	
	/* initializes the PathTransition */
	private void setUpAnimation() {
		shootRectangle = new PathTransition();
		shootText = new PathTransition();
		
		/* the parameters for the direction of the animation */
		double startX = mathBowlScreen.choicePresent().getX() +
				(mathBowlScreen.choicePresent().getWidth() / 2);
		double startY = mathBowlScreen.choicePresent().getY();
		double endX = startX;
		double endY = solutions[2].getY() + 30;
		
		/* the direction the shooting will take place */
		Line direction = new Line(startX, startY, endX, endY);
		
		shootRectangle.setNode(mathBowlScreen.choicePresent());
		shootRectangle.setPath(direction);
		shootRectangle.setDuration(Duration.millis(500));
		
		shootText.setNode(mathBowlScreen.answerText());
		shootText.setPath(direction);
		shootText.setDuration(Duration.millis(500));
		
		shootRectangle.setOnFinished(resetRectangleWithText);
	}//end setUpAnimation

	public static void main(String[] args) {
		System.out.println("DisplayMathBowl");
	}
}//end class DisplayMathBowl
