import org.apache.lucene.search.similarities.BasicStats;
import org.apache.lucene.search.similarities.SimilarityBase;

public class OurPLenghtSimilarity extends SimilarityBase {

    @Override
    protected float score(BasicStats basicStats, float freq, float docLenght) { //scores the document doc

        double s = 0.13;
        float scoreP = (float) ((1 + Math.log(1 + Math.log(freq))) /
                ((1 - s) + s * docLenght / basicStats.getAvgFieldLength()) * 1 *
                Math.log((basicStats.getNumberOfDocuments() + 1) / basicStats.getDocFreq()));

        return scoreP;
    }

    @Override
    public String toString() {
        return null;
    }
}


