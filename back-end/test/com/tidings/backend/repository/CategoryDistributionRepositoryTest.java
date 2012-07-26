package com.tidings.backend.repository;

import com.tidings.backend.domain.CategoryDistribution;
import com.tidings.backend.domain.CategoryScore;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CategoryDistributionRepositoryTest {

    private CategoryDistributionRepository repository;

    @Before
    public void setUp(){
        repository = new CategoryDistributionRepository();
        repository.deleteAll();
    }

    @Test
    public void shouldCreateNewCategoryDistribution(){
        CategoryDistribution distribution = new CategoryDistribution("hadoop");
        distribution.addOrUpdateCategory("infrastructure");
        distribution.addOrUpdateCategory("concept");
        repository.saveOrUpdate(distribution);

        CategoryDistribution savedDistribution = repository.findByWord("hadoop");
        Assert.assertEquals(1, repository.count());
        Assert.assertNotNull(savedDistribution);
        Assert.assertEquals(new CategoryScore(1), savedDistribution.categoryScore("infrastructure"));
    }

    @Test
    public void shouldAddNewCategoryToExistingCategoryDistribution(){
        CategoryDistribution distributionForReplica = new CategoryDistribution("replica");
        distributionForReplica.addOrUpdateCategory("analysis");
        repository.saveOrUpdate(distributionForReplica);

        CategoryDistribution savedDistribution = repository.findByWord("replica");
        savedDistribution.addOrUpdateCategory("technology");
        repository.saveOrUpdate(savedDistribution);
        
        CategoryDistribution updatedDistribution = repository.findByWord("replica");
        Assert.assertEquals(1, repository.count());
        Assert.assertEquals(new CategoryScore(1), updatedDistribution.categoryScore("technology"));
    }

    @Test
    public void shouldUpdateCategoryScoreForAnExistingCategory(){
        CategoryDistribution distribution = new CategoryDistribution("replica");
        distribution.addOrUpdateCategory("analysis");
        repository.saveOrUpdate(distribution);

        distribution.addOrUpdateCategory("analysis");
        distribution.addOrUpdateCategory("analysis");
        distribution.addOrUpdateCategory("analysis");

        repository.saveOrUpdate(distribution);

        CategoryDistribution updatedDistribution = repository.findByWord("replica");
        Assert.assertEquals(1, repository.count());
        Assert.assertEquals(new CategoryScore(4), updatedDistribution.categoryScore("analysis"));
    }

    @Test
    public void shouldReturnCategoryScoreAsZeroForNonExistantCategory(){
        CategoryDistribution distribution = new CategoryDistribution("replica");
        distribution.addOrUpdateCategory("analysis");
        repository.saveOrUpdate(distribution);

        CategoryDistribution updatedDistribution = repository.findByWord("replica");
        Assert.assertEquals(1, repository.count());
        Assert.assertEquals(CategoryScore.EMPTY, updatedDistribution.categoryScore("foobar"));
    }
}