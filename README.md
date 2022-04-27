# AIHW4
Artificial Intelligence HW4 - Naive Bayes Classifier


Authors: Quinlan McGaugh and Otis Milliken


Reflection (Part I):

Failures:

1. "I hate my AI class!"
This one fails because, somewhat surprisingly, "ai" is a very positive word according to our training data. We see the probability of "AI" given positive is nearly 10x the probability of seeing "AI" given negative:
"AI" | Neg: 3.1044543985029585E-5
"AI" | Pos: 1.7849849745647407E-4

It was also difficult because the text was only five words, two of which were "filler" words (I and my), which were roughly even probabilities between positive and negative.

*This problem is also not eliminated by our SentAnalysisBest since AI isn't a stop word

2. Bad at detecting sarcasm.
For example: "I really loved watching this movie. NOT" returns positive even though it is clearly sarcastic and intended to be negative. This is especially important for reviews which tend to be sarcastic/funny in nature.

3. Since our training data has more negative unique words than positive unique words (32215 vs 28012) then by construction our sentences with a lot of filler words would have a higher probability of being positive than being negative since each filler word skews a little to the positive. This is because the conditional probability divides the number of times a word appears by the number of unique words.

* Our improved classifier tries to eliminate the third problem by having words that don't appear in either hashmaps not affect the negative and positive total probabilities.

4. There might also be too much information in the reviews which cause them to skew the opposite direction. For example, a review could say "The movie started out really amazing and I was excited to watch Jonah Hill but it then ended up being terrible experience". This might read as positive even though the ending sentiment is negative.


2. To run both classifiers you need:
	train (folder) - with data to train off of
	SentAnalysis.java - normal classifier
	SentAnalysisBest.java - our improved classifier
	stop_words_english.txt - text files containing our stop words
	test (folder) - folder with test examples

3. Bugs
	Works as expected and as defined

4.
	Our accuracy on SentAnalysis (normal classifier) from test folder was:
		Positive Precision: 79.24 %
		Negative Precision: 75.57 %
		Total Precision: 77.46%

	Our accuracy on SentAnalysisBest (improved classifier) from test folder was:
		Positive Precision: 78.7%
		Negative Precision: 82.85%
		Total Precision: 80.71%

	-How our best classifier works
		Our best classifier doesn't add common stop words to the positive or negative hashmaps. This takes away the weight that they may have on classifying texts. We did this because we noticed that stop words could often influence the skew of a sentence (For example, I word like have appeared a ton of times in every sentence). These words tend to be random and without sentimental value, and therefore shouldn't influence the sentimentality of the text in question. We implemented this by looping through a list of common stop words taken from: https://countwordsfree.com/stopwords and checking if the new word is in that list. Although this increases the run time of our algorithm, it decreases the amount of space used and most importantly improves the accuracy.
		We also changed the value of lambda in our best classifier from 0.0001 to 0.01. We found that this increased our overall accuracy.
		Lastly, if a word isn't in either hashmaps (which in our case means it is either a rare word or a filler word) then we make it so that that word doesn't affect our total negative or positive probabilities. This addresses problem 3 mentioned in part 1 (see star at problem 3).

	-Some notes on our accuracy
		-Our negative precision is slightly below point of reference but our positive is slightly above.
		-Our restructuring of how we classify in our Best classifier lowers our positive precision slightly but drastically improves our negative.
		-Overall our base classifier outperforms the point of reference and our improved classifier classifier greatly improves our accuracy over our base classifier.

	- How it can be further improved:
		- Potentially we could add neutral classification if the resulting probabilities the word is negative/positive are really close (it isn't clearly positive or negative)
		- we could maybe include some measure of exclamation point/general punctuation. An exclamation could be weighted to highlight that there is extra emphasis.



All group members were present and contributing during all work on
this project

I have neither given nor received unauthorized aid on this assignment

We received a stop word list from: https://countwordsfree.com/stopwords
