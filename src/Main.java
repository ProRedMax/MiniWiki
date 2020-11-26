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
import wiki.mini.tags.Style;
import wiki.mini.tags.h1;



public class Main extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }

    TextArea textArea;
    WebView webView;
    Button convertButton;

    Style[] allowedStyles = new Style[]{new h1()};


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
        for (String line : lines) {                     //Every Line
            for (String word : line.split("\\s+")) {    //Every word in the line
                for (int i = 0; i < allowedStyles.length; i++) {    //Every Style
                    while (allowedStyles[i].getPattern().matcher(word).matches()) { //For every possible

                    }
                }
            }
        }
    };


}
