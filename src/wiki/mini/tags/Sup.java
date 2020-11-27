package wiki.mini.tags;

import java.util.regex.Pattern;

public class Sup extends Style {

    private final String REGEX = "-[>]([^-]+)-";

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
