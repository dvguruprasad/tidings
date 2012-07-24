package com.tidings.backend.domain;

import java.util.*;

public class CategorizedWordsMatrix {
    Map<String, CategoryDistribution> distributions;

    public CategorizedWordsMatrix() {
        distributions = new HashMap<String, CategoryDistribution>();
    }

    public CategoryDistribution categoryDistribution(String word) {
        return distributions.get(word);
    }

    public void train(Document document) {
        WordBag wordBag = document.wordBag();
        for (Map.Entry<String, Integer> entry : wordBag.entrySet()) {
            String word = entry.getKey();
            if (distributions.get(word) != null) {
                distributions.get(word).addOrUpdateCategory(document.category(), entry.getValue());
            } else {
                CategoryDistribution distribution = new CategoryDistribution(word);
                distribution.addOrUpdateCategory(document.category(), entry.getValue());
                distributions.put(word, distribution);
            }
            
        }
    }
    
    public Collection<CategoryDistribution> distributions(){
        return distributions.values();
    }
}