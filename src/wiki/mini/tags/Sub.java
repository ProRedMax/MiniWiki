package wiki.mini.tags;

import java.util.regex.Pattern;

public class Sub extends Style {

    private final String REGEX = "-[vV]([^-]+)-";

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
