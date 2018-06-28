import org.apache.lucene.document.*;

import java.util.ArrayList;

public class DocumentCreator {

    public static ArrayList<Document> docsCreate (ArrayList<YahooQuestion> questionsList)
    {
        ArrayList<Document> corpus = new ArrayList<>();
//        long key=0; //for the document key
        for (YahooQuestion yahooQuestion : questionsList)
        {
            ArrayList<String> nbestans = yahooQuestion.nbestanswers;
            for (String s : nbestans) {
                Document doc = new Document();
                doc.add(new TextField("body", s, Field.Store.YES));
                corpus.add(doc);
            }
        }
        return corpus;
    }

}
