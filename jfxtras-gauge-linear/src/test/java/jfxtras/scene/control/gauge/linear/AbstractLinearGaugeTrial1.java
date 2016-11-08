/**
 * AbstractLinearGaugeTrial1.java
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

package jfxtras.scene.control.gauge.linear;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import jfxtras.test.TestUtil;
import jfxtras.scene.control.gauge.linear.AbstractLinearGauge;
import jfxtras.scene.control.gauge.linear.elements.PercentMarker;
import jfxtras.scene.control.gauge.linear.elements.PercentSegment;
import jfxtras.scene.control.gauge.linear.elements.Segment;

/**
 * @author Tom Eugelink
 */
abstract public class AbstractLinearGaugeTrial1 extends Application {
	
	public abstract AbstractLinearGauge<?> createLinearGauge();
	public abstract void addDeviatingGauges(List<AbstractLinearGauge<?>> gauges, FlowPane lFlowPane);
	
	@Override
	public void start(Stage stage) {

		List<AbstractLinearGauge<?>> gauges = new ArrayList<>();
		
		FlowPane lFlowPane = new FlowPane(10, 10);
		
        // naked
		{
			final AbstractLinearGauge<?> lLinearGauge = createLinearGauge();
			lLinearGauge.setStyle("-fx-border-color: #000000;");
			lFlowPane.getChildren().add(lLinearGauge);
		}
        
        // without segments, static value
		{
			final AbstractLinearGauge<?> lLinearGauge = createLinearGauge();
			lLinearGauge.withValue(50.0);
			lLinearGauge.setStyle("-fx-border-color: #000000;");
			lFlowPane.getChildren().add(lLinearGauge);
		}
        
        // without segments, static value
		{
			final AbstractLinearGauge<?> lLinearGauge = createLinearGauge();
			lLinearGauge.withValue(100.0);
			lLinearGauge.setStyle("-fx-border-color: #000000;");
			lFlowPane.getChildren().add(lLinearGauge);
		}
        
        // without segments
		{
			final AbstractLinearGauge<?> lLinearGauge = createLinearGauge();
			lLinearGauge.setStyle("-fx-border-color: #000000;");
			lFlowPane.getChildren().add(lLinearGauge);
			gauges.add(lLinearGauge);
		}
        
        // broken
		{
			final AbstractLinearGauge<?> lLinearGauge = createLinearGauge();
			lLinearGauge.withValue(-10.0);
			lLinearGauge.setStyle("-fx-border-color: #000000;");
			lFlowPane.getChildren().add(lLinearGauge);			
		}

		// 10 segments
		{
			final AbstractLinearGauge<?> lLinearGauge = createLinearGauge();
			lLinearGauge.setStyle("-fx-border-color: #000000;");
			for (int i = 0; i < 10; i++) {
				Segment lSegment = new PercentSegment(lLinearGauge, i * 10.0, (i+1) * 10.0);
				lLinearGauge.segments().add(lSegment);
			}
			lFlowPane.getChildren().add(lLinearGauge);
			gauges.add(lLinearGauge);
		}
        
        // not animated
		{
			final AbstractLinearGauge<?> lLinearGauge = createLinearGauge();
			lLinearGauge.setStyle("-fx-border-color: #000000; -fxx-animated:NO;");
			lFlowPane.getChildren().add(lLinearGauge);
			gauges.add(lLinearGauge);
		}
        
        // shrunk
		{
			final AbstractLinearGauge<?> lLinearGauge = createLinearGauge();
			lLinearGauge.setValue(50.0);
			lLinearGauge.setStyle("-fx-border-color: #000000;");
			lLinearGauge.setPrefSize(150.0, 150.0);
			lFlowPane.getChildren().add(lLinearGauge);
			gauges.add(lLinearGauge);
		}
        
        // larger
		{
			final AbstractLinearGauge<?> lLinearGauge = createLinearGauge();
			lLinearGauge.setValue(50.0);
			lLinearGauge.setStyle("-fx-border-color: #000000;");
			lLinearGauge.setPrefSize(300.0, 300.0);
			lFlowPane.getChildren().add(lLinearGauge);
			gauges.add(lLinearGauge);
		}
        
        // large range with format
		{
			final AbstractLinearGauge<?> lLinearGauge = createLinearGauge();
			lLinearGauge.setMinValue(-10.0);
			lLinearGauge.setMaxValue(1000.0);
			lLinearGauge.setValue(100.0);
			lLinearGauge.setStyle("-fx-border-color:#000000; -fxx-value-format:' ##0.0W';");
			lFlowPane.getChildren().add(lLinearGauge);
			gauges.add(lLinearGauge);
		}
        
        // negative large range
		{
			final AbstractLinearGauge<?> lLinearGauge = createLinearGauge();
			lLinearGauge.setMinValue(-1000000.0);
			lLinearGauge.setMaxValue(100.0);
			lLinearGauge.setValue(-1000.0);
			lLinearGauge.setStyle("-fx-border-color: #000000;");
			lFlowPane.getChildren().add(lLinearGauge);
			gauges.add(lLinearGauge);
		}
        
        // 10 segments, with remove
		{
			HBox lHBox = new HBox();
			final AbstractLinearGauge<?> lLinearGauge = createLinearGauge();
			lLinearGauge.setStyle("-fx-border-color: #000000;");
			for (int i = 0; i < 10; i++) {
				Segment lSegment = new PercentSegment(lLinearGauge, i * 10.0, (i+1) * 10.0);
				lLinearGauge.segments().add(lSegment);
			}
			lHBox.getChildren().add(lLinearGauge);
			
			Button lButton = new Button("X");
			lButton.setOnAction( (event) -> {
				if (lLinearGauge.segments().size() > 0) {
					lLinearGauge.segments().remove(lLinearGauge.segments().size() - 1);
					System.out.println("removed segment, remaining " + lLinearGauge.segments().size());
				}
			});
			lHBox.getChildren().add(lButton);

			lFlowPane.getChildren().add(lHBox);
			
			gauges.add(lLinearGauge);
		}

        // markers
		{
			final AbstractLinearGauge<?> lLinearGauge = createLinearGauge();
			lLinearGauge.setStyle("-fx-border-color: #000000;");
			for (int i = 0; i <= 20; i++) {
				lLinearGauge.markers().add(new PercentMarker(lLinearGauge, i * 5.0));
			}
			lFlowPane.getChildren().add(lLinearGauge);
			gauges.add(lLinearGauge);
		}
        
		addDeviatingGauges(gauges, lFlowPane);

        // create scene
        Scene scene = new Scene(lFlowPane, 1500, 900);
        scene.getStylesheets().add(this.getClass().getResource(AbstractLinearGaugeTrial1.class.getSimpleName()+ ".css").toExternalForm());
        scene.getStylesheets().add(AbstractLinearGauge.segmentColorschemeCSSPath());

        // create stage
        stage.setTitle(this.getClass().getSimpleName());
        stage.setScene(scene);
        stage.show();
        
        // start periodically changing the value of the gauges
		Thread t = new Thread( () -> {
			TestUtil.sleep(2000);
			Random lRandom = new Random();
			while (true) {
				TestUtil.sleep(2000);
				Platform.runLater( () -> {
					double d = lRandom.nextDouble();
					for (AbstractLinearGauge<?> g : gauges) {
				 		double minValue = g.getMinValue();
				 		double maxValue = g.getMaxValue();
						g.setValue(minValue + (d * (maxValue - minValue)));
					}
				});
				
			}
		});
		t.setDaemon(true);
		t.start();
    }
}

