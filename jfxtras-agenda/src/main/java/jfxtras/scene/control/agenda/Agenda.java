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

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Locale;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.util.Callback;
import jfxtras.internal.scene.control.skin.DateTimeToCalendarHelper;
import jfxtras.internal.scene.control.skin.agenda.AgendaSkin;
import jfxtras.internal.scene.control.skin.agenda.AgendaWeekSkin;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * This controls renders appointments similar to Google Calendar.
 * Normally this would be called a Calendar, but since there are controls like CalendarPicker, this would be confusing, hence the name "Agenda".
 * 
 * The control has a list of appointments (classes implementing the Agenda.Appointment interface) and a calendar (date) that should be displayed.
 * The the appropriate appointments for the displayed time frame will be rendered.
 * The coder could provide all appointments in one big list, but that may be a bit memory heavy.
 * An alternative is to register to the displayedCalendar property and upon change update the appointment collection to match the time frame.
 * 
 * Each appointment must have an appointment group, this is a class implementing the Agenda.AppointmentGroup interface (Agenda.AppointmentGroupImpl is available).
 * The most important thing this class provides is a style class name which takes care of the rendering (color) of all appointments in that group.
 * Per default agenda has style classes defined, called group0 .. group23. 
 * 
 * A new appointment can be added by click-and-drag, but only if a createAppointmentCallbackProperty() is set. 
 * This method should return a new appointment object, for example:
 * [source,java]
 * --
 *      agenda.newAppointmentCallbackProperty().set(new Callback<Agenda.DateTimeRange, Agenda.Appointment>() {
 *           @Override
 *           public Agenda.Appointment call(Agenda.DateTimeRange dateTimeRange) {
 *               return new Agenda.AppointmentImpl()
 *                       .withStartTime(dateTimeRange.getStartDateTime())
 *                       .withEndTime(dateTimeRange.getEndDateTime())
 *                       .withAppointmentGroup(new Agenda.AppointmentGroupImpl().withStyleClass("group1")); // it is better to have a map of appointment groups to get from
 *           }
 *       });
 * --
 * 
 * Agenda has a default implementation of the Agenda.Appointment interface, in the form of Agenda.AppointmentImpl.
 *
 * @author Tom Eugelink &lt;tbee@tbee.org&gt;
 */
// TBEERNOT: should we use LocalDateTime or DateTime?
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
		// pref size
		setPrefSize(1000, 800);
		
		// setup the CSS
		// the -fx-skin attribute in the CSS sets which Skin class is used
		this.getStyleClass().add(this.getClass().getSimpleName());
		
		// handle deprecated
		DateTimeToCalendarHelper.syncLocalDateTime(displayedCalendarObjectProperty, displayedDateTimeObjectProperty, localeObjectProperty);
		
		// appointments
		constructAppointments();
	}

	/**
	 * Return the path to the CSS file so things are setup right
	 */
	@Override protected String getUserAgentStylesheet()
	{
		return this.getClass().getResource("/jfxtras/internal/scene/control/skin/agenda/" + Agenda.class.getSimpleName() + ".css").toExternalForm();
	}
	
	@Override public Skin<?> createDefaultSkin() {
		return new AgendaWeekSkin(this); 
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
	public ObjectProperty<Locale> localeProperty() { return localeObjectProperty; }
	final private ObjectProperty<Locale> localeObjectProperty = new SimpleObjectProperty<Locale>(this, "locale", Locale.getDefault());
	public Locale getLocale() { return localeObjectProperty.getValue(); }
	public void setLocale(Locale value) { localeObjectProperty.setValue(value); }
	public Agenda withLocale(Locale value) { setLocale(value); return this; } 

	/** 
	 * DisplayedCalendar: this calendar denotes the timeframe being displayed. 
	 * If the agenda is in week skin, it will display the week containing this date. (Things like FirstDayOfWeek are taken into account.)
	 * In month skin, the month containing this date.
	 */
	@Deprecated public ObjectProperty<Calendar> displayedCalendar() { return displayedCalendarObjectProperty; }
	private final ObjectProperty<Calendar> displayedCalendarObjectProperty = new SimpleObjectProperty<Calendar>(this, "displayedCalendar", Calendar.getInstance());
	@Deprecated public Calendar getDisplayedCalendar() { return displayedCalendarObjectProperty.getValue(); }
	@Deprecated public void setDisplayedCalendar(Calendar value) { displayedCalendarObjectProperty.setValue(value); }
	@Deprecated public Agenda withDisplayedCalendar(Calendar value) { setDisplayedCalendar(value); return this; }
	
	/** 
	 * The skin will use this date to determine what to display.
	 */
	public ObjectProperty<LocalDateTime> displayedDateTime() { return displayedDateTimeObjectProperty; }
	private final ObjectProperty<LocalDateTime> displayedDateTimeObjectProperty = new SimpleObjectProperty<LocalDateTime>(this, "displayedDateTime", LocalDateTime.now()) {
		public void set(LocalDateTime value) {
			if (value == null) {
				throw new NullPointerException("Null not allowed");
			}
			super.set(value);
		}
	};
	public LocalDateTime getDisplayedDateTime() { return displayedDateTimeObjectProperty.getValue(); }
	public void setDisplayedDateTime(LocalDateTime value) { displayedDateTimeObjectProperty.setValue(value); }
	public Agenda withDisplayedDateTime(LocalDateTime value) { setDisplayedDateTime(value); return this; }
	
	/** selectedAppointments: */
	public ObservableList<Appointment> selectedAppointments() { return selectedAppointments; }
	final private ObservableList<Appointment> selectedAppointments =  javafx.collections.FXCollections.observableArrayList();

	/** calendarRangeCallback: 
	 * Appointments should match:
	 * - start date &gt;= range start
	 * - end date &lt;= range end
	 */
	@Deprecated public ObjectProperty<Callback<CalendarRange, Void>> calendarRangeCallbackProperty() { return calendarRangeCallbackObjectProperty; }
	@Deprecated final private ObjectProperty<Callback<CalendarRange, Void>> calendarRangeCallbackObjectProperty = new SimpleObjectProperty<Callback<CalendarRange, Void>>(this, "calendarRangeCallback", null);
	@Deprecated public Callback<CalendarRange, Void> getCalendarRangeCallback() { return this.calendarRangeCallbackObjectProperty.getValue(); }
	@Deprecated public void setCalendarRangeCallback(Callback<CalendarRange, Void> value) { this.calendarRangeCallbackObjectProperty.setValue(value); }
	@Deprecated public Agenda withCalendarRangeCallback(Callback<CalendarRange, Void> value) { setCalendarRangeCallback(value); return this; }
	
	/** dateTimeRangeCallback: 
	 * Appointments should match:
	 * - start date &gt;= range start
	 * - end date &lt;= range end
	 */
	public ObjectProperty<Callback<DateTimeRange, Void>> dateTimeRangeCallbackProperty() { return dateTimeRangeCallbackObjectProperty; }
	final private ObjectProperty<Callback<DateTimeRange, Void>> dateTimeRangeCallbackObjectProperty = new SimpleObjectProperty<Callback<DateTimeRange, Void>>(this, "dateTimeRangeCallback", null);
	public Callback<DateTimeRange, Void> getDateTimeRangeCallback() { return this.dateTimeRangeCallbackObjectProperty.getValue(); }
	public void setDateTimeRangeCallback(Callback<DateTimeRange, Void> value) { this.dateTimeRangeCallbackObjectProperty.setValue(value); }
	public Agenda withDateTimeRangeCallback(Callback<DateTimeRange, Void> value) { setDateTimeRangeCallback(value); return this; }
	
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
	@Deprecated public ObjectProperty<Callback<CalendarRange, Appointment>> createAppointmentCallbackProperty() { return createAppointmentCallbackObjectProperty; }
	@Deprecated final private ObjectProperty<Callback<CalendarRange, Appointment>> createAppointmentCallbackObjectProperty = new SimpleObjectProperty<Callback<CalendarRange, Appointment>>(this, "createAppointmentCallback", null);
	@Deprecated public Callback<CalendarRange, Appointment> getCreateAppointmentCallback() { return this.createAppointmentCallbackObjectProperty.getValue(); }
	@Deprecated public void setCreateAppointmentCallback(Callback<CalendarRange, Appointment> value) { this.createAppointmentCallbackObjectProperty.setValue(value); }
	@Deprecated public Agenda withCreateAppointmentCallback(Callback<CalendarRange, Appointment> value) { setCreateAppointmentCallback(value); return this; }
	
	/** addAppointmentCallback:
	 * Since the Agenda is not the owner of the appointments but only dictates an interface, it does not know how to create a new one.
	 * So you need to implement this callback and create an appointment.
	 * The calendars in the provided range specify the start and end times, they can be used to create the new appointment (they do not need to be cloned).
	 * Null may be returned to indicate that no appointment was created.
	 * 
	 * Example:
		lAgenda.createAppointmentCallbackProperty().set(new Callback&lt;Agenda.DateTimeRange, Appointment&gt;()
		{
            &#064;Override
			public Void call(DateTimeRange dateTimeRange)
			{
				return new Agenda.AppointmentImpl()
					.withStartDateTime(dateTimeRange.start)
					.withEndDateTime(dateTimeRange.end)
					.withSummary("new")
					.withDescription("new")
					.withStyleClass("group1");
			}
		});
	 * 
	 */
	public ObjectProperty<Callback<DateTimeRange, Appointment>> newAppointmentCallbackProperty() { return newAppointmentCallbackObjectProperty; }
	final private ObjectProperty<Callback<DateTimeRange, Appointment>> newAppointmentCallbackObjectProperty = new SimpleObjectProperty<Callback<DateTimeRange, Appointment>>(this, "newAppointmentCallback", null);
	public Callback<DateTimeRange, Appointment> getNewAppointmentCallback() { return this.newAppointmentCallbackObjectProperty.getValue(); }
	public void setNewAppointmentCallback(Callback<DateTimeRange, Appointment> value) { this.newAppointmentCallbackObjectProperty.setValue(value); }
	public Agenda withNewAppointmentCallback(Callback<DateTimeRange, Appointment> value) { setNewAppointmentCallback(value); return this; }

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
	
	/** actionCallback:
	 * This triggered when the action is called on an appointment, usually this is a double click
	 */
	public ObjectProperty<Callback<Appointment, Void>> actionCallbackProperty() { return actionCallbackObjectProperty; }
	final private ObjectProperty<Callback<Appointment, Void>> actionCallbackObjectProperty = new SimpleObjectProperty<Callback<Appointment, Void>>(this, "actionCallback", null);
	public Callback<Appointment, Void> getActionCallback() { return this.actionCallbackObjectProperty.getValue(); }
	public void setActionCallback(Callback<Appointment, Void> value) { this.actionCallbackObjectProperty.setValue(value); }
	public Agenda withActionCallback(Callback<Appointment, Void> value) { setActionCallback(value); return this; }
	
	/**
	 * A Calendar range, for callbacks
	 */
	@Deprecated static public class CalendarRange
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
	 * A Datetime range, for callbacks
	 */
	static public class DateTimeRange
	{
		public DateTimeRange(LocalDateTime start, LocalDateTime end)
		{
			this.start = start;
			this.end = end;
		}
		
		public LocalDateTime getStartDateTime() { return start; }
		final LocalDateTime start;
		
		public LocalDateTime getEndDateTime() { return end; }
		final LocalDateTime end; 
		
		public String toString() {
			return super.toString() + " " + start + " to " + end;
		}
	}
	
	/**
	 * Force the agenda to completely refresh itself
	 */
	public void refresh()
	{
		((AgendaSkin)getSkin()).refresh();
	}
	
	// ==================================================================================================================
	// Appointment
	
	/**
	 * The interface that all appointments must adhere to; you can provide your own implementation.
	 * You must either implement the Start & End using the Calendar based or LocalDateTime based methods
	 */
	static public interface Appointment
	{
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
		
		// ----
		// Calendar
		
		/** This method is not used by the control, it can only be called when implemented by the user through the default Datetime methods on this interface **/  
		default Calendar getStartTime() {
			throw new NotImplementedException();
		}
		/** This method is not used by the control, it can only be called when implemented by the user through the default Datetime methods on this interface **/  
		default public void setStartTime(Calendar c) {
			throw new NotImplementedException();
		}
		
		/** This method is not used by the control, it can only be called when implemented by the user through the default Datetime methods on this interface **/  
		default public Calendar getEndTime() {
			throw new NotImplementedException();
		}
		/** This method is not used by the control, it can only be called when implemented by the user through the default Datetime methods on this interface **/  
		default public void setEndTime(Calendar c) {
			throw new NotImplementedException();
		}
		
		// ----
		// DateTime
		
		default LocalDateTime getStartDateTime() {
			return DateTimeToCalendarHelper.createLocalDateTimeFromCalendar(getStartTime());
	    }
		default void setStartDateTime(LocalDateTime v) {
			setStartTime(DateTimeToCalendarHelper.createCalendarFromLocalDateTime(v, getLocale()));
	    }
		
		default LocalDateTime getEndDateTime() {
			return DateTimeToCalendarHelper.createLocalDateTimeFromCalendar(getEndTime());
	    }
		/**
		 * End is exclusive
		 */
		default void setEndDateTime(LocalDateTime v) {
			setEndTime(DateTimeToCalendarHelper.createCalendarFromLocalDateTime(v, getLocale()));
	    }
		
		/**
		 * Only needed to implement this if you use calendar based setter and getters, and you are not satisfied with using the default locale to convert it to LocalDateTime.
		 * Calendar unfortunately does not expose the Locale is was created under.
		 * @return
		 */
		default Locale getLocale() {
			return Locale.getDefault();
	    }
	}
	
	/**
	 * A class to help you get going; all the required methods of the interface are implemented as JavaFX properties 
	 */
	static public abstract class AppointmentImplBase<T> 
	{
		/** WholeDay: */
		public ObjectProperty<Boolean> wholeDayProperty() { return wholeDayObjectProperty; }
		final private ObjectProperty<Boolean> wholeDayObjectProperty = new SimpleObjectProperty<Boolean>(this, "wholeDay", false);
		public Boolean isWholeDay() { return wholeDayObjectProperty.getValue(); }
		public void setWholeDay(Boolean value) { wholeDayObjectProperty.setValue(value); }
		public T withWholeDay(Boolean value) { setWholeDay(value); return (T)this; } 
		
		/** Summary: */
		public ObjectProperty<String> summaryProperty() { return summaryObjectProperty; }
		final private ObjectProperty<String> summaryObjectProperty = new SimpleObjectProperty<String>(this, "summary");
		public String getSummary() { return summaryObjectProperty.getValue(); }
		public void setSummary(String value) { summaryObjectProperty.setValue(value); }
		public T withSummary(String value) { setSummary(value); return (T)this; } 
		
		/** Description: */
		public ObjectProperty<String> descriptionProperty() { return descriptionObjectProperty; }
		final private ObjectProperty<String> descriptionObjectProperty = new SimpleObjectProperty<String>(this, "description");
		public String getDescription() { return descriptionObjectProperty.getValue(); }
		public void setDescription(String value) { descriptionObjectProperty.setValue(value); }
		public T withDescription(String value) { setDescription(value); return (T)this; } 
		
		/** Location: */
		public ObjectProperty<String> locationProperty() { return locationObjectProperty; }
		final private ObjectProperty<String> locationObjectProperty = new SimpleObjectProperty<String>(this, "location");
		public String getLocation() { return locationObjectProperty.getValue(); }
		public void setLocation(String value) { locationObjectProperty.setValue(value); }
		public T withLocation(String value) { setLocation(value); return (T)this; } 
		
		/** AppointmentGroup: */
		public ObjectProperty<AppointmentGroup> appointmentGroupProperty() { return appointmentGroupObjectProperty; }
		final private ObjectProperty<AppointmentGroup> appointmentGroupObjectProperty = new SimpleObjectProperty<AppointmentGroup>(this, "appointmentGroup");
		public AppointmentGroup getAppointmentGroup() { return appointmentGroupObjectProperty.getValue(); }
		public void setAppointmentGroup(AppointmentGroup value) { appointmentGroupObjectProperty.setValue(value); }
		public T withAppointmentGroup(AppointmentGroup value) { setAppointmentGroup(value); return (T)this; }
	}
	
	/**
	 * A class to help you get going using Calendar; all the required methods of the interface are implemented as JavaFX properties 
	 */
	static public class AppointmentImpl extends AppointmentImplBase<AppointmentImpl> 
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
		
		public String toString()
		{
			return super.toString()
				 + ", "
				 + DateTimeToCalendarHelper.quickFormatCalendar(this.getStartTime())
				 + " - "
				 + DateTimeToCalendarHelper.quickFormatCalendar(this.getEndTime())
				 ;
		}
	}
	
	/**
	 * A class to help you get going using LocalDateTime; all the required methods of the interface are implemented as JavaFX properties 
	 */
	static public class AppointmentImpl2 extends AppointmentImplBase<AppointmentImpl2> 
	implements Appointment
	{
		/** StartDateTime: */
		public ObjectProperty<LocalDateTime> startDateTimeProperty() { return startDateTimeObjectProperty; }
		final private ObjectProperty<LocalDateTime> startDateTimeObjectProperty = new SimpleObjectProperty<LocalDateTime>(this, "startDateTime");
		public LocalDateTime getStartDateTime() { return startDateTimeObjectProperty.getValue(); }
		public void setStartDateTime(LocalDateTime value) { startDateTimeObjectProperty.setValue(value); }
		public AppointmentImpl2 withStartDateTime(LocalDateTime value) { setStartDateTime(value); return this; }
		
		/** EndDateTime: */
		public ObjectProperty<LocalDateTime> endDateTimeProperty() { return endDateTimeObjectProperty; }
		final private ObjectProperty<LocalDateTime> endDateTimeObjectProperty = new SimpleObjectProperty<LocalDateTime>(this, "endDateTime");
		public LocalDateTime getEndDateTime() { return endDateTimeObjectProperty.getValue(); }
		public void setEndDateTime(LocalDateTime value) { endDateTimeObjectProperty.setValue(value); }
		public AppointmentImpl2 withEndDateTime(LocalDateTime value) { setEndDateTime(value); return this; } 
		
		public String toString()
		{
			return super.toString()
				 + ", "
				 + this.getStartDateTime()
				 + " - "
				 + this.getEndDateTime()
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
}
