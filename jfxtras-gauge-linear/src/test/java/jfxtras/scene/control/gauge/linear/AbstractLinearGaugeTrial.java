/**
 * AbstractLinearGaugeTrial.java
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
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import jfxtras.test.TestUtil;
import jfxtras.scene.control.gauge.linear.AbstractLinearGauge;
import jfxtras.scene.control.gauge.linear.BasicRoundDailGauge;
import jfxtras.scene.control.gauge.linear.SimpleMetroArcGauge;
import jfxtras.scene.control.gauge.linear.elements.Indicator;
import jfxtras.scene.control.gauge.linear.elements.PercentSegment;
import jfxtras.scene.control.gauge.linear.elements.Segment;

/**
 * @author Tom Eugelink
 */
public class AbstractLinearGaugeTrial extends Application {
	
    public static void main(String[] args) {
        launch(args);       
    }

	@Override
	public void start(Stage stage) {

		List<AbstractLinearGauge<?>> gauges = new ArrayList<>();
		
		// the border pane makes it resizeable
		FlowPane lFlowPane = new FlowPane(10.0, 10.0);
		lFlowPane.setStyle("-fx-background-color: #eeeeee;");
		
		// 10 segments, color schema
		{
			final SimpleMetroArcGauge lLinearGauge = new SimpleMetroArcGauge();
			lLinearGauge.setStyle("-fx-border-color: #000000;");
			lLinearGauge.getStyleClass().add("colorscheme-green-to-red-10");
			for (int i = 0; i < 10; i++) {
				Segment lSegment = new PercentSegment(lLinearGauge, i * 10.0, (i+1) * 10.0);
				lLinearGauge.segments().add(lSegment);
			}
			lFlowPane.getChildren().add(lLinearGauge);
			gauges.add(lLinearGauge);
		}
		
		// 10 segments, color schema
		{
			final SimpleMetroArcGauge lLinearGauge = new SimpleMetroArcGauge();
			lLinearGauge.setStyle("-fx-border-color: #000000;");
			lLinearGauge.getStyleClass().add("colorscheme-purple-to-cyan-10");
			for (int i = 0; i < 10; i++) {
				Segment lSegment = new PercentSegment(lLinearGauge, i * 10.0, (i+1) * 10.0);
				lLinearGauge.segments().add(lSegment);
			}
			lFlowPane.getChildren().add(lLinearGauge);
			gauges.add(lLinearGauge);
		}
		
        // BasicArcGauge
		{
			final BasicRoundDailGauge lBasicArcGauge = new BasicRoundDailGauge();
			lBasicArcGauge.getStyleClass().add("colorscheme-light");
			for (int i = 0; i < 10; i++) {
				Segment lSegment = new PercentSegment(lBasicArcGauge, i * 10.0, (i+1) * 10.0);
				lBasicArcGauge.segments().add(lSegment);
			}
			lBasicArcGauge.indicators().add(new Indicator(0, "warning"));
			lBasicArcGauge.indicators().add(new Indicator(1, "error"));
			lFlowPane.getChildren().add(lBasicArcGauge);
			gauges.add(lBasicArcGauge);
		}
		
        // BasicArcGauge
		{
			final BasicRoundDailGauge lBasicArcGauge = new BasicRoundDailGauge();
			lBasicArcGauge.getStyleClass().add("colorscheme-dark");
			lBasicArcGauge.getStyleClass().add("colorscheme-purple-to-cyan-10");
			for (int i = 0; i < 10; i++) {
				Segment lSegment = new PercentSegment(lBasicArcGauge, i * 10.0, (i+1) * 10.0);
				lBasicArcGauge.segments().add(lSegment);
			}
			lBasicArcGauge.indicators().add(new Indicator(0, "warning"));
			lBasicArcGauge.indicators().add(new Indicator(1, "error"));
			lFlowPane.getChildren().add(lBasicArcGauge);
			gauges.add(lBasicArcGauge);
		}
		
        
        // create scene
        Scene scene = new Scene(lFlowPane, 1300, 900);
        scene.getStylesheets().add(AbstractLinearGauge.segmentColorschemeCSSPath());
        		
        // create stage
        stage.setTitle(this.getClass().getSimpleName());
        stage.setScene(scene);
        stage.show();
        
        // start periodically changing the value of the gauges
		Thread t = new Thread( () -> {
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
	int cnt = 0;
}

