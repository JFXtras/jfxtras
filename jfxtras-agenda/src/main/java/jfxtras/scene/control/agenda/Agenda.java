/**
 * Agenda.java
 *
 * Copyright (c) 2011-2016, JFXtras
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.print.PrinterJob;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.util.Callback;
import jfxtras.internal.scene.control.skin.DateTimeToCalendarHelper;
import jfxtras.internal.scene.control.skin.agenda.AgendaSkin;
import jfxtras.internal.scene.control.skin.agenda.AgendaWeekSkin;

/**
 * = Agenda
 * 
 * This controls renders appointments similar to Google Calendar.
 * Normally this would be called a Calendar, but since there are controls like CalendarPicker, this would be confusing, hence the name "Agenda".
 * [source,java]
 * --
 *     // create Agenda
 *     Agenda agenda = new Agenda();
 *
 *     // add an appointment
 *     agenda.appointments().addAll(
 *         new Agenda.AppointmentImplLocal()
 *             .withStartLocalDateTime(LocalDate.now().atTime(4, 00))
 *             .withEndLocalDateTime(LocalDate.now().atTime(15, 30))
 *             .withDescription("It's time")
 *             .withAppointmentGroup(new Agenda.AppointmentGroupImpl().withStyleClass("group1")) // you should use a map of AppointmentGroups
 *     );
 *           
 *    // show it
 *    primaryStage.setScene(new Scene(agenda, 800, 600));
 *    primaryStage.show();
 * --
 * 
 * == Properties:
 * 
 * - appointments: a list of instances of the Appointment interface, to be rendered by Agenda.
 * - appointmentGroups: a list of AppointmentGroup implementations, for grouping appointments together (e.g. render in the same color).
 * - displayedLocalDateTime: the moment in time that is rendered, depending on the active skin this can be different time frames (day, week, etc).
 * - locale: used to determine the first-day-of-week and week day labels.
 * - allowDragging: allow appointments to be dragged by the mouse.
 * - allowResize: allow appointments to be resized using the mouse.
 * - selectedAppointments: appointments that are selected.
 *  
 * == Appointments
 * The control has a list of appointments (classes implementing the Agenda.Appointment interface) and a LocalDateTime (date) that should be displayed.
 * The appropriate appointments for the to-be-displayed time (based on the currently active skin) will be rendered.
 * The coder could provide all appointments in one big list, but that probably will be a memory heavy.
 * The better approach is to register to the localDateTimeRangeCallbackProperty, and update the appointment collection to match the time frame.
 * 
 * Agenda.Appointment can be provided in three ways:
 * 
 * - through Calendar values (by implementing the *StartTime methods in Appointment or use the Agenda.AppointmentImpl class)
 * - through ZonedDateTime values (by implementing the *ZonedDateTime methods in Appointment or use the Agenda.AppointmentImplZoned class)
 * - through LocalDateTime values (by implementing the *LocalDateTime methods in Appointment or use the Agenda.AppointmentImplLocal class)
 * 
 * == Calendar, LocalDateTime or ZonedDateTime
 * Agenda uses LocalDateTime for render the appointments.
 * Since Agenda must display all appointments in the same "time scale", it simply is not possible to mix different time zones in one view.
 * Google Calendar does this by converting everything to UTC (Coordinated Universal Time, previously know as Greenwich Mean time, or GMT), Agenda does this by converting Calendars to ZonedDateTimes, and ZonedDateTimes to LocalDateTimes.
 * The code used to convert ZonedDateTime to LocalDateTime is somewhat crude and possibly too simplistic, you may want to provide more intelligent conversion by overriding the default methods in the Appointment interface.
 * But you can provide the data in any of the three types, just as long as you understand that LocalDateTime is what is used to render, and what is communicated by Agenda.
 *
 * == AppointmentGroup
 * Each appointment should have an appointment group, this is a class implementing the Agenda.AppointmentGroup interface (Agenda.AppointmentGroupImpl is available).
 * The most important thing this class provides is a style class name which takes care of the rendering (color) of all appointments in that group.
 * Per default agenda has style classes defined, called group0 .. group23. 
 * 
 * == Callbacks
 * 
 * === Action callback
 * The actionCallback property is called when the 'action' is executed on a appointment.
 * Per default this is a double click.
 * [source,java]
 * --
 *      agenda.actionCallback().set( (appointment) -> {
 *           System.out.println("Action was triggered on " + appointment.getDescription());
 *       });
 * --
 * 
 * === Adding appointments using the mouse
 * A new appointment can be added by click-and-drag, but only if a createAppointmentCallbackProperty() is set. 
 * This method should return a new appointment object, for example:
 * [source,java]
 * --
 *      agenda.newAppointmentCallbackProperty().set( (localDateTimeRange) -> {
 *           return new Agenda.AppointmentImplLocal()
 *                   .withStartLocalDateTime(localDateTimeRange.getStartLocalDateTime())
 *                   .withEndLocalDateTime(localDateTimeRange.getEndLocalDateTime())
 *                   .withAppointmentGroup(new Agenda.AppointmentGroupImpl().withStyleClass("group1")); // it is better to have a map of appointment groups to get from
 *       });
 * --
 * 
 * === Editing appointments
 * Agenda has a default popup the allows the primarty properties of appointments to be edited, but maybe you want to do something yourself.
 * If so, you need to register to the editAppointmentCallback, and open your own popup.
 * Because Agenda does not dictate an event/callback mechanism in the implementation of Appointment, it has no way of being informed of changes on the appointment.
 * So when the custom edit is done, make sure that control gets updated (if this does not happen automatically through any of the existing listeners) by calling refresh(). 
 *
 * == Usage
 * The three steps to get Agenda up and running completely:
 * 
 * 1. Implement the localDateTimeRangeCallback and populate the appointments list with the appropriate appointments from your domain.
 * 2. Implement the newAppointmentCallback and create a new appointment, possibly saving the in your domain.
 * 3. Monitor the appointments list for removals and remove them in your domain.
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
		// pref size
		setPrefSize(1000, 800);
		
		// setup the CSS
		this.getStyleClass().add(Agenda.class.getSimpleName());
		
		// handle deprecated
		DateTimeToCalendarHelper.syncLocalDateTime(displayedCalendarObjectProperty, displayedLocalDateTimeObjectProperty, localeObjectProperty);

		// appointmentGroups
		constructAppointmentGroups();
		
		// appointments
		constructAppointments();
	}

	/**
	 * Return the path to the CSS file so things are setup right
	 */
	@Override public String getUserAgentStylesheet()
	{
		return Agenda.class.getResource("/jfxtras/internal/scene/control/skin/agenda/" + Agenda.class.getSimpleName() + ".css").toExternalForm();
	}
	
	/**
	 * 
	 */
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
	private void constructAppointmentGroups() {
		// setup appointment groups as predefined in the CSS
        final Map<String, Agenda.AppointmentGroup> lAppointmentGroupMap = new TreeMap<String, Agenda.AppointmentGroup>();
        for (int i = 0; i < 24; i++) {
        	lAppointmentGroupMap.put("group" + (i < 10 ? "0" : "") + i, new Agenda.AppointmentGroupImpl().withStyleClass("group" + i));
        }
        for (String lId : lAppointmentGroupMap.keySet())
        {
            Agenda.AppointmentGroup lAppointmentGroup = lAppointmentGroupMap.get(lId);
            lAppointmentGroup.setDescription(lId);
            appointmentGroups().add(lAppointmentGroup);
        }
	}

	/** Locale: the locale is used to determine first-day-of-week, weekday labels, etc */
	public ObjectProperty<Locale> localeProperty() { return localeObjectProperty; }
	final private ObjectProperty<Locale> localeObjectProperty = new SimpleObjectProperty<Locale>(this, "locale", Locale.getDefault());
	public Locale getLocale() { return localeObjectProperty.getValue(); }
	public void setLocale(Locale value) { localeObjectProperty.setValue(value); }
	public Agenda withLocale(Locale value) { setLocale(value); return this; } 

	/** AllowDragging: allow appointments being dragged by the mouse */
	public SimpleBooleanProperty allowDraggingProperty() { return allowDraggingObjectProperty; }
	final private SimpleBooleanProperty allowDraggingObjectProperty = new SimpleBooleanProperty(this, "allowDragging", true);
	public boolean getAllowDragging() { return allowDraggingObjectProperty.getValue(); }
	public void setAllowDragging(boolean value) { allowDraggingObjectProperty.setValue(value); }
	public Agenda withAllowDragging(boolean value) { setAllowDragging(value); return this; } 

	/** AllowResize: allow appointments to be resized using the mouse */
	public SimpleBooleanProperty allowResizeProperty() { return allowResizeObjectProperty; }
	final private SimpleBooleanProperty allowResizeObjectProperty = new SimpleBooleanProperty(this, "allowResize", true);
	public boolean getAllowResize() { return allowResizeObjectProperty.getValue(); }
	public void setAllowResize(boolean value) { allowResizeObjectProperty.setValue(value); }
	public Agenda withAllowResize(boolean value) { setAllowResize(value); return this; } 

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
	 * The skin will use this date and time to determine what to display.
	 * Each skin determines what interval suites best; for example the week skin will find the week where this LocalDateTime falls in using the Locale to decide on what day a week starts, the day skin will render the date.
	 * Possibly in the future there may be skins that render part of a day, that simply is not known, hence this is a LocalDateTime instead of a LocalDate. 
	 */
	public ObjectProperty<LocalDateTime> displayedLocalDateTime() { return displayedLocalDateTimeObjectProperty; }
	private final ObjectProperty<LocalDateTime> displayedLocalDateTimeObjectProperty = new SimpleObjectProperty<LocalDateTime>(this, "displayedLocalDateTime", LocalDateTime.now());
	public LocalDateTime getDisplayedLocalDateTime() { return displayedLocalDateTimeObjectProperty.getValue(); }
	public void setDisplayedLocalDateTime(LocalDateTime value) { displayedLocalDateTimeObjectProperty.setValue(value); }
	public Agenda withDisplayedLocalDateTime(LocalDateTime value) { setDisplayedLocalDateTime(value); return this; }
	
	/** selectedAppointments: a list of selected appointments */
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
	
	/** localDateTimeRangeCallback: 
	 * Appointments should match:
	 * - start date &gt;= range start
	 * - end date &lt;= range end
	 */
	public ObjectProperty<Callback<LocalDateTimeRange, Void>> localDateTimeRangeCallbackProperty() { return localDateTimeRangeCallbackObjectProperty; }
	final private ObjectProperty<Callback<LocalDateTimeRange, Void>> localDateTimeRangeCallbackObjectProperty = new SimpleObjectProperty<Callback<LocalDateTimeRange, Void>>(this, "localDateTimeRangeCallback", null);
	public Callback<LocalDateTimeRange, Void> getLocalDateTimeRangeCallback() { return this.localDateTimeRangeCallbackObjectProperty.getValue(); }
	public void setLocalDateTimeRangeCallback(Callback<LocalDateTimeRange, Void> value) { this.localDateTimeRangeCallbackObjectProperty.setValue(value); }
	public Agenda withLocalDateTimeRangeCallback(Callback<LocalDateTimeRange, Void> value) { setLocalDateTimeRangeCallback(value); return this; }
	
	/** addAppointmentCallback:
	 * Since the Agenda is not the owner of the appointments but only dictates an interface, it does not know how to create a new one.
	 * So you need to implement this callback and create an appointment.
	 * The calendars in the provided range specify the start and end times, they can be used to create the new appointment (they do not need to be cloned).
	 * Null may be returned to indicate that no appointment was created.
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
	 */
	public ObjectProperty<Callback<LocalDateTimeRange, Appointment>> newAppointmentCallbackProperty() { return newAppointmentCallbackObjectProperty; }
	final private ObjectProperty<Callback<LocalDateTimeRange, Appointment>> newAppointmentCallbackObjectProperty = new SimpleObjectProperty<Callback<LocalDateTimeRange, Appointment>>(this, "newAppointmentCallback", null);
	public Callback<LocalDateTimeRange, Appointment> getNewAppointmentCallback() { return this.newAppointmentCallbackObjectProperty.getValue(); }
	public void setNewAppointmentCallback(Callback<LocalDateTimeRange, Appointment> value) { this.newAppointmentCallbackObjectProperty.setValue(value); }
	public Agenda withNewAppointmentCallback(Callback<LocalDateTimeRange, Appointment> value) { setNewAppointmentCallback(value); return this; }

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

    /** appointmentChangedCallback:
     * When an appointment is changed by Agenda (e.g. drag-n-drop to new time) change listeners will not fire.
     * To enable the client to process those changes this callback can be used.  Additionally, for a repeatable
     * appointment, this can be used to prompt the user if they want the change to occur to one, this-and-future
     * or all events in series.
     */
    public ObjectProperty<Callback<Appointment, Void>> appointmentChangedCallbackProperty() { return appointmentChangedCallbackObjectProperty; }
    final private ObjectProperty<Callback<Appointment, Void>> appointmentChangedCallbackObjectProperty = new SimpleObjectProperty<Callback<Appointment, Void>>(this, "appointmentChangedCallback", null);
    public Callback<Appointment, Void> getAppointmentChangedCallback() { return this.appointmentChangedCallbackObjectProperty.getValue(); }
    public void setAppointmentChangedCallback(Callback<Appointment, Void> value) { this.appointmentChangedCallbackObjectProperty.setValue(value); }
    public Agenda withAppointmentChangedCallback(Callback<Appointment, Void> value) { setAppointmentChangedCallback(value); return this; }

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
	static public class LocalDateTimeRange
	{
		public LocalDateTimeRange(LocalDateTime start, LocalDateTime end)
		{
			this.start = start;
			this.end = end;
		}
		
		public LocalDateTime getStartLocalDateTime() { return start; }
		final LocalDateTime start;
		
		public LocalDateTime getEndLocalDateTime() { return end; }
		final LocalDateTime end; 
		
		@Override
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
	
	/**
	 * Stores date-time as a Temporal.  This allows any class implementing Temporal, that can
	 * be converted to a LocalDate or LocalDateTime, to be displayed in Agenda.
	 *
	 */
	public interface Appointment
	{
	    Boolean isWholeDay();
	    void setWholeDay(Boolean b);
	    
	    String getSummary();
	    void setSummary(String s);
	    
	    String getDescription();
	    void setDescription(String s);
	    
	    String getLocation();
	    void setLocation(String s);
	    
	    AppointmentGroup getAppointmentGroup();
	    void setAppointmentGroup(AppointmentGroup s);
	    
	    // ----
	    // Calendar
	    
	    /** This method is not used by the control, it can only be called when implemented by the user through the default Datetime methods on this interface **/  
	    default Calendar getStartTime() {
	        throw new RuntimeException("Not implemented");
	    }
	    /** This method is not used by the control, it can only be called when implemented by the user through the default Datetime methods on this interface **/  
	    default void setStartTime(Calendar c) {
	        throw new RuntimeException("Not implemented");
	    }
	    
	    /** This method is not used by the control, it can only be called when implemented by the user through the default Datetime methods on this interface **/  
	    default Calendar getEndTime() {
	        throw new RuntimeException("Not implemented");
	    }
	    /** This method is not used by the control, it can only be called when implemented by the user through the default Datetime methods on this interface **/  
	    default void setEndTime(Calendar c) {
	        throw new RuntimeException("Not implemented");
	    }
	    
	    /*
	     * Temporal - backs LocalDateTime
	     * If implementing Temporal instead of Calendar, override these default methods.
	     */
	    default Temporal getStartTemporal() {
	        throw new RuntimeException("Not implemented");
	    }
	    
	    default void setStartTemporal(Temporal t) {
	        throw new RuntimeException("Not implemented");
	    }
	    
	    default Temporal getEndTemporal() {
	        throw new RuntimeException("Not implemented");
	    }
	    
	    default void setEndTemporal(Temporal t) {
	        throw new RuntimeException("Not implemented");
	    }
	    
	    // ----
	    // LocalDateTime
	    // If implementing Temporal instead of Calendar, override these default methods.
	    
	    /** This is what Agenda uses to render the appointments */
	    default LocalDateTime getStartLocalDateTime() {
	        return LocalDateTime.ofInstant(getStartTime().toInstant(), ZoneId.systemDefault());
	    }
	    /** This is what Agenda uses to render the appointments */
	    default void setStartLocalDateTime(LocalDateTime v) {
	        setStartTime(GregorianCalendar.from(v.atZone(ZoneId.systemDefault())));
	    }
	    
	    /** This is what Agenda uses to render the appointments */
	    default LocalDateTime getEndLocalDateTime() {
	        return (getEndTime() == null) ? null : LocalDateTime.ofInstant(getEndTime().toInstant(), ZoneId.systemDefault());
	    }
	    
	    /** End is exclusive */
	    default void setEndLocalDateTime(LocalDateTime v) {
	        setEndTime(GregorianCalendar.from(v.atZone(ZoneId.systemDefault())));
	    }
	}
	
	static public abstract class AppointmentImplBase<T> implements Appointment
	{
	    /** WholeDay: */
	    public ObjectProperty<Boolean> wholeDayProperty() { return wholeDayObjectProperty; }
	    final private ObjectProperty<Boolean> wholeDayObjectProperty = new SimpleObjectProperty<Boolean>(this, "wholeDay", false);
	    @Override public Boolean isWholeDay() { return wholeDayObjectProperty.getValue(); }
	    @Override public void setWholeDay(Boolean value) { wholeDayObjectProperty.setValue(value); }
	    public T withWholeDay(Boolean value) { setWholeDay(value); return (T)this; } 
	    
	    /** Summary: */
	    public ObjectProperty<String> summaryProperty() { return summaryObjectProperty; }
	    final private ObjectProperty<String> summaryObjectProperty = new SimpleObjectProperty<String>(this, "summary");
	    @Override public String getSummary() { return summaryObjectProperty.getValue(); }
	    @Override public void setSummary(String value) { summaryObjectProperty.setValue(value); }
	    public T withSummary(String value) { setSummary(value); return (T)this; } 
	    
	    /** Description: */
	    public ObjectProperty<String> descriptionProperty() { return descriptionObjectProperty; }
	    final private ObjectProperty<String> descriptionObjectProperty = new SimpleObjectProperty<String>(this, "description");
	    @Override public String getDescription() { return descriptionObjectProperty.getValue(); }
	    @Override public void setDescription(String value) { descriptionObjectProperty.setValue(value); }
	    public T withDescription(String value) { setDescription(value); return (T)this; } 
	    
	    /** Location: */
	    public ObjectProperty<String> locationProperty() { return locationObjectProperty; }
	    final private ObjectProperty<String> locationObjectProperty = new SimpleObjectProperty<String>(this, "location");
	    @Override public String getLocation() { return locationObjectProperty.getValue(); }
	    @Override public void setLocation(String value) { locationObjectProperty.setValue(value); }
	    public T withLocation(String value) { setLocation(value); return (T)this; } 
	    
	    /** AppointmentGroup: */
	    public ObjectProperty<AppointmentGroup> appointmentGroupProperty() { return appointmentGroupObjectProperty; }
	    final private ObjectProperty<AppointmentGroup> appointmentGroupObjectProperty = new SimpleObjectProperty<AppointmentGroup>(this, "appointmentGroup");
	    @Override public AppointmentGroup getAppointmentGroup() { return appointmentGroupObjectProperty.getValue(); }
	    @Override public void setAppointmentGroup(AppointmentGroup value) { appointmentGroupObjectProperty.setValue(value); }
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
        @Override
        public Calendar getStartTime() { return startTimeObjectProperty.getValue(); }
        @Override
        public void setStartTime(Calendar value) { startTimeObjectProperty.setValue(value); }
        public AppointmentImpl withStartTime(Calendar value) { setStartTime(value); return this; }
        
        /** EndTime: */
        public ObjectProperty<Calendar> endTimeProperty() { return endTimeObjectProperty; }
        final private ObjectProperty<Calendar> endTimeObjectProperty = new SimpleObjectProperty<Calendar>(this, "endTime");
        @Override
        public Calendar getEndTime() { return endTimeObjectProperty.getValue(); }
        @Override
        public void setEndTime(Calendar value) { endTimeObjectProperty.setValue(value); }
        public AppointmentImpl withEndTime(Calendar value) { setEndTime(value); return this; } 
        
        @Override
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
    static public class AppointmentImplLocal extends AppointmentImplBase<AppointmentImplLocal> 
    implements Appointment
    {
        /** StartDateTime: */
        public ObjectProperty<LocalDateTime> startLocalDateTime() { return startLocalDateTime; }
        final private ObjectProperty<LocalDateTime> startLocalDateTime = new SimpleObjectProperty<LocalDateTime>(this, "startLocalDateTime");
        @Override
        public LocalDateTime getStartLocalDateTime() { return startLocalDateTime.getValue(); }
        @Override
        public void setStartLocalDateTime(LocalDateTime value) { startLocalDateTime.setValue(value); }
        public AppointmentImplLocal withStartLocalDateTime(LocalDateTime value) { setStartLocalDateTime(value); return this; }
        
        /** EndDateTime: */
        public ObjectProperty<LocalDateTime> endLocalDateTimeProperty() { return endLocalDateTimeProperty; }
        final private ObjectProperty<LocalDateTime> endLocalDateTimeProperty = new SimpleObjectProperty<LocalDateTime>(this, "endLocalDateTimeProperty");
        @Override
        public LocalDateTime getEndLocalDateTime() { return endLocalDateTimeProperty.getValue(); }
        @Override
        public void setEndLocalDateTime(LocalDateTime value) { endLocalDateTimeProperty.setValue(value); }
        public AppointmentImplLocal withEndLocalDateTime(LocalDateTime value) { setEndLocalDateTime(value); return this; } 
        
        @Override
        public String toString()
        {
            return super.toString()
                 + ", "
                 + this.getStartLocalDateTime()
                 + " - "
                 + this.getEndLocalDateTime()
                 ;
        }
    }
	
    /**
     * A class to help you get going using Temporal (such as LocalDate, LocalDateTime, ZonedDateTime, Instant, etc)
     * all the required methods of the interface are implemented as JavaFX properties 
     */
    static public class AppointmentImplTemporal extends AppointmentImplBase<AppointmentImplTemporal> 
    implements Appointment
    {
        /** StartDateTime: Temporal */
        public ObjectProperty<Temporal> startTemporal() { return startTemporalProperty; }
        final private ObjectProperty<Temporal> startTemporalProperty = new SimpleObjectProperty<>(this, "startTemporal");
        @Override public Temporal getStartTemporal() { return startTemporalProperty.getValue(); }
        @Override public void setStartTemporal(Temporal value) { startTemporalProperty.setValue(value); }
        public AppointmentImplTemporal withStartTemporal(Temporal value) { setStartTemporal(value); return this; }
        
        /** StartDateTime: LocalDateTime */
        @Override public LocalDateTime getStartLocalDateTime() { return TemporalUtilities.toLocalDateTime(getStartTemporal()); } //return makeLocalDateTime(getStartTemporal()); }
        @Override public void setStartLocalDateTime(LocalDateTime value) {
            TemporalAdjuster adjuster = (isWholeDay()) ? LocalDate.from(value) : value;
            Temporal start = (getStartTemporal() == null) ? value : TemporalUtilities.combine(getStartTemporal(), adjuster);
            setStartTemporal(start);
        }
        public AppointmentImplTemporal withStartLocalDateTime(LocalDateTime value) { setStartLocalDateTime(value); return this; }
        
        /** EndDateTime: Temporal */
        public ObjectProperty<Temporal> endTemporal() { return endTemporalProperty; }
        final private ObjectProperty<Temporal> endTemporalProperty = new SimpleObjectProperty<>(this, "endTemporal");
        @Override public Temporal getEndTemporal() { return endTemporalProperty.getValue(); }
        @Override public void setEndTemporal(Temporal value) { endTemporalProperty.setValue(value); }
        public AppointmentImplTemporal withEndTemporal(Temporal value) { setEndTemporal(value); return this; }
        
        /** EndDateTime: LocalDateTime */
        @Override public LocalDateTime getEndLocalDateTime() { return TemporalUtilities.toLocalDateTime(getEndTemporal()); }
        @Override public void setEndLocalDateTime(LocalDateTime value) {
            TemporalAdjuster adjuster = (isWholeDay()) ? LocalDate.from(value) : value;
            Temporal end = (getEndTemporal() == null) ? value : TemporalUtilities.combine(getEndTemporal(), adjuster);
            setEndTemporal(end);
        }
        public AppointmentImplTemporal withEndLocalDateTime(LocalDateTime value) { setEndLocalDateTime(value); return this; } 
        
        @Override
        public String toString()
        {
            return super.toString()
                 + ", "
                 + this.getStartTemporal()
                 + " - "
                 + this.getEndTemporal()
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
		@Override
        public String getDescription() { return descriptionObjectProperty.getValue(); }
		@Override
        public void setDescription(String value) { descriptionObjectProperty.setValue(value); }
		public AppointmentGroupImpl withDescription(String value) { setDescription(value); return this; } 
				
		/** StyleClass: */
		public ObjectProperty<String> styleClassProperty() { return styleClassObjectProperty; }
		final private ObjectProperty<String> styleClassObjectProperty = new SimpleObjectProperty<String>(this, "styleClass");
		@Override
        public String getStyleClass() { return styleClassObjectProperty.getValue(); }
		@Override
        public void setStyleClass(String value) { styleClassObjectProperty.setValue(value); }
		public AppointmentGroupImpl withStyleClass(String value) { setStyleClass(value); return this; }
	}
	
	
	// ==================================================================================================================
	// Print

    /**
     * Prints the current agenda using the given printer job.
     * <p>This method does not modify the state of the job, nor does it call
     * {@link PrinterJob#endJob}, so the job may be safely reused afterwards.
     * 
     * This method is experimental.
     * 
     * @param job printer job used for printing
     */
    public void print(PrinterJob job) {
    	((AgendaSkin)getSkin()).print(job);
    }
}
