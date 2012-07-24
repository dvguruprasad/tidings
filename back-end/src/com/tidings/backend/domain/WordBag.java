package com.tidings.backend.domain;

import java.util.*;

public class WordBag {
    private Map<String, Integer> map;

    public WordBag() {
        map = new HashMap<String, Integer>();
    }
    
    public static WordBag create(String content){
        Set<String> words = new HashSet<String>();
        words.add("hello");
        words.add("world");

        List<Integer> frequencies = new ArrayList<Integer>();
        frequencies.add(4);
        frequencies.add(8);
        return create(words, frequencies);
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
}
