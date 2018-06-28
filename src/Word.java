
public class Word {
	
	public String word;
	double weight;
	
	public Word(String _word, double _weight){
		word = _word;
		weight = _weight;
	}
	
	public String toString(){
		return "[" + word + ", " + weight + "]";
	}
}
