import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class AnswerBagParser {

    public static HashMap<Integer, AnswerBagQ> questionsAndAnswers = new HashMap<>();

    public AnswerBagParser() {

    }

    public static void ParseAnswerBag() {
        ReadFile("questions.txt");
        ReadFile("answers.txt");
    }

    public static void handleQuestion(String s) throws IOException {
        String parts[] = s.split("\t", 2);
        AnswerBagQ answerBagQ = new AnswerBagQ(parts[1]);
        Integer id = Integer.parseInt(parts[0]);
        questionsAndAnswers.put(id, answerBagQ);
    }

    public static void handleAnswer(String s) throws IOException {
        String parts[] = s.split("\t", 2);
        Integer id = Integer.parseInt(parts[0]);
        AnswerBagQ answerBagQ = questionsAndAnswers.get(id);
        answerBagQ.setAnswer(parts[1]);
        questionsAndAnswers.replace(id, answerBagQ);
    }

    public static void ReadFile (String fileName)
    {
        InputStream ins = null; // raw byte-stream
        Reader r = null; // cooked reader
        BufferedReader br = null; // buffered for readLine()
        try {
            String s;
            ins = new FileInputStream(fileName);
            r = new InputStreamReader(ins, "UTF-8"); // leave charset out for default
            br = new BufferedReader(r);
            if(fileName=="questions.txt") {
                while ((s = br.readLine()) != null) {
                    handleQuestion(s);
                }
            }
            if(fileName=="answers.txt") {
                while ((s = br.readLine()) != null) {
                    handleAnswer(s);
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage()); // handle exception
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Throwable t) { /* ensure close happens */ }
            }
            if (r != null) {
                try {
                    r.close();
                } catch (Throwable t) { /* ensure close happens */ }
            }
            if (ins != null) {
                try {
                    ins.close();
                } catch (Throwable t) { /* ensure close happens */ }
            }
        }

    }
}
