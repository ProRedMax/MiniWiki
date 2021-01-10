package wiki.mini.tags;

import java.util.regex.Pattern;

/**
 * @author mabug
 */
public class Hr extends Style {

    /**
      * Regex
      */
    private static final String REGEX = "^(---)$";

    @Override
    public Pattern getPattern() {
        return Pattern.compile(REGEX);
    }

    @Override
    public String toHTMLString(String word) {
        return "<hr>";
    }

    @Override
    public String getRegex() {
        return REGEX;
    }
}
