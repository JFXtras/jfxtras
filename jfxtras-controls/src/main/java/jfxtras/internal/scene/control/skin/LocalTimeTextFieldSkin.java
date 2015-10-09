/**
 * LocalTimeTextFieldSkin.java
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

package jfxtras.internal.scene.control.skin;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.SkinBase;
import jfxtras.scene.control.CalendarTimeTextField;
import jfxtras.scene.control.LocalTimeTextField;

/**
 * This skin reuses CalendarTimeTextField
 * @author Tom Eugelink
 *
 */
public class LocalTimeTextFieldSkin extends SkinBase<LocalTimeTextField>
{
	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public LocalTimeTextFieldSkin(LocalTimeTextField control)
	{
		super(control);
		construct();
	}

	/*
	 * construct the component
	 */
	private void construct()
	{
		// setup component
		createNodes();
		
		// basic control binding
		calendarTimeTextField.getStyleClass().addAll(getSkinnable().getClass().getSimpleName());
		calendarTimeTextField.getStyleClass().addAll(getSkinnable().getStyleClass());
		calendarTimeTextField.styleProperty().bindBidirectional( getSkinnable().styleProperty() );
		calendarTimeTextField.tooltipProperty().bindBidirectional( getSkinnable().tooltipProperty() ); 
                calendarTimeTextField.pickerShowingProperty().bindBidirectional(getSkinnable().pickerShowingProperty());

		// bind it up
		calendarTimeTextField.localeProperty().bindBidirectional( getSkinnable().localeProperty() );
		calendarTimeTextField.promptTextProperty().bindBidirectional( getSkinnable().promptTextProperty() );
		calendarTimeTextField.parseErrorCallbackProperty().bindBidirectional( getSkinnable().parseErrorCallbackProperty() );
		DateTimeToCalendarHelper.syncLocalTime(calendarTimeTextField.calendarProperty(), getSkinnable().localTimeProperty(), localeObjectProperty); //calendarTimeTextField.localeProperty());
		
		// formatter(s) require special attention
		DateTimeToCalendarHelper.syncDateTimeFormatterForTime(calendarTimeTextField.dateFormatProperty(), getSkinnable().dateTimeFormatterProperty());
		DateTimeToCalendarHelper.syncDateTimeFormattersForTime(calendarTimeTextField.dateFormatsProperty(), getSkinnable().dateTimeFormattersProperty());
	}
	final private ObjectProperty<Locale> localeObjectProperty = new SimpleObjectProperty<Locale>(Locale.getDefault(), "locale", new Locale("NL")); //Locale.getDefault());
	
	// ==================================================================================================================
	// DRAW
	
	/**
	 * construct the nodes
	 */
	private void createNodes()
	{
		// setup the grid so all weekday togglebuttons will grow, but the weeknumbers do not
		calendarTimeTextField = new CalendarTimeTextField().withDateFormat(SimpleDateFormat.getTimeInstance(DateFormat.LONG, localeObjectProperty.get())); //getSkinnable().getLocale())); // TODO this is not right
		getChildren().add(calendarTimeTextField);
		
		// setup CSS
        getSkinnable().getStyleClass().add(this.getClass().getSimpleName()); // always add self as style class, because CSS should relate to the skin not the control
	}
	private CalendarTimeTextField calendarTimeTextField = null;
}
