package com.tidings.backend.domain;

import java.util.*;

public class CategoryDistributions {
    Map<String, CategoryDistribution> categoryDistributionMap;
    private List<Category> categories;

    public CategoryDistributions() {
        categories = Arrays.asList(new Category("Sports"), new Category("Science"), new Category("Entertainment"));
        categoryDistributionMap = new HashMap<String, CategoryDistribution>();
    }

    public void addOrUpdate(Document document) {
        WordBag wordBag = document.wordBag();
        for (String word : wordBag.words()) {
            if (categoryDistributionMap.containsKey(word)) {
                CategoryDistribution categoryDistribution = categoryDistributionMap.get(word);
                categoryDistribution.addOrUpdateCategory(document.category(), wordBag.frequency(word));
            } else {
                CategoryDistribution categoryDistribution = new CategoryDistribution(word, categories);
                categoryDistribution.addOrUpdateCategory(document.category(), wordBag.frequency(word));
                categoryDistributionMap.put(word, categoryDistribution);
            }
        }
    }

    public Collection<CategoryDistribution> list() {
        return categoryDistributionMap.values();
    }
}
