package com.tidings.backend;

import com.tidings.backend.training.WordBag;

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
                distributions.get(word).addOrUpdateCategory(document.category());
            } else {
                CategoryDistribution distribution = new CategoryDistribution(word);
                distribution.addOrUpdateCategory(document.category());
                distributions.put(word, distribution);
            }
            
        }
    }
    
    public Collection<CategoryDistribution> distributions(){
        return distributions.values();
    }
}