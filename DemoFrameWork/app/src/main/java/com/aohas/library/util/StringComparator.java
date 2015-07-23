package com.aohas.library.util;

import java.text.CollationKey;
import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: daocren
 * Date: 12-1-9   17:57
 */
public class StringComparator implements Comparator<String> {
    Collator collator = Collator.getInstance();

    public StringComparator() {
        Locale[] availableLocales = Collator.getAvailableLocales();
        for (Locale availableLocale : availableLocales) {
            if (availableLocale.equals(Locale.CHINA)) {
                collator = Collator.getInstance(Locale.CHINA);
                break;
            }
        }
    }

    @Override
    public int compare(String s, String s1) {
        CollationKey key1 = collator.getCollationKey(s);
        CollationKey key2 = collator.getCollationKey(s1);
        return key1.compareTo(key2);
    }
}