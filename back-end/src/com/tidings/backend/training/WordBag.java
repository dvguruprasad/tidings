package com.tidings.backend.training;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WordBag {
    private final Map<String, Integer> bag;

    public WordBag() {
        bag = new HashMap<String, Integer>();
    }

    public WordBag(Set<String> words, List<Integer> frequencies) {
        bag = new HashMap<String, Integer>();
        int index = 0;
        for (String word : words) {
            bag.put(word, frequencies.get(index++));
        }
    }

    public Set<Map.Entry<String, Integer>> entrySet() {
        return bag.entrySet();
    }
}
