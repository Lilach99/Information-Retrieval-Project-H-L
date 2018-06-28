import javafx.util.Pair;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import java.io.Console;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {

        System.out.println("Hello World");

        AnswerBagParser.ParseAnswerBag();

        LineFileParser.ReadFile("finalEval.txt");

        ArrayList<Answer> answers = Tester.testFromFile(LineFileParser.testQuestions);
        WriteResults.writeToJson(answers);
    }

    public static double getDocKey(Document document) {
        return document.getField("key").numericValue().doubleValue();
    }


}
