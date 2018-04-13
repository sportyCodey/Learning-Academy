package learningacademy.userapps;

/* class DisplayVocabBuilder extends DisplayApp, which
 * controls the GUI and animation for the
 * "Display..." classes. class DisplayVocabBuilder
 * is specifically for the Vocabulary Builder app and 
 * will add some additional functionality. The
 * additions are mainly to handle the Rectangle
 * movements. 
 */

import javafx.scene.text.Text;
import java.util.ArrayList;
import java.io.*;

import learningacademy.AnswerSolution;
import learningacademy.DisplayApp;

public class DisplayVocabBuilder extends DisplayApp {	
	/* the user cannot drag pass these values in the screen */
	private static final int HEIGHT = 5;
	private static final int WIDTH = 700;
	private static final int BOTTOM = 465;

	/* the x location where each rectangle starts */
	private static final int STARTINGX = 10;
	
	/* the movable words that the user can drag */
	private RectangleWithText[] movableWords;
	
	/* the definitions currently present on the pane */
	private Text[] definitions;
	
	/* the index of the definition that the user drags the rectangle to */
	private int index = 0;
	
	/* the list of word/definitions pairs */
	private ArrayList<AnswerSolution> pair;
	
	public DisplayVocabBuilder(RandomAccessFile file) throws IOException {
		super(file, "vocab");
		movableWords = vocabBuilderScreen.movableWords();
		definitions = vocabBuilderScreen.definitions();
		pair = vocabBuilderScreen.getPairs();
		useGUI();
	}
	
	@Override
	public void useGUI() {
		super.useGUI();
		
		/* when the user clicks on the Shuffle button */
		vocabBuilderScreen.shuffle().setOnAction(e -> {
			vocabBuilderScreen.repaintActionPane();
			correct = 0;
		});
		
		/* being able to move the rectangles */
		movableWords[0].setOnMouseDragged(e -> {
			moveIfPossible(movableWords[0], e.getX(), e.getY());
		});
		
		movableWords[1].setOnMouseDragged(e -> {
			moveIfPossible(movableWords[1], e.getX(), e.getY());
		});
		
		movableWords[2].setOnMouseDragged(e -> {
			moveIfPossible(movableWords[2], e.getX(), e.getY());
		});
		
		movableWords[3].setOnMouseDragged(e -> {
			moveIfPossible(movableWords[3], e.getX(), e.getY());
		});
		
		movableWords[4].setOnMouseDragged(e -> {
			moveIfPossible(movableWords[4], e.getX(), e.getY());
		});
	
		/* the rectangles need to move if the user clicks on the text */
		movableWords[0].getTextObject().setOnMouseDragged(e -> {
			moveIfPossible(movableWords[0], e.getX(), e.getY());
		});
		
		movableWords[1].getTextObject().setOnMouseDragged(e -> {
			moveIfPossible(movableWords[1], e.getX(), e.getY());
		});
		
		movableWords[2].getTextObject().setOnMouseDragged(e -> {
			moveIfPossible(movableWords[2], e.getX(), e.getY());
		});
		
		movableWords[3].getTextObject().setOnMouseDragged(e -> {
			moveIfPossible(movableWords[3], e.getX(), e.getY());
		});
		
		movableWords[4].getTextObject().setOnMouseDragged(e -> {
			moveIfPossible(movableWords[4], e.getX(), e.getY());
		});
		
		/* This is when the user releases the mouse and thus
		 * releases the rectangle.
		 * If the user doesn't get the correct answer or 
		 * the rectangle isn't dragged in an appropriate place, then
		 * the rectangle should go back to its starting position.
		 * If it is correct, then the rectangle disappears.
		 */
		movableWords[0].setOnMouseReleased(e -> {
			GUICheckingRelease(movableWords[0], e.getX(), e.getY());
		});
		
		movableWords[1].setOnMouseReleased(e -> {
			GUICheckingRelease(movableWords[1], e.getX(), e.getY());
		});
		
		movableWords[2].setOnMouseReleased(e -> {
			GUICheckingRelease(movableWords[2], e.getX(), e.getY());
		});
		
		movableWords[3].setOnMouseReleased(e -> {
			GUICheckingRelease(movableWords[3], e.getX(), e.getY());
		});
		
		movableWords[4].setOnMouseReleased(e -> {
			GUICheckingRelease(movableWords[4], e.getX(), e.getY());
		});
		
		/* same thing if the user clicks on the text instead */
		movableWords[0].getTextObject().setOnMouseReleased(e -> {
			GUICheckingRelease(movableWords[0], e.getX(), e.getY());
		});
		
		movableWords[1].getTextObject().setOnMouseReleased(e -> {
			GUICheckingRelease(movableWords[1], e.getX(), e.getY());
		});
		
		movableWords[2].getTextObject().setOnMouseReleased(e -> {
			GUICheckingRelease(movableWords[2], e.getX(), e.getY());
		});
		
		movableWords[3].getTextObject().setOnMouseReleased(e -> {
			GUICheckingRelease(movableWords[3], e.getX(), e.getY());
		});
		
		movableWords[4].getTextObject().setOnMouseReleased(e -> {
			GUICheckingRelease(movableWords[4], e.getX(), e.getY());
		});
	}//end useGUI
	
	/* moves the rectangle if possible */
	private void moveIfPossible(RectangleWithText r, double x, double y) {
		if (!timeAttackSetUp) {
			if (okayBoundaries(x, y)) {
				r.setX(x);
				r.setY(y);
			}
		}
	}
	
	/* this ensures the user cannot drag the rectangles 
	 * anywhere on the pane
	 */
	private boolean okayBoundaries(double x, double y) {
		if (x < WIDTH && y > HEIGHT && y < BOTTOM) {
			return true;
		}
		return false;
	}
	
	/* This is where all the similar GUI action
	 * for the setOnRelease Lambda expressions above will be done.
	 * Firstly, it ensures the rectangle is intersecting with a
	 * definition. Then it checks if the answer is correct. */
	private void GUICheckingRelease(RectangleWithText r, double x, double y) {
		if (outsideDefBoundaries(x, y, r)) 
			setToStart(r);
		else if (isCorrect(r)) {
			vocabBuilderScreen.setVisibilityFalse(r, r.getTextObject());
			++correct;
			if (setIsDone()) {
				vocabBuilderScreen.repaintActionPane();
				correct = 0;
				++completions;
				if (timeAttackPlay) {
					++timeAttackCount;
					vocabBuilderScreen.setUserStats(completions, timeAttackCount,
							time, currentHighScore, timeAttackPlay);
				}
				else 
					vocabBuilderScreen.setUserStats(completions, 0, time,
							currentHighScore, false);
			}
		}
		else 
			setToStart(r);
	}//end GUICheckingRelease

	/* checks if the the location the user dragged the rectangle
	 * is on a definition or not
	 */
	private boolean outsideDefBoundaries(double x, double y,
			RectangleWithText r) {
		for (int i = 0; i < vocabBuilderScreen.SET(); ++i) {
			if (r.intersects(definitions[i].getBoundsInLocal())) {
				index = i;
				return false;
			}
		}
		return true;
	}
	
	/* sets the rectangle back to its starting position */
	private void setToStart(RectangleWithText r) {
		r.setX(STARTINGX);
		r.setY(r.getLocationY());
	}
	
	/* determines if the definition the user dragged to is correct
	 * or not
	 */
	private boolean isCorrect(RectangleWithText r) {
		int j = 0; //an index
		
		/* used for finding the index of the definition in the ArrayList
		 * since the ArrayList and definition array are shuffled differently
		 */
		for (int i = 0; i < vocabBuilderScreen.SET(); ++i) {
			if (pair.get(i).getSolution().equals(definitions[index].getText())) {
				j = i;
				break;
			}
		}
		
		if (r.getTextObject().getText().equals(pair.get(j).getAnswer()))
			return true;
			
		return false;
	}//end isCorrect
	
	/* determines if all the rectangles have been answered correctly */
	private boolean setIsDone() {
		if (correct == vocabBuilderScreen.SET())
			return true;
		return false;
	}
	
	public static void main(String[] args) {
		System.out.println("DisplayVocabBuilder");
	}
}//end class DisplayVocabBuilder