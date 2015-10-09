/**
 * AppointmentRegularBodyPane.java
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

import java.time.LocalDate;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.scene.text.Text;
import jfxtras.scene.control.agenda.Agenda.Appointment;

public class AppointmentRegularBodyPane extends AppointmentAbstractTrackedPane {

	public AppointmentRegularBodyPane(LocalDate localDate, Appointment appointment, LayoutHelp layoutHelp) {
		super(localDate, appointment, layoutHelp);
		
		// strings
		this.startAsString = layoutHelp.timeDateTimeFormatter.format(this.startDateTime);
		this.endAsString = layoutHelp.timeDateTimeFormatter.format(this.endDateTime);

		// add the duration as text
		Text lTimeText = new Text((firstPaneOfAppointment ? startAsString : "") + "-" + (lastPaneOfAppointment ? endAsString : ""));
		{
			lTimeText.getStyleClass().add("AppointmentTimeLabel");
			lTimeText.setX(layoutHelp.paddingProperty.get() );
			lTimeText.setY(lTimeText.prefHeight(0));
			layoutHelp.clip(this, lTimeText, widthProperty().subtract( layoutHelp.paddingProperty ), heightProperty().add(0.0), true, 0.0);
			getChildren().add(lTimeText);
		}
		
		// add summary
		Text lSummaryText = new Text(appointment.getSummary());
		{
			lSummaryText.getStyleClass().add("AppointmentLabel");
			lSummaryText.setX( layoutHelp.paddingProperty.get() );
			lSummaryText.setY( lTimeText.getY() + layoutHelp.textHeightProperty.get());
			lSummaryText.wrappingWidthProperty().bind(widthProperty().subtract( layoutHelp.paddingProperty.get() ));
			layoutHelp.clip(this, lSummaryText, widthProperty().add(0.0), heightProperty().subtract( layoutHelp.paddingProperty ), false, 0.0);
			getChildren().add(lSummaryText);			
		}
		
		// add the menu header
		getChildren().add(appointmentMenu);
		
		// add the duration dragger
		layoutHelp.skinnable.allowResizeProperty().addListener(new WeakInvalidationListener(allowResizeInvalidationListener));
		setupDurationDragger();
	}
	private String startAsString;
	private String endAsString;
	final private InvalidationListener allowResizeInvalidationListener = new InvalidationListener() {
		@Override
		public void invalidated(Observable arg0) {
			setupDurationDragger();
		}
	};
	
	/**
	 * 
	 */
	private void setupDurationDragger() {
		if (lastPaneOfAppointment && layoutHelp.skinnable.getAllowResize()) {
			if (durationDragger == null) {
				durationDragger = new DurationDragger(this, appointment, layoutHelp);
			}
			getChildren().add(durationDragger);
		}
		else {
			getChildren().remove(durationDragger);
		}
	}
	private DurationDragger durationDragger = null;
}
