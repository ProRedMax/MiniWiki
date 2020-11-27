package wiki.mini.tags;

import java.util.regex.Pattern;

public abstract class Style {

    public abstract Pattern getPattern();

    public abstract String toHTMLString(String word);

    public abstract String getRegex();

}
