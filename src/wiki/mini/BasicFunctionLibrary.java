package wiki.mini;

import javafx.scene.control.Alert;

import java.util.HashMap;

/**
 * Function Library which contains useful methods
 *
 * @author Maxmilian Burger
 */
public class BasicFunctionLibrary {

    /**
     * Converts an array
     *
     * @param array Array
     * @return String
     */
    public static String ArrayToString(String[] array) {
        StringBuffer sb = new StringBuffer();
        for (String s : array) {
            if (s != null) {
                sb.append(s.replace("\"", "")).append(System.lineSeparator());
            } else {
                sb.append("\0");
            }
        }
        return sb.toString().trim().replace("\0", "\n");
    }

    /**
     * Multiplies the given string
     *
     * @param str   String
     * @param times Times
     * @return String
     */
    public static String multiplyString(String str, int times) {
        if (times <= 0) {
            return "";
        }
        return String.valueOf(str).repeat(Math.max(0, times));
    }

    /**
     * how often a string is in another string (currently not used)
     *
     * @param base     Base string
     * @param toSearch toSearch
     * @param until    look for string until index
     * @return N
     */
    public static int countString(String base, String toSearch, int until) {
        int count = 0, fromIndex = 0;

        while ((fromIndex = base.indexOf(toSearch, fromIndex)) != -1 || fromIndex == until) {
            count++;
            fromIndex++;
        }
        return count;
    }

    /**
     * @param base     String
     * @param toSearch Character to search
     * @return N
     */
    public static int countSameCharInSequence(String base, char toSearch) {
        int count = 0;
        for (char c : base.toCharArray()) {
            if (count != 0 && c != toSearch) {
                break;
            }
            if (c == toSearch) {
                count++;
            }
        }
        return count;
    }

    public static void createRequest(String title, String headText, String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(headText);
        alert.setContentText(text);
        alert.showAndWait();
    }

    public static String versionsToString(HashMap<Integer, String> map) {
        StringBuilder returnString = new StringBuilder();
        for (Integer i : map.keySet()) {
            returnString.append(":").append(i.toString()).append(":").append(map.get(i));
        }
        return returnString.toString();
    }

}