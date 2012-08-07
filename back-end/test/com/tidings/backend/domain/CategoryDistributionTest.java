package com.tidings.backend.domain;

import org.junit.Test;

public class CategoryDistributionTest {
    @Test
    public void shouldComputeProbabilities(){
        CategoryDistribution distribution = new CategoryDistribution("big");
        distribution.addOrUpdateCategory("infrastructure", 4);
        distribution.addOrUpdateCategory("analysis", 7);
        distribution.computeProbabilities(new Probability(null));
    }
}
