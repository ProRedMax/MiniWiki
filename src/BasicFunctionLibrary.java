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
}