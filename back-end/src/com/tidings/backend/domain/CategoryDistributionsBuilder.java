package com.tidings.backend.domain;

import com.tidings.backend.repository.CategoryDistributionRepository;
import com.tidings.backend.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategoryDistributionsBuilder {
    private CategoryDistributionRepository distributionRepository;
    private CategoryRepository categoryRepository;

    public CategoryDistributionsBuilder(CategoryDistributionRepository distributionRepository, CategoryRepository categoryRepository) {
        this.distributionRepository = distributionRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryDistribution> distributions(Document document) {
        List<Category> categories = categoryRepository.all();
        ArrayList<CategoryDistribution> distributions = new ArrayList<CategoryDistribution>();
        Set<String> words = document.wordBag().words();
        for (String word : words) {
            CategoryDistribution distribution = distributionRepository.findByWord(word);
            if (null == distribution) {
                distribution = new CategoryDistribution(word, categories);
            }
            distribution.addOrUpdateCategory(document.category(), document.wordBag().frequency(word));
            distributions.add(distribution);
        }
        return distributions;
    }
}
