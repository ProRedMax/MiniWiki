package wiki.mini.tags;

import java.util.regex.Pattern;

/**
 * @author mabug
 */
public class H2 extends Style {

    /**
      * Regex
      */
    private static final String REGEX = "^!!\\s(.+)";

    @Override
    public Pattern getPattern() {
        return Pattern.compile(REGEX);
    }

    @Override
    public String toHTMLString(String word) {
        return "<h2>" + word + "</h2>";
    }

    @Override
    public String getRegex() {
        return REGEX;
    }
}
