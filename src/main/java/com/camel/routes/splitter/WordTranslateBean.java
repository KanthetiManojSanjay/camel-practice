package com.camel.routes.splitter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kansanja on 24/05/25.
 */
public class WordTranslateBean {
    Map<String, String> words = new HashMap<>();

    public WordTranslateBean() {
        words.put("A", "Apple");
        words.put("B", "Ball");
        words.put("C", "Cat");
    }

    public String translate(String key) {
        if (!words.containsKey(key)) {
            throw new IllegalArgumentException("Key not a known record" + key);
        }
        return key + "=" + words.get(key);
    }
}
