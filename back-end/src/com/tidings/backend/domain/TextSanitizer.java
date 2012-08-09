package com.tidings.backend.domain;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class TextSanitizer {
    private StopWords stopWords;

    public static TextSanitizer create(){
        return new TextSanitizer(new StopWords("data/stopwords.txt"));
    }

    private TextSanitizer(StopWords stopWords) {
        this.stopWords = stopWords;
    }

    public List<String> sanitize(String text) {
        ArrayList<String> result = new ArrayList<String>();
        StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_36, stopWords.get());
        TokenStream tokenStream = analyzer.tokenStream(null, new StringReader(text));
        try {
            while (tokenStream.incrementToken()) {
                CharTermAttribute attribute = tokenStream.getAttribute(CharTermAttribute.class);
                String word = attribute.toString();
                if (word.length() > 2)
                    result.add(word);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
