package wiki.mini.tags;

import java.util.regex.Pattern;


/**
 * @author mabug
 */
public class Bold extends Style {

    /**
      * Regex
      */
    private static final String REGEX = "''([^'']+)''";

    @Override
    public Pattern getPattern() {
        return Pattern.compile(REGEX);
    }

    @Override
    public String toHTMLString(String word) {
        return "<b>" + word + "</b>";
    }

    @Override
    public String getRegex() {
        return REGEX;
    }
}
