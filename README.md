# Information Retreival Project - H&S

This is an Information Retreival system, for the Non-Factoid Passage Retreival task.
It retreives the top-5 answers for questions in natural language, taken from the Yahoo WebScope L6 dataset.
The system use variouse information retreival techniques:
1. Similarities: BM25 Similarity, Pivot-Lenght Similarity, Dirichlet Similarity
2. All combined using a Fusion Ranking Formula - CombMNZ.
3. Query Expansion using a relations dictionary we built, using another dataset.

## Getting Started

In order to run the project on your computer, you have to install a Java developing environement, such as Intelj Idea. 
After downloading the project, you should make sure you add the needed libraries to the project (lucene libraries and Gson library).

### Prerequisites

What things you need to install the software and how to install them

```
Give examples
```

### Installing

A step by step series of examples that tell you how to get a development env running

Say what the step will be

```
Give the example
```

And repeat

```
until finished
```

End with an example of getting some data out of the system or using it for a little demo

## Running the tests

To test the system, you just have to run the Main class. You can set the number of random queries the tests - it is the input of the function 'Tester.Test()'. 

### Tests

The tests tests the quality of the search according to two neasurments: 
1. Percision@1 - the percentage of queries which where answered right, meaning the first answer the system retreived for them was a good answer (	one of the "nbestanswers" of the query).
2. MRR@5 - prefers answers that are ranked as higher as possible in the list of 5 answers retreived.

The output of the tests is those two measurements values:


```
Percision@1: 0.306
MRR@5: 0.306
```


## Deployment

Add additional notes about how to deploy this on a live system

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management

## Authors

* **Lilach Saban** - *Student at Haifa University* 
* **Shachar Har-Shuv** - *Student at Haifa University* 

## Acknowledgments

/TODO
* add articles!


