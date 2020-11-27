package wiki.mini.tags;

import java.util.regex.Pattern;

public class Link extends Style{

    private final String REGEX = "#([^#]+\\|[^#]+)#";

    @Override
    public Pattern getPattern() {
        return Pattern.compile(REGEX);
    }

    @Override
    public String toHTMLString(String word) {
        return "<a href=\"" + word.split("\\|")[0] + "\">" + word.split("\\|")[1] + "</a>";
    }

    @Override
    public String getRegex() {
        return REGEX;
    }
}
