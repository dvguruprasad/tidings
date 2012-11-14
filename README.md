tidings
=======

The deluge of content(text, images, videos, audio, etc.) available
on the web concerning anything, be it science, politics, sports, history,
etc. makes it difficult to find content related to a specific field of
interest. What is required is a system that can source
content from the web, classify and present it in a manner that is easier to
consume. tidings is one such system that deals text content.
 
tidings pulls text content from a bunch of rss feeds and automatically
classifies
them into predetermined categories.
 
The content(document) classification part of tidings is a Naive Bayes
classifier. Naive Bayes classification is a supervised probabilistic
text classification model. The idea is to predict the category for a document
based
on the existing knowledge of the categories of a myriad others. The probability
of a document belonging to each category is
computed and the document assigned to the category with the highest
probability.
 
The probability of a document D with n words belonging to a category C can be
defined as:
 
P(C | D) = (product of P(W(i) | C) for i from 1 to n) * P(C)
where
P(W(i)|C) is the probability of a document containing word W(i) belonging to
category C, and
P(C) is the probability of a document belonging to category C.
and:
P(W(i)|C) = Number of occurrences of W(i) in documents categorized as C / Total
number of words classified as C
P(C) = Number of documents categorized as C / Total number of documents
 
Computation of P(W(i)|C) and P(C) is on what is called a training set. Training
set constitutes data collected during training.
 
Classification in tidings can thus be divided into two phases:
 
Training: Is is the act of feeding the system, documents that have already
labelled
as belonging to different categories. During this process, a document is
the word frequencies, or the number of occurrences of every word,
captured against the given category. Once word frequencies are captured,
probability is computed for each word across all categories [P(W(i)|C)].
 
Classification: Is the act of classifying a document as belonging to a
category.
Here, the probability of the document belonging to each category is computed
[P(C|D)] and the document assigned to the category with the highest
probability.
 
tidings currently classifies documents into four categories - Science,
Entertainment, Sports
and Software development. We collected 68k words as part of training.
The system can be used to classify documents into any category set, after
training.
