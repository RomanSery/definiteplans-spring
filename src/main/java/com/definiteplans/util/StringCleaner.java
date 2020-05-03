package com.definiteplans.util;

import java.beans.PropertyEditorSupport;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

public class StringCleaner extends PropertyEditorSupport {
    @Override
    public void setAsText(String text) {
        if (StringUtils.isBlank(text)) {
            setValue(text);
        } else {
            String safe = Jsoup.clean(text, Whitelist.simpleText());
            setValue(safe);
        }
    }
}
