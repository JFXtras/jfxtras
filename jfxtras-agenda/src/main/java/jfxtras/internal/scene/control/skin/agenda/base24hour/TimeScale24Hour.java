/**
 * TimeScale24Hour.java
 *
 * Copyright (c) 2011-2015, JFXtras
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

package jfxtras.internal.scene.control.skin.agenda.base24hour;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import jfxtras.util.NodeUtil;

class TimeScale24Hour extends Pane {

	TimeScale24Hour(Pane pane, LayoutHelp layoutHelp) {
		this.pane = pane;
		this.layoutHelp = layoutHelp;
		
		// position
		layoutXProperty().set(0);
		layoutYProperty().set(0);
		prefWidthProperty().bind(pane.widthProperty());
		prefHeightProperty().bind(pane.heightProperty());

		// make completely transparent for all events
		setMouseTransparent(true);
		
		// add contents
		addTimeScale();
	}
	final Pane pane;
	final LayoutHelp layoutHelp;
	
	private void addTimeScale() {
		
		// draw hours
		for (int lHour = 0; lHour < 24; lHour++)
		{
			// hour line
			{
				Line l = new Line(0,10,100,10);
				l.setId("hourLine" + lHour);
				l.getStyleClass().add("HourLine");
				l.startXProperty().set(0.0);
				l.startYProperty().bind( NodeUtil.snapXY(layoutHelp.hourHeighProperty.multiply(lHour)) );
				l.endXProperty().bind( NodeUtil.snapXY(pane.widthProperty()));
				l.endYProperty().bind( NodeUtil.snapXY(l.startYProperty()));
				getChildren().add(l);
			}
			// half hour line
			{
				Line l = new Line(0,10,100,10);
				l.setId("halfHourLine" + lHour);
				l.getStyleClass().add("HalfHourLine");
				l.startXProperty().bind( NodeUtil.snapXY(layoutHelp.timeWidthProperty));
				l.endXProperty().bind( NodeUtil.snapXY(pane.widthProperty()));
				l.startYProperty().bind( NodeUtil.snapXY(layoutHelp.hourHeighProperty.multiply(lHour + 0.5)));
				l.endYProperty().bind( NodeUtil.snapXY(l.startYProperty()));
				getChildren().add(l);
			}
			// hour text
			{
				Text t = new Text(lHour + ":00");
				t.xProperty().bind(layoutHelp.timeWidthProperty.subtract(t.getBoundsInParent().getWidth()).subtract(layoutHelp.timeColumnWhitespaceProperty.get() / 2));
				t.yProperty().bind(layoutHelp.hourHeighProperty.multiply(lHour));
				t.setTranslateY(t.getBoundsInParent().getHeight()); // move it under the line
				t.getStyleClass().add("HourLabel");
				t.setFontSmoothingType(FontSmoothingType.LCD);
				getChildren().add(t);
			}
		}

	}
}
