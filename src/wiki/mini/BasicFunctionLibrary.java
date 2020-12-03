package wiki.mini;

public class BasicFunctionLibrary {

    public static String ArrayToString(String[] array) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null) {
                sb.append(array[i]).append(System.lineSeparator());
            }
        }
        return sb.toString();
    }

    public static String multiplyString(String str, int times) {
        return String.valueOf(str).repeat(Math.max(0, times));
    }

    public static int countString(String base, String toSearch, int until) {
        int count = 0, fromIndex = 0;

        while ((fromIndex = base.indexOf(toSearch, fromIndex)) != -1 || fromIndex == until) {
            count++;
            fromIndex++;
        }
        return count;
    }

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

}