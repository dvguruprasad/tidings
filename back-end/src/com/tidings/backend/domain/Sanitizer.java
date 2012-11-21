package com.tidings.backend.domain;

import java.util.List;

public interface Sanitizer {
    List<String> sanitize(String text);
}
