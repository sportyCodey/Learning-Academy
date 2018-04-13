package learningacademy.userapps;

/* class RectangleWithText models a customized Rectangle
 * that can be set to have a Text object 
 * in the Rectangle object.
 * This class is for the movable rectangles for
 * the Vocabulary Builder app, and this 
 * class is for the Math Bowl app. 
 */

import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class RectangleWithText extends Rectangle {
	private Text text;
	
	/* the starting y location for the RectangleWithText */
	private int locationY;

	public RectangleWithText() {
	}
	
	public RectangleWithText(double x, double y, double w, double h) { 
		super(x, y, w, h);
	}
	
	public RectangleWithText(double w, double h) {
		super(w, h);
	}
	
	public Text getTextObject() {
		return text;
	}
	
	public int getLocationY() {
		return locationY;
	}
	
	public void setText(Text text) {
		this.text = text;
	}
	
	public void setLocationY(int locationY) {
		this.locationY = locationY;
	}
	
	public static void main(String[] args) {
		System.out.println("RectangleWithText");
	}
}//end class RectangleWithText
