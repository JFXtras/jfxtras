/**
 * BasicRoundDailGaugeTrial1.java
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

import java.util.List;

import javafx.scene.layout.FlowPane;
import jfxtras.scene.control.gauge.linear.AbstractLinearGauge;
import jfxtras.scene.control.gauge.linear.BasicRoundDailGauge;
import jfxtras.scene.control.gauge.linear.elements.AbsoluteLabel;
import jfxtras.scene.control.gauge.linear.elements.Indicator;
import jfxtras.scene.control.gauge.linear.elements.Label;
import jfxtras.scene.control.gauge.linear.elements.PercentMarker;

/**
 * @author Tom Eugelink
 */
public class BasicRoundDailGaugeTrial1 extends AbstractLinearGaugeTrial1 {
	
    public static void main(String[] args) {
        launch(args);       
    }

    public AbstractLinearGauge<?> createLinearGauge() {
    	return new BasicRoundDailGauge();
    }
    
	@Override
	public void addDeviatingGauges(List<AbstractLinearGauge<?>> gauges, FlowPane lFlowPane) {
        
        // dark
		{
			final AbstractLinearGauge<?> lLinearGauge = createLinearGauge();
			lLinearGauge.setStyle("-fx-border-color: #000000;");
			lLinearGauge.getStyleClass().add("colorscheme-dark");
			for (int i = 0; i <= 5; i++) {
				lLinearGauge.markers().add(new PercentMarker(lLinearGauge, i * 20.0));
			}
			lFlowPane.getChildren().add(lLinearGauge);
			gauges.add(lLinearGauge);
		}
        
        // green
		{
			final AbstractLinearGauge<?> lLinearGauge = createLinearGauge();
			lLinearGauge.setStyle("-fx-border-color: #000000;");
			lLinearGauge.getStyleClass().add("colorscheme-green");
			for (int i = 0; i <= 5; i++) {
				lLinearGauge.markers().add(new PercentMarker(lLinearGauge, i * 20.0));
			}
			lFlowPane.getChildren().add(lLinearGauge);
			gauges.add(lLinearGauge);
		}
        
        // red
		{
			final AbstractLinearGauge<?> lLinearGauge = createLinearGauge();
			lLinearGauge.setStyle("-fx-border-color: #000000;");
			lLinearGauge.getStyleClass().add("colorscheme-red");
			for (int i = 0; i <= 5; i++) {
				lLinearGauge.markers().add(new PercentMarker(lLinearGauge, i * 20.0));
			}
			lFlowPane.getChildren().add(lLinearGauge);
			gauges.add(lLinearGauge);
		}
        
		// manually show indicators
		{
			final AbstractLinearGauge<?> lLinearGauge = createLinearGauge();
			lLinearGauge.indicators().add(new Indicator(0, "warning"));
			lLinearGauge.indicators().add(new Indicator(1, "error"));
			lLinearGauge.indicators().add(new Indicator(2, "warning"));
			lLinearGauge.indicators().add(new Indicator(3, "error"));
			lLinearGauge.indicators().add(new Indicator(4, "warning"));
			lLinearGauge.indicators().add(new Indicator(5, "error"));
			lLinearGauge.setStyle("-fx-border-color: #000000; -fxx-warning-indicator-visibility: visible; -fxx-error-indicator-visibility: visible; ");
			lFlowPane.getChildren().add(lLinearGauge);
			gauges.add(lLinearGauge);
		}
		
        // custom labels
		{
			final BasicRoundDailGauge lLinearGauge = new BasicRoundDailGauge();
			lLinearGauge.labels().clear();
			lLinearGauge.setStyle("-fxx-warning-indicator-visibility: visible; -fxx-error-indicator-visibility: visible; ");
			for (double d = 0.0; d < 100.0; d += 33.333) {
				Label lLabel = new AbsoluteLabel(d, Math.round(d) + "%");
				lLinearGauge.labels().add(lLabel);
			}
			lLinearGauge.setStyle("-fx-border-color: #000000; -fxx-warning-indicator-visibility: visible; -fxx-error-indicator-visibility: visible; ");
			lFlowPane.getChildren().add(lLinearGauge);
			gauges.add(lLinearGauge);
		}

	}
}

