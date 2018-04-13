package learningacademy;

/* class Timer creates a timer for the apps */

import javafx.scene.layout.StackPane;
import javafx.scene.text.*;

public class Timer extends StackPane {
	/* the time of the clock that will be displayed */
	private Text time;

	public Timer() {
		setUpUserInterface();
	}
	
	public void setUpUserInterface() {
		time = new Text("00:00:00");
		time.setFont(Font.font("Segoe UI", FontWeight.BOLD, 50));
		
		getChildren().add(time);
		
		setStyle("-fx-border-color: red; -fx-border-width: 5");
	}
	
	public Text time() {
		return time;
	}
	
	public static void main(String[] args) {
		System.out.println("Timer");
	}
}//end class Timer
	
