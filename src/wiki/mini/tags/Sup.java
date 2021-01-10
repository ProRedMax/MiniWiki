package wiki.mini.tags;

import java.util.regex.Pattern;

/**
 * @author mabug
 */
public class Sup extends Style {

    /**
      * Regex
      */
    private static final String REGEX = "-[>]([^-]+)-";

    @Override
    public Pattern getPattern() {
        return Pattern.compile(REGEX);
    }

    @Override
    public String toHTMLString(String word) {
        return "<sup>" + word + "</sup>";
    }

    @Override
    public String getRegex() {
        return REGEX;
    }
}
