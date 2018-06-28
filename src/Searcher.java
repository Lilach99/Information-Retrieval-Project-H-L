import javafx.util.Pair;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;

import java.io.IOException;
import java.util.*;

public class Searcher {

    public static Pair<TopDocs, IndexReader> search(YahooQuestion yahooQuestion, String qId, IndexSearcher indexSearcher, RelationsDictionary relationsDictionary) throws IOException {

        Query query = QueryParse(yahooQuestion.question, new EnglishAnalyzer());
        query = QueryExpansion(query, indexSearcher, relationsDictionary);

        TopDocs result = indexSearcher.search(query, 20);
//                //top-5 answers
//                ScoreDoc[] docs = result.scoreDocs;

        IndexReader ir = indexSearcher.getIndexReader();


        return new Pair<TopDocs, IndexReader>(result, ir);
    }

    private static Query QueryParse(String queryText, Analyzer anaylyzer) {
        QueryParser parser = new QueryParser("body", anaylyzer);
        try {
            //escape special characters from query
            return parser.parse(QueryParser.escape(queryText));
        } catch (ParseException e) {
            System.out.println("Exception occured!");
        }
        return null;
    }

    private static Query QueryExpansion(Query query, IndexSearcher indexSearcher, RelationsDictionary relationsDictionary) throws IOException {

        //extract terms from a query
        String[] terms = query.toString().split(" ");
        for (int i = 0; i < terms.length; i++) {
            terms[i] = terms[i].split(":")[1];
            //System.out.println(terms[i]);
        }
        if(terms.length > 4 ) return query; //do not expanse it if there are enough words

        LinkedList<String> keyTerms = new LinkedList<String>(Arrays.asList(terms));
        //remove "unimportant" terms:
        IndexReader ir = indexSearcher.getIndexReader();

        ListIterator it = keyTerms.listIterator();

        while (it.hasNext()) {
            String term = it.next().toString();
            long termFreq = ir.totalTermFreq(new Term("body", term));
            double idf = Math.log((ir.numDocs())/(ir.docFreq(new Term("body", term))+1));

//            System.out.println(term + " " + termFreq);
//termFreq - 35000
            if (idf < 7) {
                it.remove();
            }

        }
//        System.out.println(keyTerms);

        String newQuery = "";
        for (int m=0; m<terms.length; m++) {
            newQuery += terms[m] + " ";
        }


        //get additional terms
        for (String word : keyTerms) {
            HashMap<String, Word> nTerms = relationsDictionary.GetRelatedWords(word);
            if (nTerms != null) {
                for (String nTerm : nTerms.keySet()) {
                    if (nTerms.get(nTerm).weight > 8)
                        newQuery += nTerm + " ";
                }
            }
        }

        return QueryParse(newQuery, new EnglishAnalyzer());

    }

    public static List<String> getTokens(String text) throws IOException {
        Analyzer analyzer = new EnglishAnalyzer();

        TokenStream ts = analyzer.tokenStream("name", text);
        ts.reset();

        List<String> result = new ArrayList<>();

        try {
            while (ts.incrementToken()) {
                if (!(result.contains(ts.getAttribute(CharTermAttribute.class).toString())))
                    result.add(ts.getAttribute(CharTermAttribute.class).toString());
            }
        } catch (IOException e) {
        }
        return result;
    }

}
