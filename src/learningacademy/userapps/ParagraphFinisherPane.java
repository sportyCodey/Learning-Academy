package learningacademy.userapps;

/* class ParagraphFinisherPane extends AppPane, adds
 * some more functionality, and adds objects to the action Pane
 * for the Paragraph Finisher app.
 * In the center the paragraphs will be displayed.
 * On the sides there will be buttons to move from
 * paragraph to paragraph. The user can choose 1 out
 * of 2 buttons displayed to finish the paragraph 
 * correctly.
 */

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import java.util.*;

import learningacademy.AnswerSolution;
import learningacademy.AppPane;

public class ParagraphFinisherPane extends AppPane {
	/* array to hold all the TextAreas which will display the paragraphs */
	private TextArea[] paragraph;
	
	/* Array to hold the ScrollPane that will hold the TextAreas.
	 * Since this is a ScrollPane, the user can see scroll up and down
	 * to see the full paragraph if necessary.
	 */
	private ScrollPane[] paragraphHolder;
	
	/* index for switching paragraphs */
	private int index = 0;

	/* buttons to change from paragraph to paragraph */
	private Button goLeft = new Button("<=");
	private Button goRight = new Button("=>");
	
	/* buttons to select an answer */
	private Button firstChoice = new Button();
	private Button secondChoice = new Button();
	
	/* the label that will show the user how many
	 *  paragraphs are left in the set */
	private Label paragraphsLeft = new Label();
	
	/* the choices the user can choose from answer the problem */
	private String[] choicesToUse;
	
	public ParagraphFinisherPane() {
		super("Paragraph Finisher!");
		pair = new ArrayList<>();
		paragraph = new TextArea[SET];
		paragraphHolder = new ScrollPane[SET];
		setButtonStyle(goLeft);
		setButtonStyle(goRight);
		setUpUserInterface();
		setUpParagraphsAndAnswers();
		repaintActionPane();
		setParagraphsLeft(SET);
	}
	
	@Override
	public void repaintActionPane() {
		action.getChildren().clear();
		
		for (int i = 0; i < SET; ++i) {
			paragraph[i] = new TextArea();
			paragraph[i].setEditable(false);
			paragraph[i].setText(pair.get(i).getSolution());
			paragraph[i].setFont(Font.font("Segoe UI", 45));
			paragraph[i].setWrapText(true);
			
			paragraphHolder[i] = new ScrollPane(paragraph[i]);
			paragraphHolder[i].setPrefHeight(500);
			paragraphHolder[i].setPrefWidth(500);
			paragraphHolder[i].setFitToHeight(true);;
			paragraphHolder[i].setFitToWidth(true);
			paragraph[i].setStyle("-fx-border-color: black;" +
					"-fx-border-width: 3");
		}
		
		/* the choices the user can pick from to solve the problem */
		VBox choices = new VBox(60);
		
		firstChoice.setStyle("-fx-border-width: 3;" + 
		"-fx-font-family: Segoe UI; -fx-border-color: black;" +
		"-fx-background-color: pink; -fx-font-size: 30");
		firstChoice.setPrefWidth(200);
		firstChoice.setWrapText(true);
		
		secondChoice.setStyle("-fx-border-width: 3;" + 
		"-fx-font-family: Segoe UI; -fx-border-color: black;" +
		"-fx-background-color: pink; -fx-font-size: 30");
		secondChoice.setPrefWidth(200);
		secondChoice.setWrapText(true);
		
		/* the index of the answer in the original array */
		int findAnswer = findAnswer();
		
		/* the two choices should be randomly assigned to the buttons */
		byte random = (byte)(Math.random() * 2);
		if (random == 0) {
			firstChoice.setText(choicesToUse[findAnswer]);
			secondChoice.setText(choicesToUse[findAnswer + 1]);
		}
		else {
			secondChoice.setText(choicesToUse[findAnswer]);
			firstChoice.setText(choicesToUse[findAnswer + 1]);
		}
		
		paragraphsLeft.setFont(Font.font("Segoe UI", FontWeight.BOLD, 35));
		paragraphsLeft.setTextFill(Color.DARKBLUE);
		
		choices.getChildren().addAll(firstChoice, secondChoice, paragraphsLeft);
		
		action.getChildren().add(choices);
		
		HBox paragraphPane = new HBox();
		paragraphPane.getChildren().addAll(goLeft, paragraphHolder[index], goRight);
		paragraphPane.setTranslateX(225);
		
		action.getChildren().add(paragraphPane);
		
		setCenter(action);
	}//end repaintActionPane
	
	public void setParagraphsLeft(byte number) {
		paragraphsLeft.setText("Number of\nparagraphs in\nthe set: " + number);
	}
	
	/* this switches paragraphs to the one on the left in the array 
	 * when the goLeft button is pushed 
	 */
	public void changeParagraphLeft() {
		--index;
		
		if (index < 0)
			index = SET - 1;
		
		repaintActionPane();
	}
	
	/* same explanation as above except it goes to the right */
	public void changeParagraphRight() {
		++index;
		
		if (index > SET - 1)
			index = 0;
		
		repaintActionPane();
	}
	
	/* disables the choices the user can pick */
	public void disableChoices() {
		firstChoice.setDisable(true);
		secondChoice.setDisable(true);
	}
	
	/* enables the choices the user can pick from */
	public void enableChoices() {
		firstChoice.setDisable(false);
		secondChoice.setDisable(false);
	}
	
	/* shuffles the contents in the action Pane around */
	public void mixUp() {
		index = 0;
		enableChoices();
		shufflePair();
		repaintActionPane();
	}
	
	public void shufflePair() {
		Collections.shuffle(pair);
	}
	
	public Button goLeft() {
		return goLeft;
	}
	
	public Button goRight() {
		return goRight;
	}
	
	public Button firstChoice() {
		return firstChoice;
	}
	
	public Button secondChoice() {
		return secondChoice;
	}
	
	public int getIndex() {
		return index;
	}
	
	/* Adds the paragraphs the user will complete.
	 * Adds the answers the user can choose from. */
	private void setUpParagraphsAndAnswers() {
		String[] paragraphsToAdd = {"Billy was playing basketball all day long. " +
		"Afterwards, he was so thirsty he drank a whole gallon of water! "
		+ "When someone drinks that much water and get that much " +
		"exercise, one could say they. . .",
		"When my dog ran away from home, my father told me good ole Spot"
		+ " just went on a vacation. However, Spot has been gone for quite some "
		+ "time now. I'm starting to get worried. I think my dad might be. . .",
		"I absolutely love chocolate! Don't you just love eating chocolate " +
		" after a long day of school? My mom told me if I keep eating chocolate " +
		"the way I do, then I might. . .", "I'm learning how to code so "
		+ "one day I can work for a software company. As I practice each day "
		+ "I will be become. . .", "One day my cousin told me to stop punching " +
		"the sand bag or I'll bruise my hand. I never listened to him, so after " +
		"5 years of punching the sand bag, I finally. . .",
		"When my parents tell me to take out the trash, I'm always " +
		"disgusted! It smells so badly. However, this time I'm wearing " +
		"__________ to help with the stench.", "Mary Ann was the pretty " +
		"girl who sat next me in class. One day I finally __________ " +
		"and asked her to the school dance.", "Playing video games is fun, but"
		+ " there's nothing like going outside to enjoy. . .",
		"\"You wimp!\", said the bully at school. No one really likes " +
		"him because of his bad __________.", "Hey, do you like to swim? " +
		"Well you're in luck! Come to McGuire's Swim Club for a" +
		" __________ price of $25 a session!", "Mickey Mouse is not " +
		"real. However, many children love to pretend he's real when" +
		" they're with Mickey. . .", "I love sipping on a nice __________" +
		" can of Coke during a hot day of summer.", "My father is a lawyer and " +
		"my mother is a teacher. I want to follow my mother's footsteps and " +
		"become a. . .", "Please, always remember to wash your hands after you're " +
		"done. . .", "My brother is the coolest! He made __________ " +
		"for me. Who else can say that?", "Basketball is fun to watch!" +
		" I like playing because I dominate all the__________!",
		"Have you ever swam in the ocean? It's scary at first, but" +
		" you'll soon fall in love with. . ."};
		
		/* These are the answers the user can choose from
		 * The first index is ignored because
		 * the correct answer will
		 * always be odd. */
		String[] temp = {"", "are very healthy.", "are very unhealthy.",
				"telling a little fib.", "telling the truth.",
				"turn into one!", "become a master chef!",
				"more efficient.", "less efficient.",
				"broke my hand.", "broke my leg.",
				"a surgeon mask", "a gas mask",
				"took a deep breath", "freaked out",
				"some sun.", "some studies.", "attitude", "habits",
				"great", "awful", "visiting DisneyLand.", "watching TV.",
				"cool", "hot", "teacher one day.", "lawyer one day.",
				"playing outside.", "sleeping in your bed.",
				"software", "computers", "competition", "fans",
				"the ocean.", "ships at sea."};
		
		
		choicesToUse = temp;
		
		/* index to make sure all the paragraphsToAdd[] is added to pair */
		int j = 1;
		for (int i = 1; i < temp.length; i += 2) {
			/* the correct answer/solution pair is here */
			pair.add(new AnswerSolution(temp[i], paragraphsToAdd[i - j]));
			++j;
		}
		
		shufflePair();
	}//end setUpPair
	
	/* finds the index of the answer so the incorrect answer
	 * can also be displayed 
	 */
	private int findAnswer() {
		for (int j = 1; j < choicesToUse.length; ++j) {
			if (pair.get(index).getAnswer().equals(choicesToUse[j])) {
				return j;
			}
		}
		return -1;
	}
	
	public static void main(String[] args) {
		System.out.println("ParagraphFinisherPane");
	}
}//end class ParagraphFinisherPane
