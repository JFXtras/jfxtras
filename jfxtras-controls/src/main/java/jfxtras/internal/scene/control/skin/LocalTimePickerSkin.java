/**
 * LocalTimePickerSkin.java
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

import java.time.LocalTime;
import java.util.Calendar;

import javafx.scene.control.SkinBase;
import javafx.util.Callback;
import jfxtras.scene.control.CalendarTimePicker;
import jfxtras.scene.control.LocalTimePicker;

/**
 * This skin reuses CalendarPicker
 * @author Tom Eugelink
 *
 */
public class LocalTimePickerSkin extends SkinBase<LocalTimePicker>
{
	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public LocalTimePickerSkin(LocalTimePicker control)
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

		// bind basic node
		calendarTimePicker.getStyleClass().addAll(getSkinnable().getClass().getSimpleName());
		calendarTimePicker.getStyleClass().addAll(getSkinnable().getStyleClass());
		calendarTimePicker.styleProperty().bindBidirectional( getSkinnable().styleProperty() );
		
		// bind specifics
		calendarTimePicker.minuteStepProperty().bindBidirectional( getSkinnable().minuteStepProperty() );
		calendarTimePicker.secondStepProperty().bindBidirectional( getSkinnable().secondStepProperty() );
		calendarTimePicker.localeProperty().bindBidirectional( getSkinnable().localeProperty() );
		DateTimeToCalendarHelper.syncLocalTime(calendarTimePicker.calendarProperty(), getSkinnable().localTimeProperty(), getSkinnable().localeProperty());
		calendarTimePicker.setValueValidationCallback(new Callback<Calendar, Boolean>() {
			@Override
			public Boolean call(Calendar calendar) {
				Callback<LocalTime, Boolean> lCallback = getSkinnable().getValueValidationCallback();
				if (lCallback == null) {
					return true;
				}
				return lCallback.call(DateTimeToCalendarHelper.createLocalTimeFromCalendar(calendar));
			}
		});
	}

	// ==================================================================================================================
	// BINDING
	
    // ==================================================================================================================
	// DRAW
	
	/**
	 * construct the nodes
	 */
	private void createNodes()
	{
		// setup the grid so all weekday togglebuttons will grow, but the weeknumbers do not
		calendarTimePicker = new CalendarTimePicker();
		getChildren().add(calendarTimePicker);
		
		// setup CSS
        getSkinnable().getStyleClass().add(this.getClass().getSimpleName()); // always add self as style class, because CSS should relate to the skin not the control
	}
	private CalendarTimePicker calendarTimePicker = null;
}
