package com.tidings.backend.domain;

import org.apache.lucene.analysis.*;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.io.Reader;
import java.util.Set;

public class MicroblogAnalyzer extends StopwordAnalyzerBase {
    protected MicroblogAnalyzer(Version version, Set<?> stopwords) {
        super(version, stopwords);
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
        final KeywordTokenizer src = new KeywordTokenizer(reader);
        TokenStream tok = new LowerCaseFilter(matchVersion, src);
        tok = new StopFilter(matchVersion, tok, stopwords);
        return new TokenStreamComponents(src, tok) {
            @Override
            protected boolean reset(final Reader reader) throws IOException {
                return super.reset(reader);
            }
        };
    }
}
