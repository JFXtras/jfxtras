/**
 * AppointmentTaskBodyPane.java
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

import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import jfxtras.scene.control.agenda.Agenda.Appointment;

class AppointmentTaskBodyPane extends AppointmentAbstractTrackedPane {

	AppointmentTaskBodyPane(Appointment appointment, LayoutHelp layoutHelp) {
		super(appointment.getStartLocalDateTime().toLocalDate(), appointment, layoutHelp);
		
		// strings
		this.startAsString = layoutHelp.timeDateTimeFormatter.format(this.startDateTime);

		// add the menu
		appointmentMenu.yProperty().bind(heightProperty().subtract(appointmentMenu.heightProperty()).divide(2.0)); // position is slightly different from the default
		getChildren().add(appointmentMenu);

		// add the start time as text
		getChildren().add(createTimeText());
		
		// add summary
		if (appointment.getSummary() != null) {
			getChildren().add(createSummaryText());
		}
	}
	private String startAsString;

	/**
	 * 
	 */
	private Text createTimeText() {
		timeText = new Text(startAsString);
		{
			timeText.getStyleClass().add("AppointmentTimeLabel");
			timeText.xProperty().bind( appointmentMenu.widthProperty().add(layoutHelp.paddingProperty).add(layoutHelp.paddingProperty) ); // directly next to the menu
			timeText.yProperty().bind( heightProperty().multiply(-0.5).add(timeText.prefHeight(0) / 2) );
		}
		return timeText;
	}
	private Text timeText = null;
	
	/**
	 * 
	 */
	private Text createSummaryText() {
		summaryText = new Text(appointment.getSummary().replace("\n", ""));
		summaryText.setWrappingWidth(0);
		summaryText.getStyleClass().add("AppointmentLabel");
		summaryText.xProperty().bind( timeText.xProperty().add(timeText.prefWidth(0.0)) ); // directly next to the timetext
		summaryText.yProperty().bind( timeText.yProperty() );
		summaryText.wrappingWidthProperty().bind(widthProperty().subtract( layoutHelp.paddingProperty.get() ));
		
		// place a clip over the text to only show a single line
		Rectangle lClip = new Rectangle(0,0,0,0);
		lClip.xProperty().bind(summaryText.xProperty());
		lClip.yProperty().bind(summaryText.yProperty().multiply(-1.0));
		lClip.widthProperty().bind(widthProperty().subtract(summaryText.yProperty()).subtract(40.0)); // no clue why I need to subtract an additional 40 pixels here
		lClip.heightProperty().set(timeText.prefHeight(0.0));
		summaryText.setClip(lClip);
		
		return summaryText;
	}
	private Text summaryText = null;
}
