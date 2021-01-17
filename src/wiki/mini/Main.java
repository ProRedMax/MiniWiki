package wiki.mini;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import wiki.mini.tags.Bold;
import wiki.mini.tags.Br;
import wiki.mini.tags.Code;
import wiki.mini.tags.H1;
import wiki.mini.tags.H2;
import wiki.mini.tags.H3;
import wiki.mini.tags.H4;
import wiki.mini.tags.H5;
import wiki.mini.tags.H6;
import wiki.mini.tags.Hr;
import wiki.mini.tags.Italic;
import wiki.mini.tags.Link;
import wiki.mini.tags.List;
import wiki.mini.tags.Style;
import wiki.mini.tags.Sub;
import wiki.mini.tags.Sup;
import wiki.mini.version.control.MWVC;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.stream.Collectors;


/**
 * Main Class start the JavaFX Application ans stores important global variables
 *
 * @author Maxmilian Burger
 * @version v.1
 */
public class Main extends Application {

    /**
     * Starting the actual application
     *
     * @param args Args
     */
    public static void main(String[] args) {
        Application.launch(args);
    }

    /**
     * Maximum lines in a file. Used for the arrays
     */
    public static final int MAX_FILE_LINE_SIZE = 12345;

    /**
     * Text Area for markdown
     */
    public static TextArea textArea;
    /**
     * HTML View
     */
    static WebView webView;
    /**
     * Convert Button to html
     */
    static Button convertButton;
    /**
     * Commit to vc
     */
    static Button commitButton;

    /**
     * Stage
     */
    public static Stage stage;

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
    public static File currentFile;

    /**
     * Default HTML construct before the code
     */
    String[] defaultHTMLBeforeBody = new String[]{"<!DOCTYPE html>", "<html lang=de>", "<head>",
        "<title>MiniWiki</title>", "<meta charset=\"utf-8\">",
        "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">",
        "</head>", "<body>"
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
    Style[] allowedStyles = new Style[]{new H6(), new H5(), new H4(), new H3(),
        new H2(), new H1(), new Italic(),
        new Bold(), new Br(), new Code(), new Hr(), new Sub(), new Sup(), new Link(), new List()};


    /**
     * JavaFX Application start method
     *
     * @param stage Stage
     */
    @Override
    public void start(Stage stage) {

        Main.stage = stage;

        stage.setTitle("Mini Wiki");

        final int v = 50;
        final int v1 = 600;
        Canvas canvas = new Canvas(v, v1);
        BorderPane main = new BorderPane(canvas);
        BorderPane buttonPane = new BorderPane(canvas);

        // Create MenuBar
        MenuBar menuBar = new MenuBar();

        // Create menus
        Menu fileMenu = new Menu("File");
        Menu editMenu = new Menu("Edit");
        Menu helpMenu = new Menu("Help");

        // Create MenuItems
        MenuItem openProjectItem = new MenuItem("Open Project");
        MenuItem extractHTML = new MenuItem("Extract HTML");
        MenuItem exitItem = new MenuItem("Exit");
        MenuItem compileHTML = new MenuItem("Compile to HTML");
        MenuItem about = new MenuItem("About");
        MenuItem versions = new MenuItem("Select Version");

        //Keybinds
        exitItem.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));
        extractHTML.setAccelerator(KeyCombination.keyCombination(("Ctrl+H")));
        compileHTML.setAccelerator(KeyCombination.keyCombination("Ctrl+Alt+C"));

        //Events
        //Exit program
        exitItem.setOnAction(event -> System.exit(0));

        //Compile from Menubar
        compileHTML.setOnAction(actionEvent -> setHTML());

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

        about.setOnAction(actionEvent -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("About Page");
            alert.setHeaderText("Mini Wiki About Page");
            alert.setContentText("This program is made by Maximilian Burger.");
            alert.showAndWait();
        });

        //Open Project
        openProjectItem.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(stage);
            String[] content = new String[MAX_FILE_LINE_SIZE];
            if (file != null) {
                MWVC.readVersions(file.getAbsolutePath());
                for (String line : MWVC.versions.values()) {
                    for (String change : line.split(";")) {
                        content[Integer.parseInt(change.split(",")[0])] = change.split(",")[1];
                    }
                }
                textArea.setText(BasicFunctionLibrary.arrayToString(content));
                currentFile = file;
                setHTML();
            }
        });

        // Versions
        versions.setOnAction(actionEvent -> {
            if (Main.currentFile == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("There are currently no versions present!"
                       + " Create on by pressing the commit Button!");
                alert.setContentText("Ooops, there was an error!");
                alert.showAndWait();
            } else {
                MWVC.readVersions(Main.currentFile.getAbsolutePath());
                Map<String, String> newVersions = MWVC.versions.entrySet().stream()
                        .sorted(new VersionSortComparator())
                        .collect(Collectors.toMap(Map.Entry::getKey,
                                Map.Entry::getValue,
                                (e1, e2) -> e1, LinkedHashMap::new));
                ArrayList<String> choices = new ArrayList<>(newVersions.keySet());
                ChoiceDialog<String> dialog = new ChoiceDialog<>("Your Version", choices);

                dialog.setTitle("Version Selector");
                dialog.setHeaderText("Choose the version you want to jump to:");
                dialog.setContentText("Choose the version you want to jump to:");
                Optional<String> result = dialog.showAndWait();
                result.ifPresent(option -> {
                    String[] content = new String[MAX_FILE_LINE_SIZE];
                    MWVC.readVersions(Main.currentFile.getAbsolutePath());
                    int currentVersion = 1;
                    for (String line : newVersions.values()) {
                        for (String change : line.split(";")) {
                            content[Integer.parseInt(change.split(",")[0])] = change.split(",")[1];
                        }
                        if (currentVersion == Integer.parseInt(option.split("\\|")[0]
                                .replace(":", ""))) {
                            break;
                        }
                        currentVersion++;
                    }
                    textArea.setText(BasicFunctionLibrary.arrayToString(content));
                    setHTML();
                });
            }
        });


        // Add menuItems to the Menus
        fileMenu.getItems().addAll(openProjectItem, exitItem, extractHTML);
        editMenu.getItems().addAll(compileHTML, versions);
        helpMenu.getItems().addAll(about);

        // Add Menus to the MenuBar
        menuBar.getMenus().addAll(fileMenu, editMenu, helpMenu);

        main.setTop(menuBar);


        textArea = new TextArea();
        webView = new WebView();
        convertButton = new Button("Compile to HTML");
        commitButton = new Button("Commit");

        main.setLeft(textArea);
        main.setRight(webView);
        main.setBottom(buttonPane);
        buttonPane.setCenter(convertButton);
        buttonPane.setRight(commitButton);

        convertButton.setOnMouseClicked(onCompile);
        commitButton.setOnMouseClicked(onCommit);

        stage.setResizable(false);
        stage.setScene(new Scene(main));
        stage.show();
    }

    private static void commit() {
        TextInputDialog dialog = new TextInputDialog("Commit Name");
        dialog.setTitle("New Commit");
        dialog.setHeaderText("The actual name of the commit will also have "
                + "the date stored with it");
        dialog.setContentText("Please enter your commit comment:");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(MWVC::createVersion);
    }

    /**
     * Method used in the events
     *
     * @param stage       Stage
     * @param fileChooser Filechosser
     * @param text        Content
     */
    public void createFile(Stage stage, FileChooser fileChooser, String text) {
        File file = fileChooser.showSaveDialog(stage);
        try {
            FileWriter fileWriter = new FileWriter(file.getAbsolutePath());
            fileWriter.write(text);
            fileWriter.close();

        } catch (Exception e) {
            BasicFunctionLibrary.createRequest("Error", "IO Exception",
                    "There was an error during the file creation process! Try again!");
        }
    }


    /**
     * On Compile
     */
    EventHandler<MouseEvent> onCompile = actionEvent -> setHTML();
    /**
     * On Commit
     */
    EventHandler<MouseEvent> onCommit = mouseEvent -> commit();

    /**
     * Set HTML Method Converts the markdown language into HTML Code
     */
    private void setHTML() {
        boolean inList = false;
        String text = textArea.getText();
        String[] lines = text.split("\n");
        StringBuilder htmlLine = new StringBuilder();
        html = new String[MAX_FILE_LINE_SIZE];
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
                        htmlLine.append(BasicFunctionLibrary
                                .multiplyString("</li></ul>", List.currentIndention));
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
            html[htmlIndex] = BasicFunctionLibrary
                    .multiplyString("</li></ul>", List.currentIndention);
            for (Style style : allowedStyles) {
                style.resetVariables();
            }
        }

        finalHTML = BasicFunctionLibrary.arrayToString(defaultHTMLBeforeBody)
                + BasicFunctionLibrary.arrayToString(html)
                + BasicFunctionLibrary.arrayToString(defaultHTMLAfterBody);
        webView.getEngine().loadContent(finalHTML);
    }

}