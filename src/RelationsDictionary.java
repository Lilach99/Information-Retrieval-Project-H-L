import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.StringMap;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class RelationsDictionary {

    private ArrayList<String> targetWords;
    private Map<String, HashMap<String, Word>> relations;
    private Map<String, Double> wordsFreq;

    public RelationsDictionary() throws FileNotFoundException {
        relations = new HashMap<>();
        wordsFreq = new HashMap<>();
        HashMap<String, StringMap<StringMap<String>>> tempString = new HashMap<>();

        Gson gson = new GsonBuilder().create();
        JsonReader jr = new JsonReader(new FileReader("dictionary.json"));
        tempString = gson.fromJson(jr, HashMap.class);

        for(String s : tempString.keySet())
        {
            HashMap<String, Word> hashMap = new HashMap<>();
            StringMap<StringMap<String>> stringMap = tempString.get(s);

            for (String st : stringMap.keySet())
            {
                Double d = Double.parseDouble(String.valueOf(stringMap.get(st).get("weight")));
                Word word = new Word(stringMap.get(st).get("word"), d);
                hashMap.put(st, word);
            }
            relations.put(s, hashMap);
        }

        jr = new JsonReader(new FileReader("frequencies.json"));
        wordsFreq = gson.fromJson(jr, HashMap.class);

    }

    public void buildDictionary (HashMap<Integer, AnswerBagQ> answerBagQHashMap)
    {
        for(Integer integer : answerBagQHashMap.keySet())
        {
            AnswerBagQ answerBagQ = answerBagQHashMap.get(integer);
            setTargetWords(answerBagQ.questionTerms);
            addRelation(answerBagQ.answerTerms);
        }

    }

    public HashMap<String, Word> GetRelatedWords(String word) {
        try {
            return relations.get(word);
        } catch (Exception e) {
            return new HashMap<String, Word>();
        }
    }

    public Set<String> GetRelatedWordsArray(String word) {
        HashMap<String, Word> hashMap = GetRelatedWords(word);
        if(hashMap!=null)
            return  hashMap.keySet();
        return null;
    }

    public void setTargetWords(ArrayList<String> words) {
        targetWords = words; //words of question, which we want to connect to other words.
        for (String word : targetWords)
        {
            if(wordsFreq.get(word)==null)
            {
                wordsFreq.put(word, 1.0);
            }
            else //it is already exists
            {
                Double freq = wordsFreq.get(word);
                wordsFreq.replace(word, freq+1.0);
            }
        }
    }

    public void addRelation(String relatedWord) {
        //IDEA - implement the relation with two direction - aRb iff bRa
        for (String word : targetWords) {
            HashMap<String, Word> relatedWords;

            relatedWords = relations.get(word);

            if (relatedWords == null) {
                //if the entry does not exist - add one
                relatedWords = new HashMap<String, Word>();
                relations.put(word, relatedWords);
            }

            //Add the relation to the word
            try {
                //if entry already exist, simply put more weight.
                relatedWords.get(relatedWord).weight++;
            } catch (Exception e) {
                //if entry doesn't exist, initialize it with weight 1
                relatedWords.put(relatedWord, new Word(relatedWord, 1));
            }
        }
    }

    public void addRelation(ArrayList<String> relatedWords) {

        for (String word : relatedWords)
            addRelation(word);
    }

    public void filter(Double threshold)
    {
        for(String term : relations.keySet())
        {
            int termFreq = wordsFreq.get(term).intValue();
            Map<String , Word> relatedWords = relations.get(term);
            Iterator<Map.Entry<String, Word>> it = relatedWords.entrySet().iterator();
            while (it.hasNext())
            {
                Word w = it.next().getValue();
                double termAnsFreq = w.weight;
                if(termAnsFreq/termFreq>1.0) System.out.println("error!!");
                if (termAnsFreq/termFreq < threshold || w.word.equals(term))
                {
                    it.remove();
                }
            }
        }
        Iterator<Map.Entry<String, HashMap<String, Word>>> itr = relations.entrySet().iterator();
        while (itr.hasNext())
        {
            if (itr.next().getValue().size()==0)
            {
                itr.remove();
                wordsFreq.remove(itr.next().getKey());
            }
        }


    }

    public void saveDictionary ()
    {
        Gson gson = new Gson();
        String json = gson.toJson(relations);

        try {
            FileWriter fileWriter = new FileWriter("dictionary.json");
            fileWriter.write(json);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        json = gson.toJson(wordsFreq);

        try {
            FileWriter fileWriter = new FileWriter("frequencies.json");
            fileWriter.write(json);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
