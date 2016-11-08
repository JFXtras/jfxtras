/**
 * CircularPaneTrail.java
 *
 * Copyright (c) 2011-2016, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the organization nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package jfxtras.scene.layout.trial;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import jfxtras.test.TestUtil;
import jfxtras.scene.layout.CircularPane;
import jfxtras.scene.layout.HBox;

public class CircularPaneTrail extends Application {

    public static void main(String[] args) {
        launch(args);       
    }

	@Override
	public void start(Stage stage) {
		
		HBox lHBox = new HBox(20);
		Paint lShowDebug = Color.GREEN;
		
		{
			CircularPane lCircularPane = new CircularPane();
			//lCircularPane.setStyle("-fx-border-color:black;");
			lCircularPane.setStartAngle(360.0 / 12 / 2); // make sure the 12 is on top
			lCircularPane.setChildrenAreCircular(true);
			//lCircularPane.setShowDebug(lShowDebug);
			lCircularPane.setAnimationInterpolation(CircularPane::animateOverTheArc);
			final List<Label> labels = new ArrayList<>();
			final List<Circle> circles = new ArrayList<>();
			final AtomicReference<Circle> lastFocus = new AtomicReference<>();
			final AtomicBoolean isPM = new AtomicBoolean(false);
			for (int i = 0; i < 12; i++) {
				javafx.scene.shape.Circle c = new javafx.scene.shape.Circle(10, Color.TRANSPARENT);
				c.getStyleClass().add("timecircle");
				circles.add(c);
				javafx.scene.control.Label t = new javafx.scene.control.Label("" + (i + 1));
				t.getStyleClass().add("time");
				labels.add(t);
				c.setOnMouseEntered( (event) -> {
					int lastFocusIdx = circles.indexOf(lastFocus.get());
					int currentIdx = circles.indexOf( event.getSource() );
					
					if ( (lastFocusIdx == 11 && currentIdx == 0)
					  || (lastFocusIdx == 0 && currentIdx == 11)
					   ) {
						isPM.set( !isPM.get() );
					}
					
					for (int j = 0; j < 12; j++) {
						labels.get(j).setText( "" + ( (j + 1) + (isPM.get() ? 12 : 0) ) );
					}
				});
				c.setOnMouseExited( (event) -> {
					lastFocus.set( (Circle)event.getSource() );
				});
				StackPane lStackPane = new StackPane();
				lStackPane.setId("" + i);
				lStackPane.getChildren().add(c);
				lStackPane.getChildren().add(t);
				lCircularPane.add(lStackPane);
			}
			
			StackPane lStackPane = new StackPane();
			Circle c = new Circle(1, Color.WHITE);
			c.radiusProperty().bind(lCircularPane.widthProperty().divide(2.0));
			lStackPane.getChildren().add(c);
			lStackPane.getChildren().add(lCircularPane);
			Label l = new Label("H");
			l.getStyleClass().add("center");
			lStackPane.getChildren().add(l);
			lHBox.add(lStackPane);
		}
		
		{
			CircularPane lCircularPane = new CircularPane();
			lCircularPane.setStartAngle(-360.0 / 12 / 2);
			lCircularPane.setDiameter(150.0);
			//lCircularPane.setStyle("-fx-border-color:black;");
			lCircularPane.setChildrenAreCircular(true);
			//lCircularPane.setShowDebug(lShowDebug);
			lCircularPane.setAnimationInterpolation(CircularPane::animateOverTheArc);
			final List<Label> labels = new ArrayList<>();
			final List<Circle> circles = new ArrayList<>();
			for (int i = 0; i < 12; i++) {
				javafx.scene.shape.Circle c = new javafx.scene.shape.Circle(10, Color.TRANSPARENT);
				circles.add(c);
				javafx.scene.control.Label t = new javafx.scene.control.Label("" + (i * 5));
				t.getStyleClass().add("time");
				labels.add(t);
				StackPane lStackPane = new StackPane();
				lStackPane.getChildren().add(c);
				lStackPane.getChildren().add(t);
				lCircularPane.add(lStackPane);
			}
			StackPane lStackPane = new StackPane();
			Circle c = new Circle(1, Color.WHITE);
			c.radiusProperty().bind(lCircularPane.widthProperty().divide(2.0));
			lStackPane.getChildren().add(c);
			lStackPane.getChildren().add(lCircularPane);
			Label l = new Label("M");
			l.getStyleClass().add("center");
			lStackPane.getChildren().add(l);
			lHBox.add(lStackPane);
		}
		
		{
			CircularPane lCircularPane = new CircularPane();
			lCircularPane.setDiameter(90.0);
			//lCircularPane.setStyle("-fx-border-color:black;");
			//lCircularPane.setChildrenAreCircular(true);
			lCircularPane.setShowDebug(lShowDebug);
			lCircularPane.setAnimationInterpolation(CircularPane::animateOverTheArc);
			final List<Label> labels = new ArrayList<>();
			final List<Circle> circles = new ArrayList<>();
			final AtomicReference<Circle> lastFocus = new AtomicReference<>();
			final AtomicBoolean isPM = new AtomicBoolean(false);
			for (int i = 0; i < 12; i++) {
				javafx.scene.shape.Circle c = new javafx.scene.shape.Circle(10, Color.GRAY);
				circles.add(c);
				javafx.scene.control.Label t = new javafx.scene.control.Label("" + (i * 5));
				t.getStyleClass().add("time");
				labels.add(t);
				StackPane lStackPane = new StackPane();
				lStackPane.getChildren().add(c);
				lStackPane.getChildren().add(t);
				lCircularPane.add(lStackPane);
			}
			StackPane lStackPane = new StackPane();
			lStackPane.getChildren().add(lCircularPane);
			Label l = new Label("S");
			l.getStyleClass().add("center");
			lStackPane.getChildren().add(l);
			lHBox.add(lStackPane);
		}
		
		{
			CircularPane lCircularPane = new CircularPane().withId("XX");
			//lCircularPane.setStyle("-fx-border-color:black;");
			lCircularPane.setShowDebug(lShowDebug);
			lCircularPane.setAnimationInterpolation(CircularPane::animateOverTheArc);
			for (int i = 0; i < 12; i++) {
				javafx.scene.control.Button b = new javafx.scene.control.Button("XX");
//				b.setStyle("-fx-padding:10px;");
				b.setStyle("-fx-margin:10px;");
				lCircularPane.add(b);
			}
			lHBox.add(lCircularPane);
		}
		
		{
			CircularPane lCircularPane = new CircularPane();
			lCircularPane.setMinSize(200,  200);
			//lCircularPane.setStyle("-fx-border-color:black;");
			//lCircularPane.setChildrenAreCircular(true);
			lCircularPane.setShowDebug(lShowDebug);
			lCircularPane.setAnimationInterpolation(CircularPane::animateOverTheArc);
			for (int i = 0; i < 8; i++) {
				javafx.scene.shape.Circle c = new javafx.scene.shape.Circle(10);
				lCircularPane.add(c);
			}
			lHBox.add(lCircularPane);
			
//			final Node n = lCircularPane.getChildren().get(0);				
//			n.layoutXProperty().addListener( (observable) -> {
//				System.out.println("layoutX=" + n.getLayoutX());
//			});		
//			n.layoutYProperty().addListener( (observable) -> {
//				System.out.println("layoutY=" + n.getLayoutY());
//			});		
			
		}
		
		{
			CircularPane lCircularPane = new CircularPane();
			//lCircularPane.setStyle("-fx-border-color:black;");
			//lCircularPane.setChildrenAreCircular(true);
			lCircularPane.setShowDebug(lShowDebug);
			lCircularPane.setAnimationInterpolation(CircularPane::animateOverTheArc);
			for (int i = 0; i < 8; i++) {
				javafx.scene.shape.Circle c = new javafx.scene.shape.Circle(10);
				lCircularPane.add(c);
			}
			lHBox.add(lCircularPane, new HBox.C().hgrow(Priority.ALWAYS));
		}
		
		{
			CircularPane lCircularPane = new CircularPane();
			//lCircularPane.setStyle("-fx-border-color:black;");
			//lCircularPane.setChildrenAreCircular(true);
			lCircularPane.setShowDebug(lShowDebug);
			lCircularPane.setAnimationInterpolation(CircularPane::animateOverTheArc);
			for (int i = 0; i < 8; i++) {
				javafx.scene.shape.Circle c = new javafx.scene.shape.Circle(5 + i);
				lCircularPane.add(c);
			}
			lHBox.add(lCircularPane);
		}

		{
			CircularPane lCircularPane = new CircularPane();
			//lCircularPane.setStyle("-fx-border-color:black;");
			lCircularPane.setShowDebug(lShowDebug);
			lCircularPane.setAnimationInterpolation(CircularPane::animateOverTheArc);
			for (int i = 0; i < 8; i++) {
				javafx.scene.shape.Rectangle c = new javafx.scene.shape.Rectangle(5 + (2*i), 5 + (2*i));
				lCircularPane.add(c);
			}
			lHBox.add(lCircularPane);
		}
		
		{
			CircularPane lCircularPane = new CircularPane();
			//lCircularPane.setStyle("-fx-border-color:black;");
			lCircularPane.setShowDebug(lShowDebug);
			lCircularPane.setAnimationInterpolation(CircularPane::animateFromTheOrigin);
			for (int i = 0; i < 8; i++) {
				javafx.scene.shape.Rectangle c = new javafx.scene.shape.Rectangle(5 + (2*i), 5 + (2*i));
				c.setRotate(45);
				lCircularPane.add(c);
			}
			lHBox.add(lCircularPane);
		}
		
		
		CircularPane lCircularPane = new CircularPane();
		{
			//lCircularPane.setStyle("-fx-border-color:black;");
			lCircularPane.setShowDebug(lShowDebug);
			lCircularPane.setAnimationInterpolation(CircularPane::animateFromTheOrigin);
//			lCircularPane.setAnimationInterpolation(CircularPane::animateOverTheArc);
			for (int i = 0; i < 10; i++) {
				javafx.scene.shape.Rectangle c = new javafx.scene.shape.Rectangle(20, 20);
				c.setRotate(i * 10);
				lCircularPane.add(c);
			}
			lHBox.add(lCircularPane);
		}
		
		Button lButton = new Button("out");
		lButton.setOnAction( (actionEvent) -> {
			lCircularPane.animateOut();
		});
		lCircularPane.setOnAnimateOutFinished( (event) -> {
			lCircularPane.setVisible(false);
			Platform.runLater(() -> {
				TestUtil.sleep(3000);
				lCircularPane.setVisible(true);
				lCircularPane.animateIn();
			});
		});
		lHBox.getChildren().add(lButton);
		
        // setup scene
		Scene scene = new Scene(lHBox);
		scene.getStylesheets().add(this.getClass().getName().replace(".", "/") + ".css");
		
        // create stage
        stage.setTitle(this.getClass().getSimpleName());
        stage.setScene(scene);
        stage.show();	
	}
	
}

	