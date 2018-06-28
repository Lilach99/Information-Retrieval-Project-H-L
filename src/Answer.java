import javafx.util.Pair;

import java.util.ArrayList;

public class Answer {

    public String questionId;
    public ArrayList<Pair<String, Float>> ansScore;

    public Answer(String questionId) {
        this.questionId = questionId;
    }

    public Answer(String questionId, ArrayList<Pair<String, Float>> ansScore) {
        this.questionId = questionId;
        this.ansScore = ansScore;
    }
}
