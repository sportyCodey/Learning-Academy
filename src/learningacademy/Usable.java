package learningacademy;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import java.io.IOException;

/* interface Usable models the skeleton for classes
 * that are Usable. That means the GUI and/or animation is heavily
 * defined in that class. This interface is mostly for the 
 * "Display..." classes.
 */

public interface Usable {
	/* lets the user use the GUI displayed */
	void useGUI();
	
	/* lets the user play the Time Attack game */
	void timeAttackPlay();
	
	/* resets the app */
	void resetApp();
	
	/* increments the timer */
	void goUp();
	
	/* decrements the timer */
	void goDown() throws IOException;
	
	/* grows and shrinks the button when the mouse is entered and exited */
	void buttonGrowShrink(Button b, double h, double w);
	
	/* resets the timer */
	void resetTimer();
	
	/* This updates the high score if necessary.
	 * Since the user sets their time, it's up to the user
	 * to really challenge themselves.
	 */
	void updateHighScore() throws IOException;
	
	/* writes the new high score to the file */
	void writeToFile() throws IOException;
	
	/* Gets the high score for the user to see when they enter the app.
	 * The index specifies
	 * which file pointer to receive. */ 
	void getHighScore(int index) throws IOException;
	
	/* returns the scene for DisplayProgram.java */
	Scene appScene();
	
	/* returns to the welcome (home) screen */
	Button goHome();
	
	/* returns the number in a set */
	byte SET();
}//end interface Usable
