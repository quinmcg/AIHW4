# AIHW4
Artificial Intelligence HW4 - Naive Bayes Classifier


Authors: Quinlan McGaugh and Otis Milliken


Reflection (Part I):

Failures:

1. "I hate my AI class!"
This one fails because, somewhat surprisingly, "ai" is a very positive word according to our training data. We see the probability of "AI" given positive is nearly 10x the probability of seeing "AI" given negative:
"AI" | Neg: 3.1044543985029585E-5
"AI" | Pos: 1.7849849745647407E-4

*This is also not eliminated by our SentAnalysisBest since AI isn't a stop word

2. Bad at detecting sarcasm. 
For example: "I really loved watching this movie. NOT" returns positive even though it is clearly sarcastic and intended to be negative. This is especially important for reviews which tend to be sarcastic/funny in nature. 

3. 


It was also difficult because the text was only five words, two of which were "filler" words (I and my), which were roughly even probabilities between positive and negative.


2. 
3.
4. 

All group members were present and contributing during all work on 
this project

I have neither given nor recieved unauthorized aid on this assignment

We recieved a stop word list from: 