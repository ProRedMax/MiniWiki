package wiki.mini.version.control;

import javafx.stage.FileChooser;
import wiki.mini.BasicFunctionLibrary;
import wiki.mini.Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author mabug
 */
public class MWVC {

    /**
     * Localy stored versions
     */
    public static HashMap<String, String> versions = new HashMap<>();

    /**
     * @return Last Versions
     */
    public static int getCurrentVersion() {
        readVersions(Main.currentFile.getAbsolutePath());
        return versions.size();
    }

    /**
     * @param file File
     */
    public static void readVersions(String file) {
        try (BufferedReader rd = Files.newBufferedReader(Paths.get(file))) {
            String line;
            outer:
            while ((line = rd.readLine()) != null) {
                try {
                    String key = "";
                    Pattern pattern = Pattern.compile(":.+#:");
                    Matcher matcher = pattern.matcher(line.trim());
                    if (matcher.find()) {
                        key = matcher.group(0);
                    }
                    versions.put(key, line.trim().split(":.+#:")[1]);
                } catch (ArrayIndexOutOfBoundsException e) {
                    continue outer;
                }
            }
        } catch (IOException e) {
            BasicFunctionLibrary.createRequest("Error", "File IO",
                    "There was an error during the process of reading the versions!");
        }
    }

    /**
     * @param name Commit Comment cllaed from the lambda
     */
    public static void createVersion(String name) {
        if (Main.currentFile == null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser
                    .ExtensionFilter("Mini Wiki File", "*.mw"));
            File file = fileChooser.showSaveDialog(Main.stage);
            try {
                file.createNewFile();
                Main.currentFile = file;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        readVersions(Main.currentFile.getAbsolutePath());
        String toWrite = diffVersions(name);
        try {
            FileWriter fileWriter = new FileWriter(Main.currentFile, true);
            fileWriter.write(toWrite);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return The full versiohn stored
     */
    public static String createOldLastVersion() {
        String[] content = new String[Main.MAX_FILE_LINE_SIZE];
        for (String line : MWVC.versions.values()) {
            for (String change : line.split(";")) {
                content[Integer.parseInt(change.split(",")[0])] = change.split(",")[1];
            }
        }
        return BasicFunctionLibrary.arrayToString(content);
    }

    /**
     * @param name Name of the commit
     * @return Difference String ready to be stored into the
     */
    public static String diffVersions(String name) {
        String[] content = Main.textArea.getText().split("\n");
        String[] oldContent = createOldLastVersion().split("\n");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        StringBuilder toWrite = new StringBuilder("\n:" + (getCurrentVersion() + 1) + "|"
                + name + "#" + dtf.format(now) + "#:");
        for (int i = 0; i < Math.max(content.length, oldContent.length); i++) {
            try {
                if (!(content[i].equals(oldContent[i].replace("\r", "")))) {
                    toWrite.append(i).append(",\"").append(content[i]).append("\"");
                    toWrite.append(";");
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                if (content.length >= oldContent.length) {
                    toWrite.append(i).append(",\"").append(content[i]).append("\"");
                } else {
                    toWrite.append(i).append(",\"").append("\"");
                }
                toWrite.append(";");
            }
        }
        return toWrite.toString();
    }

}
