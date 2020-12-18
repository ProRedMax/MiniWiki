package wiki.mini.tags;

import java.util.regex.Pattern;

/**
 * Abstract style class for all the tags
 */
public abstract class Style {

    /**
     * Method in order to reset any custom variables used by any tag class
     */
    public void resetVariables() {
    }

    /**
     * @return Get the regex pattern
     */
    public abstract Pattern getPattern();

    /**
     * @param word Content
     * @return html string
     */
    public abstract String toHTMLString(String word);

    /**
     * @return Regex in String format
     */
    public abstract String getRegex();

}
