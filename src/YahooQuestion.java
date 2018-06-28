import java.util.ArrayList;

public class YahooQuestion {
	public String main_category;
	public String question;
	public ArrayList<String> nbestanswers;
	public String id;
	
	@Override
	public String toString(){
		return "qid = " + id + "\n" + question;
	}

	public YahooQuestion(String question, String id) {
		this.question = question;
		this.id = id;
	}

	public YahooQuestion(String question) {
		this.question = question;
	}
}
