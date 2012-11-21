package com.tidings.backend.domain;

import com.tidings.backend.utils.IActionOnLine;

import java.util.ArrayList;
import java.util.List;

import static com.tidings.backend.utils.FileUtils.foreachLineInFile;

public class Emoticons {
    private static Emoticons emoticons;
    private List<String> list = new ArrayList<String>();


    public static Emoticons create(String fileName) {
        if (null == emoticons) {
            CollectEmoticons collectEmoticons = new CollectEmoticons();
            foreachLineInFile(fileName, collectEmoticons);
            emoticons = new Emoticons(collectEmoticons.list);
        }
        return emoticons;
    }

    private Emoticons(List<String> list) {
        this.list = list;
    }

    public Emoticons(String... list) {
        for (String str : list) {
            this.list.add(str);
        }
    }

    public boolean has(String token) {
        return list.contains(token);
    }

    private static class CollectEmoticons implements IActionOnLine {
        public List<String> list = new ArrayList<String>();

        public void act(String line) {
            list.add(line.split(",")[1]);
        }
    }
}
