import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Indexer {

    public static IndexSearcher CreateIndexSearcher(ArrayList<Document> corpus, Similarity similarity, String name, boolean towrite) throws IOException {

        Analyzer enAnalyzer = new EnglishAnalyzer();
        IndexWriter iw = CreateIndex(enAnalyzer, similarity, name);

//        do it once!!
        if(towrite ==true) {
            for (Document doc : corpus) {
                iw.addDocument(doc);
            }
        }

            iw.commit();
            iw.close();

            DirectoryReader dr = DirectoryReader.open(iw.getDirectory());
            IndexSearcher is = new IndexSearcher(dr);
            is.setSimilarity(similarity);
            return is;
        }


    private static IndexWriter CreateIndex(Analyzer analyzer, Similarity similarity, String name) throws IOException {

        IndexWriterConfig conf = new IndexWriterConfig(analyzer);
        conf.setSimilarity(similarity);
        Directory dir = FSDirectory.open(Paths.get("C:\\Users\\Home\\IdeaProjects\\InformationRetreivalProject1\\InformationRetreivalProject\\Index\\"+name));
        //incremental indexing
        conf.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        //create an index writer
        return new IndexWriter(dir, conf);
    }
}
