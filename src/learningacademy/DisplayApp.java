package learningacademy;

/* abstract class DisplayApp is the parent
 * class that has methods that "Display..."
 * classes will inherit from and use.
 * I decided to call these classes
 * "Display..." because they will be 
 * the classes that display the 
 * GUI to the user in DisplayProgram.java.
 * This class has some important 
 * methods that will be used in all
 * "Display..." classes. 
 */

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.util.Duration;
import javafx.animation.*;
import javax.swing.JOptionPane;
import java.io.*;
import learningacademy.login.LoginChecking;
import learningacademy.userapps.*;

public abstract class DisplayApp implements Usable {
	/* the Vocabulary Builder screen */
	protected VocabBuilderPane vocabBuilderScreen;
	
	/* the Paragraph Finisher screen */
	protected ParagraphFinisherPane parFinishScreen;
	
	/* the Math Bowl screen */
	protected MathBowlPane mathBowlScreen;
	
	/* how many answer/solutions to a set */
	protected final byte SET = 5;
	
	/* calls the goUp method every second
	 * to increment the timer 
	 */
	protected Timeline countUp;
	
	/* calls the goDown method every second
	 * to decrement the timer
	 */
	protected Timeline countDown;
	
	/* If the user plays Time Attack, then
	 * there will be some initial set up with the timer.
	 * This variable will be set to true.
	 * When this is true I don't want the user
	 * using the app until they are finished
	 * with the set up process.
	 */
	protected boolean timeAttackSetUp = false;
	
	/* will be set to true if the user plays the Time Attack game */
	protected boolean timeAttackPlay = false;
	
	/* counters that increment the timer */
	protected byte second = 0;
	protected byte minute = 0;
	protected byte hour = 0;
	
	/* keeps count of the sets completed in the Time Attack game */
	protected int timeAttackCount = 0;
	
	/* the current high score from Time Attack */
	protected int currentHighScore = 0;
	
	/* this is what will be incremented for the user to see every time the
	 * user completes a set 
	 */
	protected int completions = 0;
	
	/* this keeps count of the correct answers per set */
	protected int correct = 0;
	
	/* the current time the user chose to get their high score */
	protected String time;
	
	/* This might be the time the user beats their high score. 
	 * Variable time will be set to this variable if the 
	 * user beats their high score 
	 */
	protected String maybeTime;

	/* the file with the all the user's information in it */
	private RandomAccessFile userInformation;
	
	/* this is used to get the file pointer */
	private LoginChecking needPointer;
	
	/* the scene used in DisplayProgram.java */
	private Scene scene;
	
	/* the pointer of the file */
	private long pointer = 0;
	
	/* the current app being used */
	private AppPane appScreen;
	
	/* the width and height of each scene */
	private static final double WIDTH = 1280.0;
	private static final double HEIGHT = 653.3333129882812;
	
	public DisplayApp(RandomAccessFile file, String app) throws IOException {
		appScreen = getApp(app);
		scene = new Scene(appScreen, WIDTH, HEIGHT);
		countUp = new Timeline(new KeyFrame(Duration.millis(1000),
				event -> goUp()));
		countDown = new Timeline(new KeyFrame(Duration.millis(1000),
				event ->  { try { goDown(); } catch(IOException ex) { } }));
		userInformation = file;
		needPointer = new LoginChecking(userInformation);
	}
	
	@Override
	public void useGUI() {
		timeAttackPlay();
		
		/* when the user clicks these buttons */
		appScreen.setTimer().setOnAction(e -> {
			if (countUp.getStatus() != Animation.Status.RUNNING) {
				countUp.setCycleCount(Timeline.INDEFINITE);
				countUp.play();
			}
		});
		
		appScreen.reset().setOnAction(e -> {
			resetApp();
		});
		
		appScreen.freeStyle().setOnAction(e -> {
			JOptionPane.showMessageDialog(null, "In freestyle mode " +
					"you can figure out the solution with no time constraints." +
					"\nStart the timer if you want to" +
					" keep track of time." +
					"\nOr you can just relax and figure it out slowly.\nHave fun!");
		});
		
		/* enlarges the button on mouse entered */
		appScreen.setTimer().setOnMouseEntered(e -> {
			buttonGrowShrink(appScreen.setTimer(), 58, 131);
		});
		
		appScreen.setTimer().setOnMouseExited(e -> {
			buttonGrowShrink(appScreen.setTimer(), 50, 123);
		});
		
		appScreen.reset().setOnMouseEntered(e -> {
			buttonGrowShrink(appScreen.reset(), 58, 91);
		});
		
		appScreen.reset().setOnMouseExited(e -> {
			buttonGrowShrink(appScreen.reset(), 50, 83);
		});
		
		appScreen.shuffle().setOnMouseEntered(e -> {
			buttonGrowShrink(appScreen.shuffle(), 58, 109);
		});
		
		appScreen.freeStyle().setOnMouseEntered(e -> {
			buttonGrowShrink(appScreen.freeStyle(), 58, 126);
		});
		
		appScreen.timeAttack().setOnMouseEntered(e -> {
			buttonGrowShrink(appScreen.timeAttack(), 58, 152);
		});
		
		/* shrinks the button back to normal size on mouse exit */
		appScreen.shuffle().setOnMouseExited(e -> {
			buttonGrowShrink(appScreen.shuffle(), 50, 101);
		});
		
		appScreen.homeScreen().setOnMouseEntered(e -> {
			buttonGrowShrink(appScreen.homeScreen(), 58, 166);
		});
		
		appScreen.homeScreen().setOnMouseExited(e -> {
			buttonGrowShrink(appScreen.homeScreen(), 50, 158);
		});
		
		appScreen.freeStyle().setOnMouseExited(e -> {
			buttonGrowShrink(appScreen.freeStyle(), 50, 118);
		});
		
		appScreen.timeAttack().setOnMouseExited(e -> {
			buttonGrowShrink(appScreen.timeAttack(), 50, 144);
		});
	}//end useGUI
	
	@Override
	public void timeAttackPlay() {
		/* when the user clicks on the Time Attack button */
		appScreen.timeAttack().setOnAction(e -> {
			timeAttackSetUp = true;
			timeAttackPlay = true;
			resetTimer();
			appScreen.disableButtons();
			appScreen.repaintActionPane();
			correct = 0;
			
			JOptionPane.showMessageDialog(null, "Set the timer above!\nPress "
					+ "the UP arrow key " +
					"to increase the time\nand the DOWN arrow key to decrease " +
					"the time.\nPress enter when you're ready to go!");
			
			appScreen.setOnKeyPressed(k -> {
				appScreen.requestFocus();
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
								appScreen.setUserStats(completions,
										0, time, currentHighScore, timeAttackPlay);
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
		resetTimer();
		completions = 0;
		correct = 0;
		timeAttackCount = 0;
		appScreen.setUserStats(completions, 0, time, 
		currentHighScore, false);
		appScreen.enableButtons();
		timeAttackSetUp = false;	
		timeAttackPlay = false;
		appScreen.repaintActionPane();
	}
	
	@Override
	public void goUp() {
		++second;
		if (second >= 60) {
			++minute;
			second = 0;
		}
		if (minute >= 60) {
			++hour;
			minute = 0;
		}

		String output = String.format("%02d:%02d:%02d", hour, minute, second);
		setTimerTime(output);
	}
	
	@Override
	public void goDown() throws IOException {
		if (second == 0 && minute == 0 && hour == 0) { 
			updateHighScore();
			int temp = completions;
			resetApp();
			completions = temp;
			appScreen.setUserStats(completions, 0, time, 
					currentHighScore, false);
		}
		else {
			if (second == 0) {
				--minute;
				second = 59;
			}
			else
				--second;
		}
		String output = String.format("%02d:%02d:%02d", hour, minute, second);
		setTimerTime(output);
	}
	
	@Override
	public void buttonGrowShrink(Button b, double h, double w) {
		b.setPrefHeight(h);
		b.setPrefWidth(w);
	}
	
	@Override
	public void resetTimer() {
		countUp.stop();
		countDown.stop();
		second = 0;
		minute = 0;
		hour = 0;
		setTimerTime("00:00:00");
	}
	
	@Override
	public void updateHighScore() throws IOException {
		if (timeAttackCount > currentHighScore) {
			currentHighScore = timeAttackCount;
			
			time = maybeTime;
	
			/* this sets the pointer back to the correct position on the file
			 * so the new high score can be correctly recorded 
			 */
			long differencePointer = userInformation.getFilePointer() - pointer;
			pointer = userInformation.getFilePointer() - differencePointer;
			writeToFile();
		}
	}
	
	@Override
	public void writeToFile() throws IOException {
		userInformation.seek(pointer);
		userInformation.writeUTF(time);
		userInformation.writeInt(currentHighScore);
		
		appScreen.setUserStats(completions, 0, time, 
				currentHighScore, false);
	}
	
	@Override
	public void getHighScore(int index) throws IOException {
		pointer = needPointer.getPointer(index);
		userInformation.seek(pointer);
		time = userInformation.readUTF();
		currentHighScore = userInformation.readInt();
		appScreen.setUserStats(completions, 0,
				time, currentHighScore, false);
	}
	
	
	@Override
	public Scene appScene() {
		return scene;
	}
	
	@Override
	public Button goHome() {
		return appScreen.homeScreen();
	}
	
	@Override
	public byte SET() {
		return SET;
	}

	/* this determines which app is being used by the user */
	private AppPane getApp(String app) {
		if (app.equals("vocab")) {
			vocabBuilderScreen = new VocabBuilderPane();
			return vocabBuilderScreen;
		}
		if (app.equals("paragraph")) {
			parFinishScreen = new ParagraphFinisherPane();
			return parFinishScreen;
		}
		if (app.equals("math")) {
			mathBowlScreen = new MathBowlPane();
			return mathBowlScreen;
		}
		
		return null;
	}
	
	/* sets the timer to the specified time */
	private void setTimerTime(String output) {
		appScreen.timer().time().setText(output);
	}
	
	public static void main(String[] args) {
		System.out.println("DisplayApp");
	}
}//end abstract class DisplayApp
