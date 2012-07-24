package com.tidings.backend;

import com.tidings.backend.training.WordBag;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CategorizedWordsMatrixTest {
    @Test
    public void shouldGenerateCategorizedWordsMatrix() {
        CategorizedWordsMatrix matrix = new CategorizedWordsMatrix();

        matrix.train(new Document(wordBag(words("cloud", "hadoop", "spending"), frequencies(4, 2, 1)), "concept"));
        matrix.train(new Document(wordBag(words("hadoop", "HDFS", "mapreduce"), frequencies(3, 3, 4)), "infrastructure"));
        matrix.train(new Document(wordBag(words("hadoop", "tasktrackerjob", "solving"), frequencies(2, 5, 1)), "infrastructure"));

        CategoryDistribution distribution = matrix.categoryDistribution("hadoop");
        Assert.assertEquals(2, distribution.wordFrequency("infrastructure"));
        Assert.assertEquals(1, distribution.wordFrequency("concept"));
    }

    private WordBag wordBag(HashSet<String> words, List<Integer> frequencies) {
        return new WordBag(words, frequencies);
    }

    private HashSet<String> words(String... words) {
        HashSet<String> result = new HashSet<String>();
        for (String word : words) {
            result.add(word);
        }
        return result;
    }

    private List<Integer> frequencies(int... frequencies) {
        List<Integer> result = new ArrayList<Integer>();
        for (Integer frequency : frequencies) {
            result.add(frequency);
        }
        return result;
    }
}