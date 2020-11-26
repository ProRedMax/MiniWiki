package wiki.mini.tags;

import java.util.regex.Pattern;

public class h1 extends Style{
    @Override
    public Pattern getPattern() {
        return Pattern.compile("!\\s\\w+");
    }

    @Override
    public String toHTMLString(String word) {
        return "<h1>" + word + "</h1>";
    }
}
