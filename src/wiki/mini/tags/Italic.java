package wiki.mini.tags;

import java.util.regex.Pattern;

public class Italic extends Style{

    private final String REGEX = "\\/\\/([^\\/\\/]+)\\/\\/";

    @Override
    public Pattern getPattern() {
        return Pattern.compile(REGEX);
    }

    @Override
    public String toHTMLString(String word) {
        return "<i>" + word + "</i>";
    }

    @Override
    public String getRegex() {
        return REGEX;
    }
}
