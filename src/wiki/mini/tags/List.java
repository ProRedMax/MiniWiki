package wiki.mini.tags;

import wiki.mini.BasicFunctionLibrary;

import java.util.regex.Pattern;

public class List extends Style {

    static boolean inList = true;

    private final String REGEX = "(-+[^-]+)";

    @Override
    public Pattern getPattern() {
        return Pattern.compile(REGEX);
    }

    @Override
    public String toHTMLString(String word) {
        String returnString = "";
        String content = word.substring(BasicFunctionLibrary.countSameCharInSequence(word, '-'));
        if (inList) {
            returnString += BasicFunctionLibrary.multiplyString("<ul><li>",
                    BasicFunctionLibrary.countSameCharInSequence(word, '-'));
            returnString += content;
            returnString += BasicFunctionLibrary.multiplyString("</ul></li>",
                    BasicFunctionLibrary.countSameCharInSequence(word, '-'));
        }

        return returnString;
    }

    @Override
    public String getRegex() {
        return REGEX;
    }
}
