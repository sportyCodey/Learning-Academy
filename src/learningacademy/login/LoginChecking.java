package learningacademy.login;

/* class LoginChecking creates a file if 
 * not already created.
 * This file has all the users'
 * information included in it.
 * LoginChecking does checking to ensure proper login
 * credentials are met.
 */

import java.io.*;
import java.util.ArrayList;

public class LoginChecking {
	/* this will tell other classes if the user has an account */
	private boolean hasAccount = false;
	
	/* this will tell other classes if the user put in correct info */
	private boolean isCorrect = false;
	
	/* this will tell other classes if the user put in a 
	 * username that's already taken so the software can specifically
	 * warn the user it's a duplicate 
	 */
	private boolean isDuplicate = false;
	
	/* variable for the data file */
	private RandomAccessFile userInformation;
	
	/* This is used to store all the usernames from the file.
	 * This will be used to determine if there is a duplicate username
	 * when the user is trying to create a new account.
	 * There can not be any duplicate usernames.
	 */
	private ArrayList<String> usernames;
	
	/* the number of apps in this software */
	private static final byte NUM_OF_APPS = 3;
	
	/* This will show where the current user is at in the file.
	 * pointer[0] is for the Vocabulary Builder app.
	 * pointer[1] is for the Paragraph Finisher app.
	 * pointer[2] is for the Math Bowl app. */
	private static long[] pointer = new long[NUM_OF_APPS];
	
	public LoginChecking(RandomAccessFile file, String username,
			String password) throws IOException {
		userInformation = file;
		usernames = new ArrayList<>();
		
		/* This is used to delete everything in the file.
		 * It was used at the beginning of development for test cases and to 
		 * ensure this class worked properly. It's commented out because
		 * the user information needs to be saved when they exit the software. 
		 */
		//userInformation.setLength(0);
	}
	
	public LoginChecking(RandomAccessFile file) throws IOException {
		userInformation = file;
	}
	
	/* checks if the user has an account */
	public void checkForAccount(String username, String password) throws IOException {
		if (userInformation.length() > 0) {
			userInformation.seek(0);
			try {
				while (true) {
					String inFileUsername = userInformation.readUTF();
					if (inFileUsername.equals(username)) {
						String inFilePassword = userInformation.readUTF();
						if (inFilePassword.equals(password)) {
							hasAccount = true;
							
							/* file pointer for the Vocabulary
							 * Builder app
							 */
							pointer[0] = userInformation.getFilePointer();
							
							findPointersForOtherApps();
							break;
						}
						discardLinesAfterPassword();
					}
					else {
						discardLinesAfterUsername();
					}
				}//end while loop
			}//end try block
			catch (EOFException ex) {
				System.out.println("Done reading from file");
			}
		}//end if (userInformation.length() > 0)
	}//end performLoginChecking
	
	public boolean hasAccount() {
		return hasAccount;
	}
	
	public boolean isCorrect() {
		return isCorrect;
	}
	
	public boolean isDuplicate() {
		return isDuplicate;
	}
	
	public long getPointer(int index) {
		return pointer[index];
	}
	
	/* creates an account if credentials are valid */
	public void createAccount(String username, String password) throws IOException {
		if (isAcceptableCredentials(username, password)) {
			userInformation.seek(userInformation.length());
			
			/* login information */
			userInformation.writeUTF(username);
			userInformation.writeUTF(password);
			
			/* the file pointer for
			 * the Vocabulary Builder app
			 */
			pointer[0] = userInformation.getFilePointer();
			
			/* Vocabulary Builder app information */
			userInformation.writeUTF("00:00:00");
			userInformation.writeInt(0);
			
			/* the file pointer for the
			 * Paragraph Finisher app
			 */
			pointer[1] = userInformation.getFilePointer();
					
			/* Paragraph Finisher app information */
			userInformation.writeUTF("00:00:00");
			userInformation.writeInt(0);
			
			/* the file pointer for the Math Bowl app */
			pointer[2] = userInformation.getFilePointer();
			
			/* Math Bowl app information */
			userInformation.writeUTF("00:00:00");
			userInformation.writeInt(0);
			
			isCorrect = true;
		}
		else
			isCorrect = false;
	}//end createAccount
	
	/* checks the validity of the credentials entered by the user */
	private boolean isAcceptableCredentials(String username,
			String password) throws IOException {
		getUsernames();
		if (usernames.contains(username)) {
			isDuplicate = true;
			return false;
		}
		if (username.length() < 8 || username.length() > 16)
			return false;
		if (username.contains(" "))
			return false;
		if (password.length() < 6 || password.length() > 12)
			return false;
		
		/* The password must have at least 1 special character in it.
		 * The special characters are defined in method hasSpecialCharacters.
		 */
		int specialCharCount = 0;
		for (int i = 0; i < password.length(); ++i) {
			if (hasSpecialCharacter(password.charAt(i))) {
				++specialCharCount;
			}
		}
		if (specialCharCount == 0)
			return false;
		return true;
	}//end isAcceptableCredentials
	
	/* determines if the password has a special character in it */
	private boolean hasSpecialCharacter(char i) {
		char[] specialCharacters = {'@', '$', '&', '*', '?', '!', 
				'#', '%'};
		for (int j = 0; j < specialCharacters.length; ++j) {
			if (i == specialCharacters[j])
				return true;
		}
		return false;
	}//end noSpecialCharacters
	
	/* This method is for retrieving the usernames from the
	 * file so any duplicates can be detected when the user creates
	 * a new account. 
	 */
	private void getUsernames() throws IOException {
		userInformation.seek(0);
		try {
			while (true) {
				String inFileUsername = userInformation.readUTF();
				usernames.add(inFileUsername);
				
				discardLinesAfterUsername();
			}
		}
		catch (EOFException ex) {
			System.out.println("Done reading from file");
		}
	}
	
	/* this discards unwanted lines when checking for an account, but
	 * it's specifically for the lines that would happen if the password
	 * is read because the username given by the user equals a username in 
	 * the file
	 */
	private void discardLinesAfterPassword() throws IOException {
		/* this would be the time for the Vocabulary
		 * Builder app, so we want to discard it */
		userInformation.readUTF();
		
		/* this would be the high score value for the Vocabulary
		 * Builder app, so we want to discard it
		 */
		userInformation.readInt();
		
		/* read info from the Paragraph Finisher app */
		userInformation.readUTF();
		userInformation.readInt();
		
		/* read infor from the Math Bowl app */
		userInformation.readUTF();
		userInformation.readInt();
	}
	
	/* this does the same thing as above, but this discards everything
	 * if the username given by the user does not equal a username from the file
	 */
	private void discardLinesAfterUsername() throws IOException {
		/* this would be the password on the second line, so we want
		 * to discard it 
		 */
		userInformation.readUTF();
		
		discardLinesAfterPassword();
	}
	
	/* this stores the rest of the file pointer array
	 * at the beginning of the other apps
	 */
	private void findPointersForOtherApps() throws IOException {
		/* we discard information until we get to the right spot
		 * in the file
		 */
		userInformation.readUTF();
		userInformation.readInt();
		
		/* file pointer for the
		 * Paragraph Finisher app
		 */
		pointer[1] = userInformation.getFilePointer();
		
		userInformation.readUTF();
		userInformation.readInt();
		
		/* file pointer for the Math Bowl app */
		pointer[2] = userInformation.getFilePointer();
	}//end findPointersForOtherApps

	public static void main(String[] args) {
		System.out.println("LoginChecking");
	}
}//end class LoginChecking