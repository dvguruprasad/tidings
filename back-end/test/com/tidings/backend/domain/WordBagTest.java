package com.tidings.backend.domain;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class WordBagTest {
    @Test
    public void shouldCreateWordBagFromContent() {
        WordBag wordBag = WordBag.create(words());
        Assert.assertEquals(3, wordBag.size());
        Assert.assertEquals(3, wordBag.countFor("picked"));
        Assert.assertEquals(2, wordBag.countFor("larry"));
        Assert.assertEquals(1, wordBag.countFor("fight"));
    }

    private ArrayList<String> words() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("larry");
        list.add("larry");
        list.add("picked");
        list.add("picked");
        list.add("picked");
        list.add("fight");
        return list;
    }
}
