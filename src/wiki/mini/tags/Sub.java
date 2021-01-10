package wiki.mini.tags;

import java.util.regex.Pattern;

/**
 * @author mabug
 */
public class Sub extends Style {

    /**
      * Regex
      */
    private static final String REGEX = "-[vV]([^-]+)-";

    @Override
    public Pattern getPattern() {
        return Pattern.compile(REGEX);
    }

    @Override
    public String toHTMLString(String word) {
        return "<sub>" + word + "</sub>";
    }

    @Override
    public String getRegex() {
        return REGEX;
    }
}
