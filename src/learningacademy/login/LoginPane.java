package learningacademy.login;

/* class LoginPane creates a login screen for the user 
 * to sign into their account
 */

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.image.*;
import javafx.scene.text.*;
import java.io.*;
import learningacademy.Creatable;

public class LoginPane extends BorderPane implements Creatable {
	/* pane to hold the login user interface */
	private GridPane loginBox = new GridPane();
	
	/* button to submit login form */
	private Button submit;
	
	/* button to create a new account */
	private Button newAccount;
	
	/* textboxes for user input */
	private TextField usernameTF;
	private TextField passwordTF;
	
	/* label to let user know input is not correct or 
	 * user does not have an account yet 
	 */
	private Label errorLB = new Label();
	
	/* variable for the data file */
	private RandomAccessFile userInformation;

	public LoginPane(RandomAccessFile file) {
		userInformation = file;
		setUpUserInterface();
	}
	
	@Override
	public void setUpUserInterface() {
		/* labels to give directions to the user */
		Label usernameLB = new Label("Username: ");
		usernameLB.setFont(Font.font("Segoe UI", FontWeight.BOLD, 25));
		Label passwordLB = new Label("Password: ");
		passwordLB.setFont(Font.font("Segoe UI", FontWeight.BOLD, 25));
		
		usernameTF = new TextField();
		passwordTF = new TextField();
		
		submit = new Button("Login");
		submit.setStyle("-fx-font-size: 35; -fx-border-width: 3;" +
		"-fx-font-family: Segoe UI; -fx-border-color: red");
		
		newAccount = new Button("Create a new account!");
		newAccount.setStyle("-fx-font-size: 10; -fx-background-color: transparent;" +
		"-fx-font-family: Segoe UI");
		
		/* background image */
		ImageView background = new ImageView(new Image("loginbackground.jpg"));
		getChildren().add(background);
		background.fitWidthProperty().bind(widthProperty());
		background.fitHeightProperty().bind(heightProperty());
	
		loginBox.setHgap(10);
		loginBox.setVgap(20);
		
		loginBox.add(newAccount, 0, 0);
		loginBox.add(usernameLB, 0, 1);
		loginBox.add(usernameTF, 1, 1);
		loginBox.add(passwordLB, 0, 2);
		loginBox.add(passwordTF, 1, 2);
		
		loginBox.setAlignment(Pos.CENTER);
		
		loginBox.setStyle("-fx-border-color: black; -fx-border-width: 5");
		
		errorLB.setVisible(false);
		setCenter(loginBox);
		setBottom(submit);
		setTop(errorLB);
		setAlignment(errorLB, Pos.CENTER);
		errorLB.setText("");
		setAlignment(submit, Pos.BOTTOM_CENTER);
	}//end setUpUserInterface
	
	/* this method will check if the user has an account */
	public boolean hasAnAccount() throws IOException {
		LoginChecking check = new LoginChecking(userInformation, usernameTF.getText(),
				passwordTF.getText());
		
		check.checkForAccount(usernameTF.getText(), passwordTF.getText());
	
		String noAccount = "We couldn't find you.\n"
				+ "Try again or click on \"Create a new account!\".";
		
		if (!check.hasAccount()) {
			displayError(noAccount);
			clearText();
			return false;
		}
		
		errorLB.setText("");
		clearText();
		
		return true;
	}//end hasAnAccount
	
	/* this method will create a new account for the user */
	public boolean createANewAccount() throws IOException {
		LoginChecking check = new LoginChecking(userInformation, usernameTF.getText(),
				passwordTF.getText());
		
		check.createAccount(usernameTF.getText(), passwordTF.getText());
		
		String incorrect = "Incorrect credentials!\n"
				+ "Username must be between 8 and 16 characters and only one word.\n"
				+ "Password must be betwen 6 and 12 characters and "
				+ "must include\nat least 1 special character: "
				+ "(@, $, &, *, ?, !, #, %).";
		String duplicate = "That username is already taken.";
		
		if (check.isDuplicate()) {
			displayError(duplicate);
			clearText();
			return false;
		}
		
		if (!check.isCorrect()) {
			displayError(incorrect);
			clearText();
			return false;
		}
		
		errorLB.setText("");
		clearText();
		
		return true;
	}//end createANewAccount
	
	/* this method displays an error message to the errorLB label */
	private void displayError(String message) {
		errorLB.setText(message);
		errorLB.setFont(Font.font("Segoe UI", FontWeight.BOLD, 25));
		errorLB.setTextFill(Color.RED);
		errorLB.setVisible(true);
	}
	
	private void clearText() {
		usernameTF.setText("");
		passwordTF.setText("");
	}
	
	public Button submit() {
		return submit;
	}
	
	public Button newAccount() {
		return newAccount;
	}
	
	public static void main(String[] args) {
		System.out.println("LoginPane");
	}
}//end class LoginPane
