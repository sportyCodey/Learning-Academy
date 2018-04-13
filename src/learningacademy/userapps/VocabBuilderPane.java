package learningacademy.userapps;

/* class VocabBuilderPane extends AppPane, adds some 
 * more functionality, and adds objects to the action Pane
 * for the vocabulary builder app.
 * There will be rectangles that have words on them
 * and definitions to the right of the rectangles.
 * The user can click and drag the rectangles to move
 * them to the appropriate definition of the word.
 * Error checking, messages, and display options will be
 * available. Definitions are taken from http://www.dictionary.com.
 * Some definitions are simplified by me.
 */

import javafx.scene.text.*;
import javafx.scene.paint.Color;
import learningacademy.AnswerSolution;
import learningacademy.AppPane;
import java.util.*;

public class VocabBuilderPane extends AppPane {
	/* amount of words that the user can challenge themselves with */
	private static final byte NUM_OF_WORDS = 30;
	
	/* array to hold the Rectangles with text
	 * that are able to be dragged by the user */
	private RectangleWithText[] movableWords;
	
	/* array to hold all the words that are connected to the rectangle */
	private Text[] words;
	
	/* array to hold all the definitions that will be displayed */
	private Text[] definitions;
	
	/* These are the default RectanglesWithText.
	 * I need to declare default objects here because each one
	 * will be used for an ActionEvent in class DisplayVocabBuilder.
	 * I cannot create a new RectangleWithText 
	 * each time the pane needs to be created again in class DisplayVocabBuilder.
	 * If a new RectangleWithText object was created each time, then the
	 * ActionEvent could not read the newer ones. All of these will be put in 
	 * array movableWords and info can be changed by 
	 * mutators in class RectangleWithText.
	 */
	private RectangleWithText rOne = new RectangleWithText();
	private RectangleWithText rTwo = new RectangleWithText();
	private RectangleWithText rThree = new RectangleWithText();
	private RectangleWithText rFour = new RectangleWithText();
	private RectangleWithText rFive = new RectangleWithText();
	
	/* These are the default Texts that go inside each RectangleWithText.
	 * These are here for the same reason as above.  */
	private Text tOne = new Text();
	private Text tTwo = new Text();
	private Text tThree = new Text();
	private Text tFour = new Text();
	private Text tFive = new Text();
	
	/* word/definition pair */
	private AnswerSolution[] wordDef;
	
	public VocabBuilderPane() {
		super("Vocabulary Builder!");
		movableWords = new RectangleWithText[SET];
		words = new Text[NUM_OF_WORDS];
		wordDef = new AnswerSolution[NUM_OF_WORDS];
		pair = new ArrayList<>();
		definitions = new Text[NUM_OF_WORDS];
		initializeRectanglesAndTexts();
		addWordsAndDefinitions();
		setUpUserInterface();
		repaintActionPane();
	}
	
	@Override
	public void repaintActionPane() {
		setUpRectanglesAndTexts();
		setUpDefinitions();
	}
	
	public void setVisibilityFalse(RectangleWithText r, Text t) {
		r.setVisible(false);
		t.setVisible(false);
	}

	public RectangleWithText[] movableWords() {
		return movableWords;
	}
	
	public Text[] definitions() {
		return definitions;
	}
	
	/* sets up part of the action pane to hold the rectangles and texts */
	private void setUpRectanglesAndTexts() {
		action.getChildren().clear();
		
		/* a shuffle happens so it's more challenging for the user */
		Collections.shuffle(pair);
		
		int y = 20;
		int x = 10;
		for (int i = 0; i < SET; ++i, y += 100) {
			movableWords[i].setX(x);
			movableWords[i].setY(y);
			movableWords[i].setWidth(140);
			movableWords[i].setHeight(60);
			movableWords[i].setFill(Color.KHAKI);
			movableWords[i].setStroke(Color.BLACK);
			movableWords[i].setStrokeWidth(3);
			movableWords[i].setLocationY(y);
			movableWords[i].setVisible(true);
			words[i].setText(pair.get(i).getAnswer());
			words[i].setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
			words[i].xProperty().bind(movableWords[i].xProperty().add(30));
			words[i].yProperty().bind(movableWords[i].yProperty().add(30));
			words[i].setVisible(true);
			movableWords[i].setText(words[i]);
			action.getChildren().addAll(movableWords[i], words[i]);
		}
	}//end setUpRectanglesAndText
	
	/* sets up the rest of the action pane to hold the definitions */
	private void setUpDefinitions() {
		/* temp variable to hold the "shuffled" definitions */
		ArrayList<String> temp = new ArrayList<>();
		for (int i = 0; i < SET; ++i) {
			String def = pair.get(i).getSolution();
			temp.add(def);
		}
		
		Collections.shuffle(temp);
		
		int x = 400;
		int y = 50;
		for (int i = 0; i < SET; ++i, y += 100) {
			Text t = new Text(x, y, temp.get(i));
			t.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
			definitions[i] = t;
			action.getChildren().add(t);
		}
	
		setCenter(action);
	}//end setUpDefinitions
	
	/* adds the vocabulary words and definitions to the ArrayList */
	private void addWordsAndDefinitions() {
		String[] wordsToAdd = {"Glorious", "Stupendous", "Fiddle", 
			"Conflit", "Analyze", "Generate", "Examine", "Noun",
			"Stroke", "Idle", "Fetch", "Boil", "Fossil Fuel", "Transport",
			"Community", "Volunteer", "Seesaw", "Supper", "Pair",
			"Merry", "Suit", "Sudden", "Village", "Doctor", "Truly",
			"Send", "Hour", "Large", "High", "Together"};
		
		String[] definitionsToAdd = {"delightful; wonderful; completely enjoyable",
				"causing amazement; astounding; marvelous",
				"a musical instrument of the viol family",
				"to come into collision or disagreement",
				"to examine critically",
				"to bring into existence; cause to be; produce",
				"to inspect carefully", "a person, place, or thing",
				"the act of striking", "doing nothing", 
				"to go and bring back", "to change from a liquid to a gaseous"
				+ " state", "oil, coal, or natural gas, derived from "
				+ "the\nremains of former life", "to carry from one "
				+ " place to another", "a group sharing common interests",
				"to offer onself to service", "park equipment "
				+ " where two people\nride up and down",
				"the evening meal", "two identical things",
				"full of cheerfulness", "a set of men's garments of the same" +
				" color and fabric", "happening without warning",
				"a small community of houses", "surgeon", "fact or truth",
				"transmit to a location", "the same as 60 minutes",
				"bigger than normal", "tall", "involving two or more persons"};
		
		for (int i = 0; i < NUM_OF_WORDS; ++i) {
			wordDef[i] = new AnswerSolution();
			wordDef[i].setAnswer(wordsToAdd[i]);
			wordDef[i].setSolution(definitionsToAdd[i]);
			pair.add(wordDef[i]);
		}
	}//end addWordsAndDefinitions

	/* initializes the rectangles and words to their corresponding arrays */
	private void initializeRectanglesAndTexts() {
		movableWords[0] = rOne;
		movableWords[1] = rTwo;
		movableWords[2] = rThree;
		movableWords[3] = rFour;
		movableWords[4] = rFive;
		
		words[0] = tOne;
		words[1] = tTwo;
		words[2] = tThree;
		words[3] = tFour;
		words[4] = tFive;
	}

	public static void main(String[] args) {
		System.out.println("VocabBuilderPane");
	}
}//end class VocabBuilderPane
