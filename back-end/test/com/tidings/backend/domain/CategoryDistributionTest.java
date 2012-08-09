package com.tidings.backend.domain;

import org.junit.Assert;
import org.junit.Test;

public class CategoryDistributionTest {
    @Test
    public void shouldComputeProbabilities() {
        CategoryDistribution distribution = new CategoryDistribution("big");
        distribution.addOrUpdateCategory("infrastructure", 4);
        distribution.addOrUpdateCategory("analysis", 7);
        distribution.computeProbabilities();

        float infrastructureProbability = distribution.probability("infrastructure");
        float analysisProbability = distribution.probability("analysis");
        float dummyProbability = distribution.probability("dummy");

        Assert.assertEquals(0.3636, infrastructureProbability, .0001);
        Assert.assertEquals(0.6363, analysisProbability, .0001);
        Assert.assertEquals(0.0, dummyProbability, 0);
    }
}
