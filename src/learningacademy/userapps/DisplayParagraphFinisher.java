package learningacademy.userapps;

/* class DisplayParagraphFinisher will define 
 * the GUI that goes with ParagraphFinisherPane.
 * It extends DisplayApp, which has a lot of the 
 * GUI and animation already defined. However,
 * this class is more specific to the Paragraph
 * Finisher app, and it adds more functionality.
 */

import javafx.animation.Animation;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javax.swing.JOptionPane;
import java.io.*;
import java.util.ArrayList;

import learningacademy.AnswerSolution;
import learningacademy.DisplayApp;

public class DisplayParagraphFinisher extends DisplayApp {
	/* the ArrayList that holds all the answer/solution pairs */
	private ArrayList<AnswerSolution> pair;
	
	/* shows which solutions the user has answered by storing the
	 * index number
	 */
	private ArrayList<Integer> indexKeeper;
	
	/* the number of paragraphs in the set */
	private byte numOfParagraphsLeft = parFinishScreen.SET();
	
	public DisplayParagraphFinisher(RandomAccessFile file) throws IOException {
		super(file, "paragraph");
		pair = parFinishScreen.getPairs();
		indexKeeper = new ArrayList<>();
		useGUI();
	}
	
	@Override
	public void useGUI() {
		super.useGUI();
		
		/* buttons to switch between the paragraphs */
		parFinishScreen.goLeft().setOnAction(e -> {
			if (!timeAttackSetUp) {
				parFinishScreen.changeParagraphLeft();
				disableIfNeeded();
			}
		});
		
		parFinishScreen.goRight().setOnAction(e -> {
			if (!timeAttackSetUp) {
				parFinishScreen.changeParagraphRight();
				disableIfNeeded();
			}
		});
		
		/* when the user clicks on the Shuffle button */
		parFinishScreen.shuffle().setOnAction(e -> {
			startNewSet();
		});
		
		/* when the user chooses an answer by clicking on 1 of the 2 
		 * buttons present on the left
		 */
		parFinishScreen.firstChoice().setOnAction(e -> {
			updateScoreIfCorrect(parFinishScreen.firstChoice());
		});
		
		parFinishScreen.secondChoice().setOnAction(e -> {
			updateScoreIfCorrect(parFinishScreen.secondChoice());
		});
	}//end useGUI
	
	@Override
	public void timeAttackPlay() {
		/* when the user clicks on the Time Attack button */
		parFinishScreen.timeAttack().setOnAction(e -> {
			timeAttackSetUp = true;
			timeAttackPlay = true;
			resetTimer();
			parFinishScreen.disableButtons();
			parFinishScreen.repaintActionPane();
			numOfParagraphsLeft = parFinishScreen.SET();
			
			JOptionPane.showMessageDialog(null, "Set the timer above!\nPress "
					+ "the UP arrow key " +
					"to increase the time\nand the DOWN arrow key to decrease " +
					"the time.\nPress enter when you're ready to go!");
			
			parFinishScreen.setOnKeyPressed(k -> {
				parFinishScreen.requestFocus();
				if (countDown.getStatus() != Animation.Status.RUNNING && 
						timeAttackPlay) {
					switch(k.getCode()) {
					case UP: goUp(); break;
					case DOWN: if (second == 0 && minute == 0 && hour == 0) {
									second = 1;
								} 
								try { goDown(); } catch(IOException ex) { }; break;
					case ENTER: timeAttackSetUp = false;
								countDown.setCycleCount(Timeline.INDEFINITE);
								countDown.play();
								parFinishScreen.setUserStats(completions,
										0, time, currentHighScore, true);
								maybeTime = String.format
										("%02d:%02d:%02d", hour, minute, second);
								break;
					default: break;
					}//end switch
				}//end if()
			});//end setOnKeyPressed
		});//end setOnAction
	}//end timeAttackPlay
	
	@Override
	public void resetApp() {
		indexKeeper = new ArrayList<>();
		numOfParagraphsLeft = parFinishScreen.SET();
		parFinishScreen.setParagraphsLeft(numOfParagraphsLeft);
		parFinishScreen.shufflePair();
		super.resetApp();
	}
	
	/* disables the choices so the user cannot answer twice */
	private void disableIfNeeded() {
		if (alreadyBeenAnswered(parFinishScreen.getIndex())) 
			parFinishScreen.disableChoices();
		else 
			parFinishScreen.enableChoices();
	}
	
	/* updates the score if the user got the correct answer */
	private void updateScoreIfCorrect(Button b) {
		if (!timeAttackSetUp) {
			if (isCorrect(b, parFinishScreen.getIndex())) {
				--numOfParagraphsLeft;
				parFinishScreen.setParagraphsLeft(numOfParagraphsLeft);
			
				/* the user is done with this set */
				if (numOfParagraphsLeft == 0) {			
					++completions;
					
					/* if the user is playing the Time Attack game */
					if (timeAttackPlay) {
						++timeAttackCount;
						parFinishScreen.setUserStats(completions, timeAttackCount,
								time, currentHighScore, timeAttackPlay);
					}
					startNewSet();
					return;
				}
			
				parFinishScreen.changeParagraphRight();
			
				if (alreadyBeenAnswered(parFinishScreen.getIndex()))
					parFinishScreen.disableChoices();
				else
					parFinishScreen.enableChoices();
			}//end if (isCorrect())
		}
	}//end updateScoreIfCorrect
	
	/* determines if the user got the answer correct or not */
	private boolean isCorrect(Button b, int index) {
		if (b.getText().equals(pair.get(index).getAnswer())) {
			indexKeeper.add(index);
			return true;
		}
		return false;
	}//end isCorrect
	
	/* determines if the user has already solved this problem */
	private boolean alreadyBeenAnswered(int index) {
		if (indexKeeper.contains(index)) {
			return true;
		}
		return false;
	}
	
	/* creates a new set of answers/solutions for the user */
	private void startNewSet() {
		indexKeeper = new ArrayList<>();
		
		numOfParagraphsLeft = parFinishScreen.SET();
		parFinishScreen.setParagraphsLeft(numOfParagraphsLeft);
		
		parFinishScreen.setUserStats(completions, timeAttackCount, time,
				currentHighScore, timeAttackPlay);
		
		parFinishScreen.mixUp();
	}
	
	public static void main(String[] args) {
		System.out.println("DisplayParagraphFinisher");
	}
	
}//end class DisplayParagraphFinisher
