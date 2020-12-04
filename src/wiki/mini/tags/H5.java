package wiki.mini.tags;

import java.util.regex.Pattern;

public class H5 extends Style{

    private final String REGEX = "^!!!!!\\s(.+)";

    @Override
    public Pattern getPattern() {
        return Pattern.compile(REGEX);
    }

    @Override
    public String toHTMLString(String word) {
        return "<h5>" + word + "</h5>";
    }

    @Override
    public String getRegex() {
        return REGEX;
    }
}
