package jfxtras.resources;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class JFXtrasFontRobotoTrial  extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        JFXtrasFontRoboto.loadAll();
        for (String s : javafx.scene.text.Font.getFamilies()) {
            if (s.contains("Roboto")) System.out.println(s + " -> " + javafx.scene.text.Font.getFontNames(s));
        }

        FlowPane lFlowPane = new FlowPane();

        Text lText = new Text("bla");
        lText.setStyle("-fx-font-family: '" + JFXtrasFontRoboto.AvailableFonts.RobotoMedium.getFamilyName() + "';");
        lFlowPane.getChildren().add(lText);

        // create scene
        Scene scene = new Scene(lFlowPane, 800, 800);

        // load custom CSS
        //scene.getStylesheets().addAll(this.getClass().getResource(this.getClass().getSimpleName() + ".css").toExternalForm());

        // create stage
        stage.setTitle(this.getClass().getSimpleName());
        stage.setScene(scene);
        stage.show();
    }
}
