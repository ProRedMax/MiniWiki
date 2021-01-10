package wiki.mini.tags;

import java.util.regex.Pattern;

/**
 * @author mabug
 */
public class H1 extends Style {

    /**
      * Regex
      */
    private static final String REGEX = "^!\\s(.+)";

    @Override
    public Pattern getPattern() {
        return Pattern.compile(REGEX);
    }

    @Override
    public String toHTMLString(String word) {
        return "<h1>" + word + "</h1>";
    }

    @Override
    public String getRegex() {
        return REGEX;
    }
}
