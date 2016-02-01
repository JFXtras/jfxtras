/**
 * DurationDragger.java
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

import java.time.LocalDateTime;

import javafx.scene.Cursor;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Callback;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.util.NodeUtil;

class DurationDragger extends Rectangle
{

	DurationDragger(AppointmentRegularBodyPane appointmentPane, Appointment appointment, LayoutHelp layoutHelp)
	{
		// remember
		this.appointmentPane = appointmentPane;
		this.appointment = appointment;
		this.layoutHelp = layoutHelp;
		
		// bind: place at the bottom of the pane, 1/2 width of the pane, centered
		xProperty().bind( NodeUtil.snapXY(appointmentPane.widthProperty().multiply(0.25)) );
		yProperty().bind( NodeUtil.snapXY(appointmentPane.heightProperty().subtract(5)) );
		widthProperty().bind( appointmentPane.widthProperty().multiply(0.5) );
		setHeight(3);
		minimumHeight = ( ((AgendaSkinTimeScale24HourAbstract<?>)layoutHelp.skin).getSnapToMinutes() * 60 * 1000) / layoutHelp.durationInMSPerPixelProperty.get();
		
		// styling
		getStyleClass().add("DurationDragger");

		// mouse
		layoutHelp.setupMouseOverAsBusy(this);
		setupMouseDrag();
	}
	private final AppointmentRegularBodyPane appointmentPane;
	private final Appointment appointment;
	private final LayoutHelp layoutHelp;
	private double minimumHeight = 5;
	
	private void setupMouseDrag() {
		// start resize
		setOnMousePressed( (mouseEvent) -> {
			// only on primary
			if (mouseEvent.getButton().equals(MouseButton.PRIMARY) == false) {
				return;
			}

			// we handle this event
			mouseEvent.consume();
			
			// place a rectangle at exactly the same location as the appointmentPane
			setCursor(Cursor.V_RESIZE);
			resizeRectangle = new Rectangle(appointmentPane.getLayoutX(), appointmentPane.getLayoutY(), appointmentPane.getWidth(), appointmentPane.getHeight()); // the values are already snapped
			resizeRectangle.getStyleClass().add("GhostRectangle");
			((Pane)appointmentPane.getParent()).getChildren().add(resizeRectangle);
			
			// place a text node at the bottom of the resize rectangle
			endTimeText = new Text(layoutHelp.timeDateTimeFormatter.format(appointment.getEndLocalDateTime()));
			endTimeText.layoutXProperty().set(appointmentPane.getLayoutX()); 
			endTimeText.layoutYProperty().bind(resizeRectangle.heightProperty().add(appointmentPane.getLayoutY())); 
			endTimeText.getStyleClass().add("GhostRectangleText");
			((Pane)appointmentPane.getParent()).getChildren().add(endTimeText);
		});
		
		// visualize resize
		setOnMouseDragged( (mouseEvent) -> {
			// only on primary
			if (mouseEvent.getButton().equals(MouseButton.PRIMARY) == false) {
				return;
			}

			// we handle this event
			mouseEvent.consume();
			
			//  calculate the number of pixels from on-screen nodeY (layoutY) to on-screen mouseY					
			double lNodeScreenY = NodeUtil.screenY(appointmentPane);
			double lMouseY = mouseEvent.getScreenY();
			double lHeight = lMouseY - lNodeScreenY;
			if (lHeight < minimumHeight) {
				lHeight = minimumHeight; // prevent underflow
			}
			resizeRectangle.setHeight( NodeUtil.snapWH(resizeRectangle.getLayoutY(), lHeight) );

			// show the current time in the label
			LocalDateTime endLocalDateTime = calculateEndDateTime();
			endTimeText.setText(layoutHelp.timeDateTimeFormatter.format(endLocalDateTime));
		});
		
		// end resize
		setOnMouseReleased( (mouseEvent) -> {			
			// only on primary
			if (mouseEvent.getButton().equals(MouseButton.PRIMARY) == false) {
				return;
			}

			LocalDateTime endLocalDateTime = calculateEndDateTime(); // must be done before the UI is reset and the event is consumed
							
			// we handle this event
			mouseEvent.consume();
			
			// reset ui
			setCursor(Cursor.HAND);
			((Pane)appointmentPane.getParent()).getChildren().remove(resizeRectangle);
			resizeRectangle = null;					
			((Pane)appointmentPane.getParent()).getChildren().remove(endTimeText);
			endTimeText = null;
			
			// set the new enddate
			appointmentPane.appointment.setEndLocalDateTime(endLocalDateTime);
			layoutHelp.callAppointmentChangedCallback(appointment);

			// relayout the entire skin
			layoutHelp.skin.setupAppointments();
		});
	}
	private Rectangle resizeRectangle = null;
	private Text endTimeText = null;
	
	private LocalDateTime calculateEndDateTime() {
		
		// calculate the new end datetime for the appointment (recalculating the duration)
		int ms = (int)(resizeRectangle.getHeight() * layoutHelp.durationInMSPerPixelProperty.get());
		LocalDateTime endLocalDateTime = appointmentPane.startDateTime.plusSeconds(ms / 1000);					
		
		// round to X minutes accuracy
		endLocalDateTime = layoutHelp.roundTimeToNearestMinutes(endLocalDateTime, (int)((AgendaSkinTimeScale24HourAbstract<?>)layoutHelp.skin).getSnapToMinutes());
		return endLocalDateTime;
	}
}
