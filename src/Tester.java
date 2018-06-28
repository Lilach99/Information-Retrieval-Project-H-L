import javafx.util.Pair;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Tester {

    public static Pair<Double, Double> test(int num) throws IOException {

        ArrayList<YahooQuestion> questions = DataSetParser.ParseJson();
        ArrayList<Document> documents = DocumentCreator.docsCreate(questions);
        RelationsDictionary relationsDictionary = new RelationsDictionary();


        boolean toDo = true;
        File file = new File("C:\\Users\\Home\\IdeaProjects\\InformationRetreivalProject1\\InformationRetreivalProject\\Index");
        if (file.isDirectory()) {
            if (file.list().length > 0) { //index already exists
                toDo = false;
            }
        }
        IndexSearcher indexSearcherDiri = Indexer.CreateIndexSearcher(documents, new LMDirichletSimilarity((float) 19.5), "IndexDiri", toDo);
        IndexSearcher indexSearcherBM = Indexer.CreateIndexSearcher(documents, new OurBM25Similarity(), "IndexBM", toDo);
        IndexSearcher indexSearcherP = Indexer.CreateIndexSearcher(documents, new OurPLenghtSimilarity(), "IndexP", toDo);

        Random r = new Random();
        Answer ans;
        YahooQuestion q;
        double sum = 0;
        double mrr = 0;
        double Per_1;

        ArrayList<Answer> results = new ArrayList<>();

        for (int i = 0; i < num; i++) {
            q = questions.get(r.nextInt(questions.size() - 1)); //random query from the dataset
            Pair<TopDocs, IndexReader> pairBM = Searcher.search(q, q.id, indexSearcherBM, relationsDictionary);
            Pair<TopDocs, IndexReader> pairP = Searcher.search(q, q.id, indexSearcherP, relationsDictionary);
            Pair<TopDocs, IndexReader> pairDiri = Searcher.search(q, q.id, indexSearcherDiri, relationsDictionary);

            HashMap<String, Float> scores = FusionRanker.fusionRankScorer(new Pair[]{pairBM, pairP, pairDiri});

            List<Map.Entry<String, Float>> entries = new ArrayList<>(
                    scores.entrySet()
            );
            Collections.sort(
                    entries
                    , (a, b) -> Float.compare(a.getValue(), b.getValue())
            );
            Collections.reverse(entries);

            ArrayList<Pair<String, Float>> ansScore = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                ansScore.add(new Pair<>(entries.get(j).getKey(), entries.get(j).getValue()));
            }
            results.add(new Answer(q.id, ansScore));
            for (String s : q.nbestanswers) {
                if (entries.get(0).getKey().equals(s)) {
                    sum++;
                    break;
                }
            }
            outerloop:
            for (int k = 0; k < 5; k++) {
                for (String s : q.nbestanswers) {
                    if (entries.get(k).getKey().equals(s)) {
                        mrr = mrr + 1 / (k + 1);
                        break outerloop;
                    }
                }
            }

        }
        Per_1 = sum / num;
        return new Pair<Double, Double>(Per_1, mrr / num);

    }


    public static ArrayList<Answer> testFromFile(ArrayList<YahooQuestion> testQ) throws IOException {

        ArrayList<YahooQuestion> questions = DataSetParser.ParseJson();
        ArrayList<Document> documents = DocumentCreator.docsCreate(questions);
        RelationsDictionary relationsDictionary = new RelationsDictionary();
//        relationsDictionary.buildDictionary(AnswerBagParser.questionsAndAnswers);
//        relationsDictionary.filter((double) 0.9);
//
//        relationsDictionary.saveDictionary();

        boolean toDo = true;
        File file = new File("C:\\Users\\Home\\IdeaProjects\\InformationRetreivalProject1\\InformationRetreivalProject\\Index");
        if (file.isDirectory()) {
            if (file.list().length > 0) { //index already exists
                toDo = false;
            }
        }
        IndexSearcher indexSearcherDiri = Indexer.CreateIndexSearcher(documents, new LMDirichletSimilarity((float) 19.5), "IndexDiri", toDo);
        IndexSearcher indexSearcherBM = Indexer.CreateIndexSearcher(documents, new OurBM25Similarity(), "IndexBM", toDo);
        IndexSearcher indexSearcherP = Indexer.CreateIndexSearcher(documents, new OurPLenghtSimilarity(), "IndexP", toDo);

        ArrayList<Answer> results = new ArrayList<>();

        for (YahooQuestion q : testQ) {
            Pair<TopDocs, IndexReader> pairBM = Searcher.search((q), q.id, indexSearcherBM, relationsDictionary);
            Pair<TopDocs, IndexReader> pairP = Searcher.search((q), q.id, indexSearcherP, relationsDictionary);
            Pair<TopDocs, IndexReader> pairDiri = Searcher.search((q), q.id, indexSearcherDiri, relationsDictionary);

            HashMap<String, Float> scores = FusionRanker.fusionRankScorer(new Pair[]{pairBM, pairP, pairDiri});

            List<Map.Entry<String, Float>> entries = new ArrayList<>(
                    scores.entrySet()
            );
            Collections.sort(
                    entries
                    , (a, b) -> Float.compare(a.getValue(), b.getValue())
            );
            Collections.reverse(entries);

            System.out.println(q.question);
            System.out.println(entries.get(0).getKey());

            ArrayList<Pair<String, Float>> ansScore = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                ansScore.add(new Pair<>(entries.get(i).getKey(), entries.get(i).getValue()));
            }
            results.add(new Answer(q.id, ansScore));

        }
        return results;

    }


}
