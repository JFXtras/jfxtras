package jfxtras.scene.layout.trial;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import jfxtras.scene.layout.CircularPane;
import jfxtras.scene.layout.HBox;

public class CircularPaneTrail6 extends Application {

    public static void main(String[] args) {
        launch(args);       
    }

	@Override
	public void start(Stage stage) {

		CircularPane lCircularPane = new CircularPane();
//		lCircularPane.setStyle("-fx-border-color:black;");
		lCircularPane.setShowDebug(Color.GREEN);
//		lCircularPane.setStartAngle(30.0); 
		for (int i = 0; i < 20; i++) {
//			javafx.scene.shape.Rectangle c = new javafx.scene.shape.Rectangle(10,10);
//			lCircularPane.add(c);
			lCircularPane.add(new javafx.scene.control.Button("X X X"));
		}

        // setup scene
		Scene scene = new Scene(lCircularPane);
		scene.getStylesheets().add(this.getClass().getName().replace(".", "/") + ".css");
		
        // create stage
        stage.setTitle(this.getClass().getSimpleName());
        stage.setScene(scene);
        stage.show();	
	}
	
	

}

	