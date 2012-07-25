package com.tidings.backend.domain;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class CategorizedWordsMatrixTest {

    private CategorizedWordsMatrix matrix;

    @Before
    public void setUp() {
        matrix = new CategorizedWordsMatrix();
    }

    @Test
    public void shouldGenerateCategorizedWordsMatrix() {
        matrix.train(new Document(wordBag("cloud", "hadoop", "spending", "hadoop", "hadoop"), "analysis"));
        matrix.train(new Document(wordBag("hadoop", "HDFS", "mapreduce", "hadoop"), "infrastructure"));
        matrix.train(new Document(wordBag("hadoop", "tasktrackerjob", "solving"), "infrastructure"));
        assertCategoryDistribution("hadoop", 3, 3);
        assertCategoryDistribution("cloud", 0, 1);
        assertCategoryDistribution("tasktrackerjob", 1, 0);
    }

    private void assertCategoryDistribution(String word, int countForInfrastructure, int countForAnalysis) {
        CategoryDistribution distribution = matrix.categoryDistribution(word);
        Assert.assertEquals(countForInfrastructure, distribution.wordFrequency("infrastructure"));
        Assert.assertEquals(countForAnalysis, distribution.wordFrequency("analysis"));
    }

    private WordBag wordBag(String... words) {
        return WordBag.create(Arrays.asList(words));
    }
}