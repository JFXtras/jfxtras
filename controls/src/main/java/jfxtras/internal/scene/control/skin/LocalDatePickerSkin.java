/**
 * LocalDatePickerSkin.java
 *
 * Copyright (c) 2011-2014, JFXtras
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

import javafx.beans.value.ObservableValue;
import javafx.scene.control.SkinBase;
import jfxtras.scene.control.CalendarPicker;
import jfxtras.scene.control.LocalDatePicker;

/**
 * This skin reuses CalendarPicker
 * @author Tom Eugelink
 *
 */
public class LocalDatePickerSkin extends SkinBase<LocalDatePicker>
{
	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public LocalDatePickerSkin(LocalDatePicker control)
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
		calendarPicker.getStyleClass().addAll(getSkinnable().getClass().getSimpleName());
		calendarPicker.getStyleClass().addAll(getSkinnable().getStyleClass());
		getSkinnable().styleProperty().bindBidirectional( calendarPicker.styleProperty() );
		
		// bind specifics
		getSkinnable().localeProperty().bindBidirectional( calendarPicker.localeProperty() );
		getSkinnable().allowNullProperty().bindBidirectional( calendarPicker.allowNullProperty() );
		DateTimeToCalendarHelper.syncLocalDate(calendarPicker.calendarProperty(), getSkinnable().localDateProperty(), calendarPicker.localeProperty());
		DateTimeToCalendarHelper.syncLocalDate(calendarPicker.displayedCalendar(), getSkinnable().displayedLocalDateProperty(), calendarPicker.localeProperty());
		DateTimeToCalendarHelper.syncLocalDates(calendarPicker.calendars(), getSkinnable().localDates(), calendarPicker.localeProperty());
		DateTimeToCalendarHelper.syncLocalDates(calendarPicker.highlightedCalendars(), getSkinnable().highlightedLocalDates(), calendarPicker.localeProperty());
		DateTimeToCalendarHelper.syncLocalDates(calendarPicker.disabledCalendars(), getSkinnable().disabledLocalDates(), calendarPicker.localeProperty());
		syncMode();
	}
	
	// ==================================================================================================================
	// BINDING
	
	private void syncMode()
	{
		// forward changes from calendar
		calendarPicker.modeProperty().addListener( (ObservableValue<? extends CalendarPicker.Mode> observableValue, CalendarPicker.Mode oldValue, CalendarPicker.Mode newValue) -> {
			if (newValue == CalendarPicker.Mode.SINGLE) {
				getSkinnable().modeProperty().set(LocalDatePicker.Mode.SINGLE); 
			}
			if (newValue == CalendarPicker.Mode.RANGE) {
				getSkinnable().modeProperty().set(LocalDatePicker.Mode.RANGE); 
			}
			if (newValue == CalendarPicker.Mode.MULTIPLE) {
				getSkinnable().modeProperty().set(LocalDatePicker.Mode.MULTIPLE); 
			}
		});
		
		// forward changes to calendar
		getSkinnable().modeProperty().addListener( (ObservableValue<? extends LocalDatePicker.Mode> observableValue, LocalDatePicker.Mode oldValue, LocalDatePicker.Mode newValue) -> {
			if (newValue == LocalDatePicker.Mode.SINGLE) {
				calendarPicker.modeProperty().set(CalendarPicker.Mode.SINGLE); 
			}
			if (newValue == LocalDatePicker.Mode.RANGE) {
				calendarPicker.modeProperty().set(CalendarPicker.Mode.RANGE); 
			}
			if (newValue == LocalDatePicker.Mode.MULTIPLE) {
				calendarPicker.modeProperty().set(CalendarPicker.Mode.MULTIPLE); 
			}
		});
	}

    // ==================================================================================================================
	// DRAW
	
	/**
	 * construct the nodes
	 */
	private void createNodes()
	{
		// setup the grid so all weekday togglebuttons will grow, but the weeknumbers do not
		calendarPicker = new CalendarPicker();
		getChildren().add(calendarPicker);
		
		// setup CSS
        getSkinnable().getStyleClass().add(this.getClass().getSimpleName()); // always add self as style class, because CSS should relate to the skin not the control
	}
	private CalendarPicker calendarPicker = null;
}
