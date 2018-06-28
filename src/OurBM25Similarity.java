import org.apache.lucene.search.similarities.BasicStats;
import org.apache.lucene.search.similarities.SimilarityBase;

public class OurBM25Similarity extends SimilarityBase {
    @Override
    protected float score(BasicStats basicStats, float freq, float docLenght) {

        //BM25 constants
        double k1 = 1.20;
        double b = 0.75;

        //BM25 scoring formula
        float scoreBM25 = (float) (Math.log((basicStats.getNumberOfDocuments() - basicStats.getDocFreq() + 0.5) /
                (basicStats.getDocFreq() + 0.5)) * ((k1 + 1) * freq) /
                (k1 * ((1 - b) + b * docLenght / basicStats.getAvgFieldLength()) + freq));

        return scoreBM25;
    }

    @Override
    public String toString() {
        return null;
    }
}
