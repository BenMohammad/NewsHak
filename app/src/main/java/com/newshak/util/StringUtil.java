package com.newshak.util;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    private Context mContext;

    public StringUtil(Context context) {
        this.mContext = context;
    }

    public static boolean isBlank(CharSequence string) {
        return (string == null || string.toString().trim().length() == 0);
    }

    public static String toTitleCase(String str) {
        if(str == null) {
            return null;
        }

        boolean space = true;
        StringBuilder builder = new StringBuilder(str);
        final int len = builder.length();

        for(int i = 0; i < len; i++) {
            char c = builder.charAt(i);
            if(space) {
                if(!Character.isWhitespace(c)) {
                    builder.setCharAt(i, Character.toTitleCase(c));
                    space = false;
                }
            } else if (Character.isWhitespace(c)) {
                space = true;
            } else {
                builder.setCharAt(i, Character.toLowerCase(c));
            }
        }
        return builder.toString();
    }

    public static List<String> extractUrl(String text) {
        List<String> containedUrls = new ArrayList<>();
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);

        while(matcher.find()) {
            containedUrls.add(text.substring(matcher.start(0),
                    matcher.end(0)));
        }
        return containedUrls;
    }
}
