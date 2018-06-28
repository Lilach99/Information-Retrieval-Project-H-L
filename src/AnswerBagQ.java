import java.io.IOException;
import java.util.ArrayList;

public class AnswerBagQ {

    public String question;
    public String answer;
    public ArrayList<String> questionTerms;
    public ArrayList<String> answerTerms;

    public AnswerBagQ(String question) throws IOException {
        this.question = question;
        questionTerms = (ArrayList<String>) Searcher.getTokens(question);

    }

    public void setAnswer(String answer) throws IOException {
        this.answer = answer;
        answerTerms = (ArrayList<String>) Searcher.getTokens(answer);

    }
}

