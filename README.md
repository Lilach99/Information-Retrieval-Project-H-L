# Information Retreival Project - H&L

This is an Information Retreival system, for the Non-Factoid Passage Retreival task.
It retreives the top-5 answers for questions in natural language, taken from the Yahoo WebScope L6 dataset.
The system use variouse information retreival techniques:
1. **Similarities**: BM25 Similarity, Pivot-Lenght Similarity, Dirichlet Similarity
2. All combined using a Fusion Ranking Formula - **CombMNZ**.
3. **Query Expansion** using a relations dictionary we built, using another dataset.

## Getting Started

In order to run the project on your computer, you have to install a Java developing environement, such as Intelj Idea. 
After downloading the project, you should make sure you add the needed libraries to the project (Lucene libraries and Gson library). 
In addition, you have to add the json file of the Webscope L6 dataset to the project directory. 

## The Classes

The project's important classes:
1. **Indexer** - indexes the documents (all of the questions answers) according to a given similarity.
2. **Similarities**: OurBM25Similarity, OurPLenghtSimilarity - those classes compute the score of one term in the answer, regarding to the question, according to the BM25 formula and the Pivot Document Lenght formula, respectively. 
3. **Searcher** - searches answers for a given question, according to a specific similarity.
4. **RelationsDictionary** - a class for building the data structure of the relations dictionary, and filling it with relations between word (using the 'answerbag' dataset).
5. **FusionRanker** - gets some sets of scored documents and re-ranks them according to a fusion ranking formula (ComnMNZ or RRF, we found CombMNZ as better for the results).

## Running the tests

To test the system, you just have to run the following python script through the cmd: https://mw5.haifa.ac.il/pluginfile.php/270006/mod_resource/content/0/eval.py
For usage run the command python eval.py -h.

### Tests

The tests tests the quality of the search according to two neasurments: 
1. Percision@1 - the percentage of queries which where answered right, meaning the first answer the system retreived for them was a good answer (	one of the "nbestanswers" of the query).
2. MRR@5 - prefers answers that are ranked as higher as possible in the list of 5 answers retreived.

The output of the tests is those two measurements values:

```
Percision@1: 0.263
MRR@5: 0.329
```

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management

## Authors

* **Lilach Saban** - *Student at Haifa University* 
* **Shachar Har-Shuv** - *Student at Haifa University* 

## Acknowledgments

Mihai Surdeanu, Massimiliano Ciaramita  and Hugo Zaragoza. 2011. Learning to Rank Answers to Non-Factoid Questions from Web Collections. https://www.mitpressjournals.org/doi/10.1162/COLI_a_00051

Amit Singhal, Chris Buckley and Mandar Mitra. 1996. Pivoted Document Length Normalization. http://singhal.info/pivoted-dln.pdf

G. V. Cormack, C. L. A. Clarke and Stefan B¨uttcher . 2009. Reciprocal Rank Fusion outperforms Condorcet and individual Rank Learning Methods. http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.150.2291&rep=rep1&type=pdf

Lucene LMDirichletSimilarity.
https://lucene.apache.org/core/4_7_1/core/org/apache/lucene/search/similarities/LMDirichletSimilarity.html

Lucene EnglishAnalyzer.
https://lucene.apache.org/core/5_5_1/analyzers-common/org/apache/lucene/analysis/en/EnglishAnalyzer.html

The 'answerbag' dataset.
https://github.com/rmit-ir/answerbag-dataset

The Yahoo! Webscope L6 dataset.
https://ciir.cs.umass.edu/downloads/nfL6/



