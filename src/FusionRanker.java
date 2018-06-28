import javafx.util.Pair;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class FusionRanker {

    private static float k = 40; //the RRF constant
    private static float c = 20; //the CombMNZ threshold

    public static HashMap<String, Float> fusionRankScorer(Pair<TopDocs, IndexReader>[] scorings) throws IOException //each TopDocs contains top-100 docs according to one similarity; the IndexReader is appropriate for reading the docs.
    {
        //create an ArrayList from all of the docs (each one will appear once)
        ArrayList<Document> allDocs = new ArrayList<>();
        //create an array of hashmaps, for each similarity rank
        ArrayList<HashMap<String, Pair<Integer, Float>>> listOfMaps = new ArrayList<>();

        //filling the array & the list
        for (Pair<TopDocs, IndexReader> pair : scorings) {

            listOfMaps.add(scoreToRank(pair)); //adding the ranks HashMap for the current scoring method

            //adding the documents of the current scoring method to the list
            for (ScoreDoc scoreDoc : pair.getKey().scoreDocs) {
                Document currentDoc = pair.getValue().document(scoreDoc.doc);
                if (!allDocs.contains(currentDoc)) //if we hadn't added the doc to the list yet
                {
                    allDocs.add(currentDoc);
                }
            }
        }

        //creating the final ranks HashMap according to the fusion formula
        HashMap<String, Float> finalScores = new HashMap<>();
        //initializing the map
        for (Document document : allDocs) {
            finalScores.put(document.get("body"), (float) 0); //init the score of each doc to 0
        }

        //filling the HashMap according to the Fusion-Rank formula
        //CombMNZ
        for (Document doc : allDocs) {
            int countOfRanks = 0;
            double sumOfScores =0;
            for (HashMap<String, Pair<Integer, Float>> ranks : listOfMaps) {
                if (ranks.containsKey(doc.get("body"))) //add to the formula only if the method ranked the doc in the top-100 docs!
                {
                   if (ranks.get(doc.get("body")).getKey()<=c)
                   {
                       countOfRanks++; //counting the number of ranks which ranked the doc. in the first 5 docs.
                       sumOfScores = sumOfScores + ranks.get(doc.get("body")).getValue(); //add the score
                   }
                }
            }
            finalScores.put(doc.get("body"), (float) (countOfRanks*sumOfScores));
        }

//        //RRF
//        float newScore;
//        for (Document doc : allDocs) {
//            for (HashMap<String, Pair<Integer, Float>> ranks : listOfMaps) {
//                if (ranks.containsKey(doc.get("body"))) //add to the formula only if the method ranked the doc in the top-100 docs!
//                {
//                    newScore = finalScores.get(doc.get("body")).floatValue() + (1 / (ranks.get(doc.get("body")).getValue() + k));
//                    finalScores.replace(doc.get("body"), newScore);
//                }
//            }
//        }

        return finalScores;
    }




    public static HashMap<String, Pair<Integer, Float>> scoreToRank(Pair<TopDocs, IndexReader> scores) throws IOException {

        HashMap<String, Pair<Integer, Float>> docsRanks = new HashMap<>();
        ScoreDoc[] scoreDocs = scores.getKey().scoreDocs; //the scores are ordered in a decreasing order!
        IndexReader ir = scores.getValue();
        for (int i = 0; i < scoreDocs.length; i++) {
            docsRanks.put(ir.document(scoreDocs[i].doc).get("body"), new Pair<>(i + 1, scoreDocs[i].score)); //put the doc & it's rank in the HashMap (i+1 for nonzero rank...)
        }
        return docsRanks;
    }


}
