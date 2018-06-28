import java.io.*;
import java.util.Arrays;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

public class DataSetParser {

    public static ArrayList<YahooQuestion> ParseJson() {

        BufferedReader br = null;
        try {
            Gson gson = new GsonBuilder().create();
            JsonReader jr = new JsonReader(new FileReader("C:/Users/Home/IdeaProjects/InformationRetreivalProject1/InformationRetreivalProject/nfL6.json"));
            YahooQuestion[] qs = gson.fromJson(jr, YahooQuestion[].class);
            ArrayList<YahooQuestion> questions = new ArrayList<>(Arrays.asList(qs));
            return questions;
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }

        return null;
    }

}
