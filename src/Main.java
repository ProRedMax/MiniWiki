
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import wiki.mini.tags.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;


public class Main extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }

    TextArea textArea;
    WebView webView;
    Button convertButton;

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

    String[] defaultHTMLAfterBody = new String[]{
            "</body>",
            "</html>"
    };

    Style[] allowedStyles = new Style[]{new H1(), new H2(), new H3(), new H4(), new H5(), new H6(), new Italic(),
            new Bold(), new Br(), new Code()};


    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Mini Wiki");

        Canvas canvas = new Canvas(50, 600);
        BorderPane main = new BorderPane(canvas);

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

    EventHandler<MouseEvent> onCompile = actionEvent -> {
        String text = textArea.getText();
        String[] lines = text.split("\n");
        String[] html = new String[Integer.MAX_VALUE / 100000];
        int htmlIndex = 0;
        for (String line : lines) {                     //Every Line
            for (Style allowedStyle : allowedStyles) {    //Every Style
                Matcher matcher = allowedStyle.getPattern().matcher(line);
                while (matcher.find()) {
                    line = line.replaceFirst(allowedStyle.getRegex(),
                            allowedStyle.toHTMLString(matcher.group(1)));
                }
            }
            html[htmlIndex] = line;
            htmlIndex++;
        }

        String finalHTML = BasicFunctionLibrary.ArrayToString(defaultHTMLBeforeBody)
                + BasicFunctionLibrary.ArrayToString(html)
                + BasicFunctionLibrary.ArrayToString(defaultHTMLAfterBody);
        webView.getEngine().loadContent(finalHTML);

        try {
            BufferedWriter bw = Files.newBufferedWriter(Paths.get("testFiles/outHTML.html"));
            bw.write(finalHTML);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    };


}
