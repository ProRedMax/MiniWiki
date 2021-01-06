package wiki.mini.version.control;

import wiki.mini.BasicFunctionLibrary;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class MWVC {

    public static HashMap<Integer, String> versions = new HashMap<>();

    public static void readVersions(String file) {
        try (BufferedReader rd = Files.newBufferedReader(Paths.get(file))) {
            String line = "";
            int version = 0;
            while ((line = rd.readLine()) != null) {
                versions.put(version, line.trim().split(":\\d+:")[1]);
                version++;
            }
        } catch (IOException e) {
            BasicFunctionLibrary.createRequest("Error", "File IO", "There was an error during the process of reading the versions!");
        }
    }

    public static void createVersion() {

    }

}
