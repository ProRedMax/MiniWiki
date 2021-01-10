package wiki.mini.tags;

import java.util.regex.Pattern;

/**
 * @author mabug
 */
public class Br extends Style {

    /**
      * Regex
      */
    private static final String REGEX = "^()$";

    @Override
    public Pattern getPattern() {
        return Pattern.compile(REGEX);
    }

    @Override
    public String toHTMLString(String word) {
        return "<br>";
    }

    @Override
    public String getRegex() {
        return REGEX;
    }
}
