package com.tidings.backend.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WordBag {
    private Map<String, Integer> map;

    private WordBag() {
        map = new HashMap<String, Integer>();
    }

    public static WordBag create(List<String> words) {
        WordBag wordBag = new WordBag();
        for (String word : words) {
            if (wordBag.map.containsKey(word)) {
                wordBag.map.put(word, wordBag.map.get(word) + 1);
            } else{
                wordBag.map.put(word, 1);
            }
        }
        return wordBag;
    }

    public static WordBag create(Set<String> words, List<Integer> frequencies) {
        WordBag wordBag = new WordBag();
        wordBag.map = new HashMap<String, Integer>();
        int index = 0;
        for (String word : words) {
            wordBag.map.put(word, frequencies.get(index++));
        }
        return wordBag;
    }

    public Set<Map.Entry<String, Integer>> entrySet() {
        return map.entrySet();
    }

    public int size() {
        return map.size();
    }

    public int countFor(String word) {
        return map.containsKey(word) ? map.get(word) : 0;
    }
}
