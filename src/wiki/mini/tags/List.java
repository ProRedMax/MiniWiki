package wiki.mini.tags;

import java.util.regex.Pattern;

import static wiki.mini.BasicFunctionLibrary.countSameCharInSequence;
import static wiki.mini.BasicFunctionLibrary.multiplyString;

/**
 * @author mabug
 */
public class List extends Style {

    /**
     * Current Indention
     */
    public static int currentIndention = 0;

    /**
      * Regex
      */
    private static final String REGEX = "^(-+[^-]+)";

    @Override
    public void resetVariables() {
        currentIndention = 0;
    }

    @Override
    public Pattern getPattern() {
        return Pattern.compile(REGEX);
    }

    @Override
    public String toHTMLString(String word) {
        String returnString = "";
        String content = word.substring(countSameCharInSequence(word, '-'));
        int indention = countSameCharInSequence(word, '-');
        if (currentIndention != 0) {
            if (currentIndention - indention == 0) returnString += "</li>";
            else returnString += multiplyString("</li>\n</ul>\n</li>",
                    currentIndention - indention);
        } else {
            currentIndention = 1;
        }

        if (currentIndention - indention >= 0) returnString += "<li>";
        else returnString += multiplyString("<ul><li>",
                Math.abs(currentIndention - indention));

        returnString += content;

        currentIndention = indention;

        return returnString;
    }

    @Override
    public String getRegex() {
        return REGEX;
    }
}
