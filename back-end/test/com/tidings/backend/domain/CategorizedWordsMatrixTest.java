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
        assertCategoryDistribution("hadoop", new CategoryScore(3), new CategoryScore(3));
        assertCategoryDistribution("cloud", CategoryScore.EMPTY, new CategoryScore(1));
        assertCategoryDistribution("tasktrackerjob", new CategoryScore(1), CategoryScore.EMPTY);
    }

    private void assertCategoryDistribution(String word, CategoryScore infrastructureScore, CategoryScore analysisScore) {
        CategoryDistribution distribution = matrix.categoryDistribution(word);
        Assert.assertEquals(infrastructureScore, distribution.categoryScore("infrastructure"));
        Assert.assertEquals(analysisScore, distribution.categoryScore("analysis"));
    }

    private WordBag wordBag(String... words) {
        return WordBag.create(Arrays.asList(words));
    }
}