package learningacademy;

/* class DisplayProgram will display all the scenes 
 * and will be the starting point of the software
 */

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.*;
import learningacademy.login.LoginPane;
import learningacademy.welcome.WelcomePane;
import learningacademy.userapps.*;

public class DisplayProgram extends Application {
	/* the file that will be passed from class to class and finally closed
	 * when the user exits the program
	 */
	private static RandomAccessFile file;
	
	/* the width and height of each scene */
	private static final double WIDTH = 1280.0;
	private static final double HEIGHT = 653.3333129882812;
	
	@Override
	public void start(Stage primaryStage) throws IOException {
		 file = new RandomAccessFile("data.dat", "rw");
		 
		/* the login screen and its corresponding Scene */
		LoginPane loginScreen = new LoginPane(file);
		Scene loginScene = new Scene(loginScreen, WIDTH, HEIGHT);
		
		/* the welcome (home) screen and its corresponding Scene */
		WelcomePane welcomeScreen = new WelcomePane();
		Scene welcomeScene = new Scene(welcomeScreen, WIDTH, HEIGHT);
		
		/* the Vocabulary Builder screen and its corresponding Scene */
		DisplayVocabBuilder vocabBuilderScreen = new DisplayVocabBuilder(file);
		Scene vocabBuilderScene = vocabBuilderScreen.appScene();
		
		/* the Paragraph Finisher screen and its corresponding Scene */
		DisplayParagraphFinisher parFinishScreen = new DisplayParagraphFinisher(file);
		Scene parFinishScene = parFinishScreen.appScene();
		
		/* the Math Bowl screen and its corresponding Scene */
		DisplayMathBowl mathBowlScreen = new DisplayMathBowl(file);
		Scene mathBowlScene = mathBowlScreen.appScene();
		
		/* goes to the welcome (home) screen once login is acceptable */
		loginScreen.submit().setOnAction(e -> {
			try {
				if (loginScreen.hasAnAccount()) {	
					display("Welcome", welcomeScene, primaryStage);
				}
			}
			catch (IOException ex) { System.out.println(ex); }
		});
		
		/* goes to the welcome (home) screen if user creates an
		 * acceptable new account 
		 */
		loginScreen.newAccount().setOnAction(e -> {
			try {
				if (loginScreen.createANewAccount()) 
					display("Welcome", welcomeScene, primaryStage);
			}
			catch (IOException ex) { System.out.println(ex); }
				
		});
		
		/* goes to the Vocabulary Builder screen from the welcome (home) screen */
		welcomeScreen.vocabBuilder().setOnAction(e -> {
			display("Vocabulary Builder!", vocabBuilderScene, primaryStage);
			try {
				vocabBuilderScreen.getHighScore(0);
			}
			catch (IOException ex) { System.out.println(ex); }
		});
		
		/* goes to the Paragraph Finisher screen from the welcome (home) screen */
		welcomeScreen.parFinisher().setOnAction(e -> {
			display("Paragraph Finisher!", parFinishScene, primaryStage);
			try {
				parFinishScreen.getHighScore(1);
			}
			catch (IOException ex) { System.out.println(ex); }
		});
		
		/* goes to the Math Bowl screen from the welcome (home) screen */
		welcomeScreen.mathBowl().setOnAction(e -> {
			display("Math Bowl!", mathBowlScene, primaryStage);
			try {
				mathBowlScreen.getHighScore(2);
			}
			catch (IOException ex) { System.out.println(ex); }
		});
		
		/* goes back to the login screen */
		welcomeScreen.logOut().setOnAction(e -> {
			display("Login", loginScene, primaryStage);
		});
		
		/* goes to the welcome (home) screen from the Vocabulary Builder screen */
		vocabBuilderScreen.goHome().setOnAction(e -> {
			display("Welcome", welcomeScene, primaryStage);
			vocabBuilderScreen.resetApp();
		});
		
		/* goes to the welcome (home) screen from the Paragraph Finisher screen */
		parFinishScreen.goHome().setOnAction(e -> {
			display("Welcome", welcomeScene, primaryStage);
			parFinishScreen.resetApp();
		});
		
		/* goes to the welcome (home) screen from the Math Bowl screen */
		mathBowlScreen.goHome().setOnAction(e -> {
			display("Welcome", welcomeScene, primaryStage);
			mathBowlScreen.resetApp();
		});
		
		/* closes the file when the closes the window */
		primaryStage.setOnCloseRequest(e -> {
			try {
				file.close();
			}
			catch(IOException ex) { System.out.println(ex); }
			System.out.println("File closed successfully");
		});
		
		/* the login screen will always be the first scene that is displayed */
		display("Login", loginScene, primaryStage);
	}//end start
	
	/* displays the current scene */
	public void display(String title, Scene scene, Stage primaryStage) {
		primaryStage.setTitle(title);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}//end class DisplayProgram
