package wiki.mini.tags;

import java.util.regex.Pattern;

public class List extends Style {

    private static int currentIndention = 0;

    private static boolean first = false;

    private final String REGEX = "(-+[^-]+)";

    @Override
    public Pattern getPattern() {
        return Pattern.compile(REGEX);
    }

    @Override
    public String toHTMLString(String word) {
        return null;
    }

    @Override
    public String getRegex() {
        return REGEX;
    }
}
