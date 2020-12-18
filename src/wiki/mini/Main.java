package wiki.mini;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import wiki.mini.tags.*;

import java.awt.event.WindowStateListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;


/**
 * Main Class start the JavaFX Application ans stores important global variables
 * @author Maxmilian Burger
 * @version v.1
 */
public class Main extends Application {

    /**
     * Starting the actual application
     * @param args Args
     */
    public static void main(String[] args) {
        Application.launch(args);
    }

    TextArea textArea;
    WebView webView;
    Button convertButton;
    /**
     * The html that is being written into the file
     */
    String finalHTML;
    /**
     * The html code that the user has input
     */
    String[] html;
    /**
     * The file the user is currently using
     */
    File currentFile;

    /**
     * Default HTML construct before the code
     */
    String[] defaultHTMLBeforeBody = new String[]{
            "<!DOCTYPE html>",
            "<html lang=de>",
            "<head>",
            "<title>MiniWiki</title>",
            "<meta charset=\"utf-8\">",
            "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">",
            "</head>",
            "<body>"
    };

    /**
     * Default HMTL construct after the code
     */
    String[] defaultHTMLAfterBody = new String[]{
            "</body>",
            "</html>"
    };

    /**
     * All Styles that should be checked
     */
    Style[] allowedStyles = new Style[]{new H6(), new H5(), new H4(), new H3(), new H2(), new H1(), new Italic(),
            new Bold(), new Br(), new Code(), new Hr(), new Sub(), new Sup(), new Link(), new List()};


    /**
     * JavaFX Application start method
     * @param stage Stage
     */
    @Override
    public void start(Stage stage) {
        stage.setTitle("Mini Wiki");

        Canvas canvas = new Canvas(50, 600);
        BorderPane main = new BorderPane(canvas);

        // Create MenuBar
        MenuBar menuBar = new MenuBar();

        // Create menus
        Menu fileMenu = new Menu("File");
        Menu editMenu = new Menu("Edit");
        Menu helpMenu = new Menu("Help");

        // Create MenuItems
        MenuItem newItem = new MenuItem("Save File As");
        MenuItem saveItem = new MenuItem("Save");
        MenuItem openFileItem = new MenuItem("Open File");
        MenuItem extractHTML = new MenuItem("Extract HTML");
        MenuItem exitItem = new MenuItem("Exit");

        //Keybinds
        newItem.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+S"));
        saveItem.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        exitItem.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));
        extractHTML.setAccelerator(KeyCombination.keyCombination(("Ctrl+H")));

        //Events
        //Exit program
        exitItem.setOnAction(event -> System.exit(0));

        //Extract HTML
        extractHTML.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("HTML file", "*.html"),
                    new FileChooser.ExtensionFilter("HTML file", "*.htm")
            );
            setHTML();
            createFile(stage, fileChooser, finalHTML);
        });

        //Open File
        openFileItem.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(stage);
            StringBuilder finalString = new StringBuilder();
            if (file != null) {
                try {
                    BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(file.getAbsolutePath()));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        finalString.append(line).append("\n");
                    }
                    textArea.setText(finalString.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        //SaveFileAs
        newItem.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Text Files", "*.txt")
            );
            createFile(stage, fileChooser, textArea.getText());
        });

        //SaveAs
        saveItem.setOnAction(actionEvent -> {
            if (currentFile != null) {
                try {
                    FileWriter fileWriter = new FileWriter(currentFile, false);
                    setHTML();
                    fileWriter.write(textArea.getText());
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Text Files", "*.txt")
                );
                createFile(stage, fileChooser, textArea.getText());
            }
        });


        // Add menuItems to the Menus
        fileMenu.getItems().addAll(saveItem, newItem, openFileItem, exitItem, extractHTML);

        // Add Menus to the MenuBar
        menuBar.getMenus().addAll(fileMenu, editMenu, helpMenu);

        main.setTop(menuBar);


        textArea = new TextArea();
        webView = new WebView();
        convertButton = new Button("Compile to HTML");

        main.setLeft(textArea);
        main.setRight(webView);
        main.setBottom(convertButton);

        convertButton.setOnMouseClicked(onCompile);

        stage.setResizable(false);
        stage.setScene(new Scene(main));
        stage.show();
    }

    /**
     * Method used in the events
     * @param stage Stage
     * @param fileChooser Filechosser
     * @param text Content
     */
    private void createFile(Stage stage, FileChooser fileChooser, String text) {
        File file = fileChooser.showSaveDialog(stage);
        try {
            FileWriter fileWriter = new FileWriter(file.getAbsolutePath());
            fileWriter.write(text);
            fileWriter.close();

            if (file.getName().split("\\.")[1].equalsIgnoreCase("txt")) {
                currentFile = file;
            }

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("IO Exception");
            alert.setContentText("There was an error during the file creation process! Try again!");
            alert.showAndWait();
        }

    }

    EventHandler<MouseEvent> onCompile = actionEvent -> setHTML();

    /**
     * Set HTML Method Converts the markdown language into HTML Code
     */
    private void setHTML() {
        boolean inList = false;
        String text = textArea.getText();
        String[] lines = text.split("\n");
        StringBuilder htmlLine = new StringBuilder();
        html = new String[Integer.MAX_VALUE / 100000];
        int htmlIndex = 0;
        for (String line : lines) { //Every Line
            htmlLine = new StringBuilder();
            for (Style allowedStyle : allowedStyles) {    //Every Style
                Matcher matcher = allowedStyle.getPattern().matcher(line);
                while (matcher.find()) {    //Every Match
                    if (allowedStyle instanceof List && !inList) {
                        htmlLine.append("<ul>");
                        inList = true;
                    }
                    if (!(allowedStyle instanceof List) && inList) {
                        htmlLine.append(BasicFunctionLibrary.multiplyString("</li></ul>", List.currentIndention));
                        inList = false;
                        for (Style style : allowedStyles) {
                            style.resetVariables();
                        }
                    }
                    line = line.replaceFirst(allowedStyle.getRegex(),
                            allowedStyle.toHTMLString(matcher.group(1)));
                }
            }
            htmlLine.append(line);
            html[htmlIndex] = htmlLine.toString();
            htmlIndex++;
        }
        //Closing the list when the markdown file ends IF IN LIST
        if (inList) {
            html[htmlIndex] = BasicFunctionLibrary.multiplyString("</li></ul>", List.currentIndention);
            for (Style style : allowedStyles) {
                style.resetVariables();
            }
        }

        finalHTML = BasicFunctionLibrary.ArrayToString(defaultHTMLBeforeBody)
                + BasicFunctionLibrary.ArrayToString(html)
                + BasicFunctionLibrary.ArrayToString(defaultHTMLAfterBody);
        webView.getEngine().loadContent(finalHTML);
    }

}
