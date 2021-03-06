package wiki.mini.tags;

import java.util.regex.Pattern;

/**
 * @author mabug
 */
public class H6 extends Style {

    /**
      * Regex
      */
    private static final String REGEX = "^!!!!!!\\s(.+)";

    @Override
    public Pattern getPattern() {
        return Pattern.compile(REGEX);
    }

    @Override
    public String toHTMLString(String word) {
        return "<h6>" + word + "</h6>";
    }

    @Override
    public String getRegex() {
        return REGEX;
    }
}
