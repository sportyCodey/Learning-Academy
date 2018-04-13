package learningacademy;

/* class AnswerSolution is for modeling
 * an answer with it's corresponding solution.
 * This will be used for the apps to help
 * determine if the user gets the answer correct.
 */

public class AnswerSolution {
	private String answer;
	
	private String solution;
	
	public AnswerSolution() {
	}
	
	public AnswerSolution(String answer, String solution) {
		this.answer = answer;
		this.solution = solution;
	}
	
	public String getAnswer() {
		return answer;
	}
	
	public String getSolution() {
		return solution;
	}
	
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	public void setSolution(String solution) {
		this.solution = solution;
	}
	
	public static void main(String[] args) {
		System.out.println("AnswerSolution");
	}
}//end class AnswerSolution
