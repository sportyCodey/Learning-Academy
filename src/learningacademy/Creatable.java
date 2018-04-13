package learningacademy;

import java.util.ArrayList;

/* interface Creatable will be used for 
 * classes that are creatable.
 * Mainly, the "Pane" classes (E.g. WelcomePane)
 *  will implement this interface or because 
 * they are creatable. Some classes like
 * VocabBuilderPane will implicityly implement 
 * this interface.
 */

import javafx.scene.control.Button;

public interface Creatable {
	/* This sets up the user interface. 
	 * Every class that implements Creatable will
	 * override this method.
	 */
	void setUpUserInterface();
	
	default void setUserStats(int setsCompleted, int timeAttackSetsCompleted,
			String time, int topScore, boolean timeAttack) {
		System.out.println("setUserStats");
	}
	
	/* repaints the action Pane (the Pane where the 
	 * user does most of their interaction)
	 * if that's an option */ 
	default void repaintActionPane() {
		System.out.println("repaintActionPane");
	}
	
	/* sets the button style if that's an option */
	default void setButtonStyle(Button b) {
		System.out.println("setButtonStyle");
	}
	
	/* disables some buttons so the user can't push them */
	default void disableButtons() {
		System.out.println("disableButtons");
	}
	
	/* the user can now push the buttons again */
	default void enableButtons() {
		System.out.println("enableButtons");
	}
	
	/* returns to the welcome (home) screen if that's an option */
	default Button homeScreen() {
		return null;
	}
	
	/* returns the number of solutions
	 * displayed at a time
	 */
	default byte SET() {
		return 0;
	}
	
	/* the following accessors return 
	 * buttons that correspond to the 
	 * 3 apps
	 */
	default Button setTimer() {
		return null;
	}
	
	default Button reset() {
		return null;
	}
	
	default Button shuffle() {
		return null;
	}
	
	default Timer timer() {
		return null;
	}
	
	default Button freeStyle() {
		return null;
	}
	
	default Button timeAttack() {
		return null;
	}
	
	/* returns the answer and solution pair */
	default ArrayList<AnswerSolution> getPairs() {
		return null;
	}
}//end interface Creatable
