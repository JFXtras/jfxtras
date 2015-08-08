/**
 * LocalDatePickerSkin.java
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

import java.time.LocalDate;
import java.util.Calendar;
import java.util.logging.Logger;

import javafx.beans.InvalidationListener;
import javafx.scene.control.SkinBase;
import javafx.util.Callback;
import jfxtras.scene.control.CalendarPicker;
import jfxtras.scene.control.CalendarPicker.CalendarRange;
import jfxtras.scene.control.LocalDatePicker;
import jfxtras.scene.control.LocalDatePicker.LocalDateRange;

/**
 * This skin reuses CalendarPicker
 * @author Tom Eugelink
 *
 */
public class LocalDatePickerSkin extends SkinBase<LocalDatePicker>
{
private static final Logger log = Logger.getLogger( LocalDatePickerSkin.class.getName() );
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
		calendarPicker.styleProperty().bindBidirectional( getSkinnable().styleProperty() );
		
		// bind specifics
		calendarPicker.localeProperty().bindBidirectional( getSkinnable().localeProperty() );
		DateTimeToCalendarHelper.syncLocalDate(calendarPicker.calendarProperty(), getSkinnable().localDateProperty(), getSkinnable().localeProperty());
		DateTimeToCalendarHelper.syncLocalDate(calendarPicker.displayedCalendar(), getSkinnable().displayedLocalDateProperty(), getSkinnable().localeProperty());
		DateTimeToCalendarHelper.syncLocalDates(calendarPicker.calendars(), getSkinnable().localDates(), getSkinnable().localeProperty());
		DateTimeToCalendarHelper.syncLocalDates(calendarPicker.highlightedCalendars(), getSkinnable().highlightedLocalDates(), getSkinnable().localeProperty());
		DateTimeToCalendarHelper.syncLocalDates(calendarPicker.disabledCalendars(), getSkinnable().disabledLocalDates(), getSkinnable().localeProperty());
		syncMode();
		calendarPicker.allowNullProperty().bindBidirectional( getSkinnable().allowNullProperty() );
		calendarPicker.setCalendarRangeCallback(new Callback<CalendarRange,Void>() {
			@Override
			public Void call(CalendarRange calendarRange) {
				Callback<LocalDateRange, Void> lCallback = getSkinnable().getLocalDateRangeCallback();
				if (lCallback == null) {
					return null;
				}
				return lCallback.call(new LocalDatePicker.LocalDateRange(DateTimeToCalendarHelper.createLocalDateFromCalendar(calendarRange.getStartCalendar()), DateTimeToCalendarHelper.createLocalDateFromCalendar(calendarRange.getEndCalendar())));
			}
		});
		calendarPicker.setValueValidationCallback(new Callback<Calendar, Boolean>() {
			@Override
			public Boolean call(Calendar calendar) {
				Callback<LocalDate, Boolean> lCallback = getSkinnable().getValueValidationCallback();
				if (lCallback == null) {
					return true;
				}
				return lCallback.call(DateTimeToCalendarHelper.createLocalDateFromCalendar(calendar));
			}
		});
	}
	
	// ==================================================================================================================
	// BINDING
	
	private void syncMode()
	{
		// forward changes from calendar
		calendarPicker.modeProperty().addListener( (invalidationEvent) -> {
			CalendarPicker.Mode newValue = calendarPicker.getMode();
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
		InvalidationListener localDatePickerModeInvalidationListener = (invalidationEvent) -> {
			LocalDatePicker.Mode newValue = getSkinnable().getMode();
			if (newValue == LocalDatePicker.Mode.SINGLE) {
				calendarPicker.modeProperty().set(CalendarPicker.Mode.SINGLE); 
			}
			if (newValue == LocalDatePicker.Mode.RANGE) {
				calendarPicker.modeProperty().set(CalendarPicker.Mode.RANGE); 
			}
			if (newValue == LocalDatePicker.Mode.MULTIPLE) {
				calendarPicker.modeProperty().set(CalendarPicker.Mode.MULTIPLE); 
			}
		};
		getSkinnable().modeProperty().addListener( localDatePickerModeInvalidationListener);
		// this will set the value on the initial construction
		localDatePickerModeInvalidationListener.invalidated(null);
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
