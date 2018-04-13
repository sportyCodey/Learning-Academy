package learningacademy.welcome;

/* class WelcomePane creates a welcome screen (home screen) 
 *  for the user after they sign into their account
 */

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.*;
import javafx.scene.paint.Color;
import learningacademy.Creatable;

public class WelcomePane extends BorderPane implements Creatable {
	/* goes to Vocabulary Builder app */
	private Button vocabBuilder;
	
	/* goes to Paragraph Finisher app */
	private Button paragraphFinisher;
	
	/* goes to math bowl app */
	private Button mathBowl;
	
	/* lets the user logout of their account */
	private Button logout;
	
	public WelcomePane() {
		setUpUserInterface();
		animationGo();
	}
	
	@Override
	public void setUpUserInterface() {
		vocabBuilder = new Button("Vocabular Builder!");
		setButtonStyle(vocabBuilder);
		
		paragraphFinisher = new Button("Paragraph Finisher!");
		setButtonStyle(paragraphFinisher);
		
		mathBowl = new Button("Math Bowl!");
		setButtonStyle(mathBowl);
		
		logout = new Button("Log out");
		logout.setStyle("-fx-font-size: 20; -fx-border-width: 2;" +
				"-fx-font-family: Segoe UI; -fx-border-color: blue;" +
				"-fx-background-color: silver");
		
		/* title */
		Text title = new Text("Welcome to Drew's\nLearning Academy!");
		title.setFont(Font.font("Algerian", FontWeight.BOLD, 50));
		title.setFill(Color.PERU);
		title.setUnderline(true);
		
		/* used to put the title and logout button */
		BorderPane topLayer = new BorderPane();
		topLayer.setCenter(title);
		topLayer.setLeft(logout);
		
		BorderPane.setAlignment(title, Pos.CENTER);
		
		/* holds the three buttons that go to the apps */
		HBox holder = new HBox(15, vocabBuilder, paragraphFinisher, mathBowl);
		holder.setAlignment(Pos.CENTER);
		
		/* holds the username that will be displayed */
		Label username = new Label("Choose an app from below!");
		username.setFont(Font.font("Segoe UI", FontWeight.BOLD, 40));
		username.setTextFill(Color.PURPLE);
		
		/* pane that goes in the center of the BorderPane */
		VBox center = new VBox(20, username, holder);
		center.setAlignment(Pos.CENTER);
		
		setTop(topLayer);
		setCenter(center);
		
		setStyle("-fx-background-color: lightgreen");
	}//end setUpUserInterface
	
	public Button vocabBuilder(){
		return vocabBuilder;
	}
	
	public Button parFinisher() {
		return paragraphFinisher;
	}
	
	public Button mathBowl() {
		return mathBowl;
	}
	
	public Button logOut() {
		return logout;
	}
	
	/* moves the button on mouse touch and exit */
	public void animationGo() {
		vocabBuilder.setOnMouseEntered(e -> {
			vocabBuilder.setPrefHeight(110);
			vocabBuilder.setPrefWidth(438);
		});
		
		vocabBuilder.setOnMouseExited(e -> {
			vocabBuilder.setPrefHeight(102);
			vocabBuilder.setPrefWidth(430);
		});
		
		paragraphFinisher.setOnMouseEntered(e -> {
			paragraphFinisher.setPrefHeight(110);
			paragraphFinisher.setPrefWidth(456);
		});
		
		paragraphFinisher.setOnMouseExited(e -> {
			paragraphFinisher.setPrefHeight(102);
			paragraphFinisher.setPrefWidth(448);
		});
		
		mathBowl.setOnMouseEntered(e -> {
			mathBowl.setPrefHeight(110);
			mathBowl.setPrefWidth(299);
		});
		
		mathBowl.setOnMouseExited(e -> {
			mathBowl.setPrefHeight(102);
			mathBowl.setPrefWidth(291);
		});
	}//end animationGo
	
	@Override
	public void setButtonStyle(Button b) {
		b.setStyle("-fx-font-size: 45; -fx-border-width: 3;" +
		"-fx-font-family: Segoe UI; -fx-border-color: blue;" +
		"-fx-background-color: silver");
	}
	
	public static void main(String[] args) {
		System.out.println("WelcomePane");
	}
}//end class LoginPane
