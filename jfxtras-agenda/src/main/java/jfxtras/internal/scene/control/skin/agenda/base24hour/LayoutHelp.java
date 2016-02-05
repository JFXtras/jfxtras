/**
 * LayoutHelp.java
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

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.util.Locale;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.NodeOrientation;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Callback;
import jfxtras.internal.scene.control.skin.agenda.AgendaSkin;
import jfxtras.internal.scene.control.skin.agenda.base24hour.AppointmentAbstractPane.AppointmentForDrag;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.Agenda.Appointment;

/**
 * This class is not a class but a data holder, a record, all fields are accessed directly.
 * Its methods are utility methods, which normally would be statics in a util class. 
 */
class LayoutHelp {
	public LayoutHelp(Agenda skinnable, AgendaSkin skin) {
		this.skinnable = skinnable;
		this.skin = skin;
		dragPane = new DragPane(this);
		
		// header
		titleDateTimeHeightProperty.bind( textHeightProperty.multiply(1.5) ); 
		appointmentHeaderPaneHeightProperty.bind( textHeightProperty.add(5) ); // not sure why the 5 is needed
		headerHeightProperty.bind( highestNumberOfWholedayAppointmentsProperty.multiply( appointmentHeaderPaneHeightProperty ).add( titleDateTimeHeightProperty ) );

		// day columns
		dayFirstColumnXProperty.bind( timeWidthProperty );
		dayContentWidthProperty.bind( dayWidthProperty.subtract(10) ); // the 10 is a margin at the right so that there is always room to start a new appointment
		
		// hour height
		dayHeightProperty.bind(hourHeighProperty.multiply(24));
		durationInMSPerPixelProperty.bind( msPerDayProperty.divide(dayHeightProperty) );
		
		// generic
		Text textHeight = new Text("X");
		textHeight.getStyleClass().add("Agenda");		
		textHeightProperty.set( textHeight.getBoundsInParent().getHeight() );
		
		// time column
		Text textWidth = new Text("88:88");
		textWidth.getStyleClass().add("Agenda");		
		timeWidthProperty.bind( timeColumnWhitespaceProperty.add( textWidth.getBoundsInParent().getWidth() )  );
	}
	final Agenda skinnable;
	final AgendaSkin skin;
	final DragPane dragPane;
	
	final DoubleProperty msPerDayProperty = new SimpleDoubleProperty(24 * 60 * 60 * 1000);
	
	final IntegerProperty highestNumberOfWholedayAppointmentsProperty = new SimpleIntegerProperty(0);
	
	final DoubleProperty paddingProperty = new SimpleDoubleProperty(3);
	final DoubleProperty timeColumnWhitespaceProperty = new SimpleDoubleProperty(10);
	final DoubleProperty wholedayAppointmentFlagpoleWidthProperty = new SimpleDoubleProperty(5);
	final DoubleProperty textHeightProperty = new SimpleDoubleProperty(0);
	final DoubleProperty titleDateTimeHeightProperty = new SimpleDoubleProperty(0);
	final DoubleProperty headerHeightProperty = new SimpleDoubleProperty(0);
	final DoubleProperty appointmentHeaderPaneHeightProperty = new SimpleDoubleProperty(0);
	final DoubleProperty timeWidthProperty = new SimpleDoubleProperty(0); 
	final DoubleProperty dayFirstColumnXProperty = new SimpleDoubleProperty(0); 
	final DoubleProperty dayWidthProperty = new SimpleDoubleProperty(0); 
	final DoubleProperty dayContentWidthProperty = new SimpleDoubleProperty(0); 
	final DoubleProperty dayHeightProperty = new SimpleDoubleProperty(0);  
	final DoubleProperty durationInMSPerPixelProperty = new SimpleDoubleProperty(0);
	final DoubleProperty hourHeighProperty = new SimpleDoubleProperty(0);
	
	SimpleDateFormat dayOfWeekDateFormat = new SimpleDateFormat("E", Locale.getDefault());
	DateTimeFormatter dayOfWeekDateTimeFormatter = new DateTimeFormatterBuilder().appendPattern("E").toFormatter(Locale.getDefault());
	SimpleDateFormat dateFormat = (SimpleDateFormat)SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, Locale.getDefault());
	DateTimeFormatter dateDateTimeFormatter = new DateTimeFormatterBuilder().appendLocalized(FormatStyle.SHORT, null).toFormatter(Locale.getDefault());
	final static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
	DateTimeFormatter timeDateTimeFormatter = new DateTimeFormatterBuilder().appendPattern("HH:mm").toFormatter(Locale.getDefault());

	/**
	 * I have no clue why the wholeday appointment header needs an additional 10.0 px X offset in right-to-left mode
	 */
	void clip(Pane pane, Text text, DoubleBinding width, DoubleBinding height, boolean mirrorWidth, double additionMirorXOffset) {
		Rectangle lClip = new Rectangle(0,0,0,0);
		if (mirrorWidth && skinnable.getNodeOrientation().equals(NodeOrientation.RIGHT_TO_LEFT)) {
			lClip.xProperty().bind(pane.widthProperty().multiply(-1.0).add(text.getBoundsInParent().getWidth()).add(additionMirorXOffset));
		}
		lClip.widthProperty().bind(width);
		lClip.heightProperty().bind(height);
		text.setClip(lClip);
	}

	/**
	 * 
	 */
	void setupMouseOverAsBusy(final Node node) {
		// play with the mouse pointer to show something can be done here
		node.setOnMouseEntered( (mouseEvent) -> {
			if (!mouseEvent.isPrimaryButtonDown()) {						
				node.setCursor(Cursor.HAND);
				mouseEvent.consume();
			}
		});
		node.setOnMouseExited( (mouseEvent) -> {
			if (!mouseEvent.isPrimaryButtonDown()) {
				node.setCursor(Cursor.DEFAULT);
				mouseEvent.consume();
			}
		});
	}

	/**
	 * 
	 * @param localDateTime
	 * @param minutes
	 * @return
	 */
	LocalDateTime roundTimeToNearestMinutes(LocalDateTime localDateTime, int minutes)
	{
		localDateTime = localDateTime.withSecond(0).withNano(0);
		int lMinutes = localDateTime.getMinute() % minutes;
		if (lMinutes < (minutes/2)) {
			localDateTime = localDateTime.plusMinutes(-1 * lMinutes);
		}
		else {
			localDateTime = localDateTime.plusMinutes(minutes - lMinutes);
		}
		return localDateTime;
	}
	
    /**
     * Has the client added a callback to process the change?
     * @param appointment
     */
	void callAppointmentChangedCallback(Appointment appointment) {
		// ignore temp appointments
		if (!(appointment instanceof AppointmentAbstractPane.AppointmentForDrag)) {
		    Callback<Appointment, Void> lChangedCallback = skinnable.getAppointmentChangedCallback();
		    if (lChangedCallback != null) {
		        lChangedCallback.call(appointment);
		    }
		}
	}

}
