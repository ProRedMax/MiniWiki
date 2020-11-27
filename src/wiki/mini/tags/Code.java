package wiki.mini.tags;

import java.util.regex.Pattern;

public class Code extends Style{

    private final String REGEX = "\\|\\|\\|([^\\|\\|\\|]+)\\|\\|\\|";

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
