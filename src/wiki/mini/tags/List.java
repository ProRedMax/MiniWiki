package wiki.mini.tags;

import java.util.regex.Pattern;

import static wiki.mini.BasicFunctionLibrary.countSameCharInSequence;
import static wiki.mini.BasicFunctionLibrary.multiplyString;

public class List extends Style {

    public static int currentIndention = 0;

    private final String REGEX = "(-+[^-]+)";

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
            returnString += currentIndention - indention == 0 ? "</li>" : multiplyString("</li>\n</ul>\n</li>", currentIndention - indention);
        } else {
            currentIndention = 1;
        }

        returnString += currentIndention - indention >= 0 ? "<li>" : multiplyString("<ul><li>", Math.abs(currentIndention - indention));

        returnString += content;

        currentIndention = indention;

        return returnString;
    }

    @Override
    public String getRegex() {
        return REGEX;
    }
}
