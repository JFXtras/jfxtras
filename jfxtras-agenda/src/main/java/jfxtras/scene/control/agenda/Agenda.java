/**
 * Agenda.java
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

package jfxtras.scene.control.agenda;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.util.Callback;

/**
 * This controls renders appointments similar to Google Calendar.
 * Normally this would be called a Calendar, but since there are controls like CalendarPicker, this would be confusing, hence the name "Agenda".
 * 
 * The control has a list of appointments and a calendar (date) that should be displayed.
 * The skin will use the calendar to display the appropriate time frames and then it scans the list of appointments and display those that are visible.
 * 
 * You could provide all available appointments in one big list, but that may be a bit memory heavy.
 * An alternative is to register to the displayedCalendar property and upon change update the appointment collection.
 * A drawback of this approach is that you need to know what skin (day, week, ...) is active, so the correct set is provided, but most of the time this is known to the coder.
 * Therefore all skins are obligated to report the range they display via the calendarRangeCallback.
 * So if the calendar changes, the skin adapts and reports the new range via the callback, through which the coder can set the correct appointments.
 *
 * @author Tom Eugelink &lt;tbee@tbee.org&gt;
 */
public class Agenda extends Control
{
	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public Agenda()
	{
		construct();
	}
	
	/*
	 * 
	 */
	private void construct()
	{
		// setup the CSS
		// the -fx-skin attribute in the CSS sets which Skin class is used
		this.getStyleClass().add(this.getClass().getSimpleName());
		
		// appointments
		constructAppointments();
	}

	/**
	 * Return the path to the CSS file so things are setup right
	 */
	@Override protected String getUserAgentStylesheet()
	{
		return this.getClass().getResource("/jfxtras/internal/scene/control/skin/agenda/" + this.getClass().getSimpleName() + ".css").toExternalForm();
	}
	
	// ==================================================================================================================
	// PROPERTIES

	/** Id */
	public Agenda withId(String value) { setId(value); return this; }

	/** Appointments: */
	public ObservableList<Appointment> appointments() { return appointments; }
	final private ObservableList<Appointment> appointments =  javafx.collections.FXCollections.observableArrayList();
	private void constructAppointments()
	{
		// when appointments are removed, they can't be selected anymore
		appointments.addListener(new ListChangeListener<Agenda.Appointment>() 
		{
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Appointment> changes)
			{
				while (changes.next())
				{
					for (Appointment lAppointment : changes.getRemoved())
					{
						selectedAppointments.remove(lAppointment);
					}
				}
			} 
		});
	}
	
	/** AppointmentGroups: */
	public ObservableList<AppointmentGroup> appointmentGroups() { return appointmentGroups; }
	final private ObservableList<AppointmentGroup> appointmentGroups =  javafx.collections.FXCollections.observableArrayList();

	/** Locale: the locale is used to determine first-day-of-week, weekday labels, etc */
	public ObjectProperty<Locale> localeProperty() { return iLocaleObjectProperty; }
	final private ObjectProperty<Locale> iLocaleObjectProperty = new SimpleObjectProperty<Locale>(this, "locale", Locale.getDefault());
	public Locale getLocale() { return iLocaleObjectProperty.getValue(); }
	public void setLocale(Locale value) { iLocaleObjectProperty.setValue(value); }
	public Agenda withLocale(Locale value) { setLocale(value); return this; } 

	/** 
	 * DisplayedCalendar: this calendar denotes the timeframe being displayed. 
	 * If the agenda is in week skin, it will display the week containing this date. (Things like FirstDayOfWeek are taken into account.)
	 * In month skin, the month containing this date.
	 */
	public ObjectProperty<Calendar> displayedCalendar() { return displayedCalendarObjectProperty; }
	private final ObjectProperty<Calendar> displayedCalendarObjectProperty = new SimpleObjectProperty<Calendar>(this, "displayedCalendar", Calendar.getInstance())
	{
		public void set(Calendar value)
		{
			if (value == null) {
				throw new NullPointerException("Null not allowed");
			}
			super.set(value);
		}
	};
	public Calendar getDisplayedCalendar() { return displayedCalendarObjectProperty.getValue(); }
	public void setDisplayedCalendar(Calendar value) { displayedCalendarObjectProperty.setValue(value); }
	public Agenda withDisplayedCalendar(Calendar value) { setDisplayedCalendar(value); return this; }
	
	/** selectedAppointments: */
	public ObservableList<Appointment> selectedAppointments() { return selectedAppointments; }
	final private ObservableList<Appointment> selectedAppointments =  javafx.collections.FXCollections.observableArrayList();

	/** calendarRangeCallback: 
	 * Appointments should match:
	 * - start date &gt;= range start
	 * - end date &lt;= range end
	 */
	public ObjectProperty<Callback<CalendarRange, Void>> calendarRangeCallbackProperty() { return calendarRangeCallbackObjectProperty; }
	final private ObjectProperty<Callback<CalendarRange, Void>> calendarRangeCallbackObjectProperty = new SimpleObjectProperty<Callback<CalendarRange, Void>>(this, "calendarRangeCallback", null);
	public Callback<CalendarRange, Void> getCalendarRangeCallback() { return this.calendarRangeCallbackObjectProperty.getValue(); }
	public void setCalendarRangeCallback(Callback<CalendarRange, Void> value) { this.calendarRangeCallbackObjectProperty.setValue(value); }
	public Agenda withCalendarRangeCallback(Callback<CalendarRange, Void> value) { setCalendarRangeCallback(value); return this; }
	
	/** addAppointmentCallback:
	 * Since the Agenda is not the owner of the appointments but only dictates an interface, it does not know how to create a new one.
	 * So you need to implement this callback and create an appointment.
	 * The calendars in the provided range specify the start and end times, they can be used to create the new appointment (they do not need to be cloned).
	 * Null may be returned to indicate that no appointment was created.
	 * 
	 * Example:
		lAgenda.createAppointmentCallbackProperty().set(new Callback&lt;Agenda.CalendarRange, Appointment&gt;()
		{
            &#064;Override
			public Void call(CalendarRange calendarRange)
			{
				return new Agenda.AppointmentImpl()
					.withStartTime(calendarRange.start)
					.withEndTime(calendarRange.end)
					.withSummary("new")
					.withDescription("new")
					.withStyleClass("group1");
			}
		});
	 * 
	 */
	public ObjectProperty<Callback<CalendarRange, Appointment>> createAppointmentCallbackProperty() { return createAppointmentCallbackObjectProperty; }
	final private ObjectProperty<Callback<CalendarRange, Appointment>> createAppointmentCallbackObjectProperty = new SimpleObjectProperty<Callback<CalendarRange, Appointment>>(this, "createAppointmentCallback", null);
	public Callback<CalendarRange, Appointment> getCreateAppointmentCallback() { return this.createAppointmentCallbackObjectProperty.getValue(); }
	public void setCreateAppointmentCallback(Callback<CalendarRange, Appointment> value) { this.createAppointmentCallbackObjectProperty.setValue(value); }
	public Agenda withCreateAppointmentCallback(Callback<CalendarRange, Appointment> value) { setCreateAppointmentCallback(value); return this; }
	
	/** editAppointmentCallback:
	 * Agenda has a default popup, but maybe you want to do something yourself.
	 * If so, you need to set this callback method and open your own window.
	 * Because Agenda does not dictate a event/callback in the implementation of appointment, it has no way of being informed of changes on the appointment.
	 * So when the custom edit is done, make sure that control gets updated, if this does not happen automatically through any of the existing listeners, then call refresh(). 
	 */
	public ObjectProperty<Callback<Appointment, Void>> editAppointmentCallbackProperty() { return editAppointmentCallbackObjectProperty; }
	final private ObjectProperty<Callback<Appointment, Void>> editAppointmentCallbackObjectProperty = new SimpleObjectProperty<Callback<Appointment, Void>>(this, "editAppointmentCallback", null);
	public Callback<Appointment, Void> getEditAppointmentCallback() { return this.editAppointmentCallbackObjectProperty.getValue(); }
	public void setEditAppointmentCallback(Callback<Appointment, Void> value) { this.editAppointmentCallbackObjectProperty.setValue(value); }
	public Agenda withEditAppointmentCallback(Callback<Appointment, Void> value) { setEditAppointmentCallback(value); return this; }
	
	/**
	 * A Calendar range
	 */
	static public class CalendarRange
	{
		public CalendarRange(Calendar start, Calendar end)
		{
			this.start = start;
			this.end = end;
		}
		
		public Calendar getStartCalendar() { return start; }
		final Calendar start;
		
		public Calendar getEndCalendar() { return end; }
		final Calendar end; 
	}
	
	/**
	 * Force the agenda to completely refresh itself
	 */
	public void refresh()
	{
		((AgendaSkin)getSkin()).refresh();
	}
	
	public static interface AgendaSkin
	{
		public void refresh();
	}

	// ==================================================================================================================
	// Appointment
	
	/**
	 * The interface that all appointments must adhere to; you can provide your own implementation.
	 */
	static public interface Appointment
	{
		public Calendar getStartTime();
		public void setStartTime(Calendar c);
		
		public Calendar getEndTime();
		public void setEndTime(Calendar c);
		
		public Boolean isWholeDay();
		public void setWholeDay(Boolean b);
		
		public String getSummary();
		public void setSummary(String s);
		
		public String getDescription();
		public void setDescription(String s);
		
		public String getLocation();
		public void setLocation(String s);
		
		public AppointmentGroup getAppointmentGroup();
		public void setAppointmentGroup(AppointmentGroup s);
	}
	
	/**
	 * A class to help you get going; all the required methods of the interface are implemented as JavaFX properties 
	 */
	static public class AppointmentImpl 
	implements Appointment
	{
		/** StartTime: */
		public ObjectProperty<Calendar> startTimeProperty() { return startTimeObjectProperty; }
		final private ObjectProperty<Calendar> startTimeObjectProperty = new SimpleObjectProperty<Calendar>(this, "startTime");
		public Calendar getStartTime() { return startTimeObjectProperty.getValue(); }
		public void setStartTime(Calendar value) { startTimeObjectProperty.setValue(value); }
		public AppointmentImpl withStartTime(Calendar value) { setStartTime(value); return this; }
		
		/** EndTime: */
		public ObjectProperty<Calendar> endTimeProperty() { return endTimeObjectProperty; }
		final private ObjectProperty<Calendar> endTimeObjectProperty = new SimpleObjectProperty<Calendar>(this, "endTime");
		public Calendar getEndTime() { return endTimeObjectProperty.getValue(); }
		public void setEndTime(Calendar value) { endTimeObjectProperty.setValue(value); }
		public AppointmentImpl withEndTime(Calendar value) { setEndTime(value); return this; } 
		
		/** WholeDay: */
		public ObjectProperty<Boolean> wholeDayProperty() { return wholeDayObjectProperty; }
		final private ObjectProperty<Boolean> wholeDayObjectProperty = new SimpleObjectProperty<Boolean>(this, "wholeDay", false);
		public Boolean isWholeDay() { return wholeDayObjectProperty.getValue(); }
		public void setWholeDay(Boolean value) { wholeDayObjectProperty.setValue(value); }
		public AppointmentImpl withWholeDay(Boolean value) { setWholeDay(value); return this; } 
		
		/** Summary: */
		public ObjectProperty<String> summaryProperty() { return summaryObjectProperty; }
		final private ObjectProperty<String> summaryObjectProperty = new SimpleObjectProperty<String>(this, "summary");
		public String getSummary() { return summaryObjectProperty.getValue(); }
		public void setSummary(String value) { summaryObjectProperty.setValue(value); }
		public AppointmentImpl withSummary(String value) { setSummary(value); return this; } 
		
		/** Description: */
		public ObjectProperty<String> descriptionProperty() { return descriptionObjectProperty; }
		final private ObjectProperty<String> descriptionObjectProperty = new SimpleObjectProperty<String>(this, "description");
		public String getDescription() { return descriptionObjectProperty.getValue(); }
		public void setDescription(String value) { descriptionObjectProperty.setValue(value); }
		public AppointmentImpl withDescription(String value) { setDescription(value); return this; } 
		
		/** Location: */
		public ObjectProperty<String> locationProperty() { return locationObjectProperty; }
		final private ObjectProperty<String> locationObjectProperty = new SimpleObjectProperty<String>(this, "location");
		public String getLocation() { return locationObjectProperty.getValue(); }
		public void setLocation(String value) { locationObjectProperty.setValue(value); }
		public AppointmentImpl withLocation(String value) { setLocation(value); return this; } 
		
		/** AppointmentGroup: */
		public ObjectProperty<AppointmentGroup> appointmentGroupProperty() { return appointmentGroupObjectProperty; }
		final private ObjectProperty<AppointmentGroup> appointmentGroupObjectProperty = new SimpleObjectProperty<AppointmentGroup>(this, "appointmentGroup");
		public AppointmentGroup getAppointmentGroup() { return appointmentGroupObjectProperty.getValue(); }
		public void setAppointmentGroup(AppointmentGroup value) { appointmentGroupObjectProperty.setValue(value); }
		public AppointmentImpl withAppointmentGroup(AppointmentGroup value) { setAppointmentGroup(value); return this; }
		
		public String toString()
		{
			return super.toString()
				 + ", "
				 + quickFormatCalendar(this.getStartTime())
				 + " - "
				 + quickFormatCalendar(this.getEndTime())
				 ;
		}
	}
	
	
	// ==================================================================================================================
	// AppointmentGroup
	
	/**
	 * The interface that appointment groups must adhere to; you can provide your own implementation.
	 * An appointment group is a binding element between appointments; it contains information about visualization, and in the future reminders, etc.
	 */
	static public interface AppointmentGroup
	{
		public String getDescription();
		public void setDescription(String s);
		
		public String getStyleClass(); // this is the CSS class being assigned
		public void setStyleClass(String s);
	}
	
	/**
	 * A class to help you get going; all the required methods of the interface are implemented as JavaFX properties 
	 */
	static public class AppointmentGroupImpl 
	implements AppointmentGroup
	{
		/** Description: */
		public ObjectProperty<String> descriptionProperty() { return descriptionObjectProperty; }
		final private ObjectProperty<String> descriptionObjectProperty = new SimpleObjectProperty<String>(this, "description");
		public String getDescription() { return descriptionObjectProperty.getValue(); }
		public void setDescription(String value) { descriptionObjectProperty.setValue(value); }
		public AppointmentGroupImpl withDescription(String value) { setDescription(value); return this; } 
				
		/** StyleClass: */
		public ObjectProperty<String> styleClassProperty() { return styleClassObjectProperty; }
		final private ObjectProperty<String> styleClassObjectProperty = new SimpleObjectProperty<String>(this, "styleClass");
		public String getStyleClass() { return styleClassObjectProperty.getValue(); }
		public void setStyleClass(String value) { styleClassObjectProperty.setValue(value); }
		public AppointmentGroupImpl withStyleClass(String value) { setStyleClass(value); return this; }
	}
	
	
	// ==================================================================================================================
	// SUPPORT

	/*
	 * 
	 */
	static public String quickFormatCalendar(Calendar value)
	{
		if (value == null) return "";
		SimpleDateFormat lSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		return lSimpleDateFormat.format(value.getTime());
	}
	static public String quickFormatCalendar(List<Calendar> value)
	{
		if (value == null) return "null";
		String s = value.size() + "x [";
		for (Calendar lCalendar : value)
		{
			if (s.endsWith("[") == false) s += ",";
			s += quickFormatCalendar(lCalendar);
		}
		s += "]";
		return s;
	}
}
