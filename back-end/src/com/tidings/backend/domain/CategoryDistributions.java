package com.tidings.backend.domain;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryDistributions {
    Map<String, CategoryDistribution> categoryDistributionMap;
    private List<Category> categories;
    private int count = 1;

    public CategoryDistributions(List<Category> categories) {
        this.categories = categories;
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
