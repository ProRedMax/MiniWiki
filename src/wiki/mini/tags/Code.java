package wiki.mini.tags;

import java.util.regex.Pattern;

/**
 * @author mabug
 */
public class Code extends Style {

    /**
      * Regex
      */
    private static final String REGEX = "\\|\\|\\|([^\\|\\|\\|]+)\\|\\|\\|";

    @Override
    public Pattern getPattern() {
        return Pattern.compile(REGEX);
    }

    @Override
    public String toHTMLString(String word) {
        return "<code>" + word + "</code>";
    }

    @Override
    public String getRegex() {
        return REGEX;
    }
}
