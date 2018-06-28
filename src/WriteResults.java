import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import javafx.util.Pair;
import org.apache.lucene.search.TopDocs;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

public class WriteResults {

    public static void writeToJson(ArrayList<Answer> results) throws IOException {
        ArrayList<result> res = new ArrayList<>();
        JsonWriter writer = new JsonWriter(new FileWriter("C:/Users/Home/IdeaProjects/InformationRetreivalProject1/InformationRetreivalProject/answers.json"));
        writer.beginArray();
        for (Answer answer : results) {
            writer.beginObject();
            writer.name("id").value(answer.questionId);
            writer.name("answers");
            writer.beginArray();
            for (Pair<String, Float> p : answer.ansScore) {
                writer.beginObject();
                writer.name("answer").value(p.getKey());
                writer.name("score").value(String.valueOf(p.getValue()));
                writer.endObject();
            }
            writer.endArray();
            writer.endObject();
        }
        writer.endArray();
        writer.close();
    }
}

