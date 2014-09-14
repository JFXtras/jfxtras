package jfxtras.scene.layout.trial;


import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import jfxtras.scene.layout.CircularPane;

public class CircularPaneTrail4 extends Application {

    public static void main(String[] args) {
        launch(args);       
    }

	@Override
	public void start(Stage stage) {

		FlowPane lFlowPane = new FlowPane();
	    lFlowPane.setVgap(20);
	    lFlowPane.setHgap(20);
	    Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
	    lFlowPane.setPrefWrapLength(primaryScreenBounds.getWidth());
	     
		for (int j = 0; j < 360; j += 10)
		{
			CircularPane lCircularPane = new CircularPane();
			lCircularPane.setStyle("-fx-border-color:black;");
			lCircularPane.setShowDebug(Color.GREEN);
			lCircularPane.setStartAngle( (double)j );
			lCircularPane.setArc(90.0);
			for (int i = 0; i < 10; i++) {
				javafx.scene.shape.Rectangle c = new javafx.scene.shape.Rectangle(15,15);
				//c.setStroke(Color.RED);
				lCircularPane.add(c);
			}
			lFlowPane.getChildren().add(lCircularPane);
		}

        // setup scene
		Scene scene = new Scene(lFlowPane);
		scene.getStylesheets().add(this.getClass().getName().replace(".", "/") + ".css");
		
        // create stage
        stage.setTitle(this.getClass().getSimpleName());
        stage.setScene(scene);
        stage.show();	
	}
	
	

}

	