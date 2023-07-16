package com.dreamtech.tldental.utils;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class Utils {
    public static ArrayList<String> convertStringToImages(String imgs) {
        return new ArrayList<>(Arrays.asList(imgs
                .substring(1, imgs.length() - 1).split(", ")));
    }

    public static String generateSlug(String str) {
        String slug = Normalizer.normalize(str, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        return slug.toLowerCase(Locale.getDefault())
                .replaceAll("\\s+", "-");
    }
}
