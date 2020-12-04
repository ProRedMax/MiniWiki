package wiki.mini.tags;

import java.util.regex.Pattern;

public abstract class Style {

    public void resetVariables() {
    }

    public abstract Pattern getPattern();

    public abstract String toHTMLString(String word);

    public abstract String getRegex();

}
