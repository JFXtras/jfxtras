/**
 * DateTimeToCalendarHelper.java
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

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/**
 *
 * @author user
 */
public class DateTimeToCalendarHelper {

	/**
	 * 
	 * @param localDate
	 * @return
	 */
	public static Calendar createCalendarFromLocalDate(LocalDate localDate, Locale locale)
	{
		if (localDate == null) return null;
		Calendar lCalendar = Calendar.getInstance(locale);
		lCalendar.set(Calendar.YEAR, localDate.getYear());
		lCalendar.set(Calendar.MONTH, localDate.getMonth().getValue() - 1);
		lCalendar.set(Calendar.DATE, localDate.getDayOfMonth());
		lCalendar.set(Calendar.HOUR_OF_DAY, 0);
		lCalendar.set(Calendar.MINUTE, 0);
		lCalendar.set(Calendar.SECOND, 0);
		lCalendar.set(Calendar.MILLISECOND, 0);
		return lCalendar;
	}
	
	/**
	 * 
	 * @param localDateTime
	 * @return
	 */
	public static Calendar createCalendarFromLocalDateTime(LocalDateTime localDateTime, Locale locale)
	{
		if (localDateTime == null) return null;
		Calendar lCalendar = Calendar.getInstance(locale);
		lCalendar.set(Calendar.YEAR, localDateTime.getYear());
		lCalendar.set(Calendar.MONTH, localDateTime.getMonth().getValue() - 1);
		lCalendar.set(Calendar.DATE, localDateTime.getDayOfMonth());
		lCalendar.set(Calendar.HOUR_OF_DAY, localDateTime.getHour());
		lCalendar.set(Calendar.MINUTE, localDateTime.getMinute());
		lCalendar.set(Calendar.SECOND, localDateTime.getSecond());
		lCalendar.set(Calendar.MILLISECOND, localDateTime.getNano() / 1000000);
		return lCalendar;
	}
	
	/**
	 * 
	 * @param localTime
	 * @return
	 */
	public static Calendar createCalendarFromLocalTime(LocalTime localTime, Locale locale)
	{
		if (localTime == null) return null;
		Calendar lCalendar = Calendar.getInstance(locale);
		lCalendar.set(Calendar.HOUR_OF_DAY, localTime.getHour());
		lCalendar.set(Calendar.MINUTE, localTime.getMinute());
		lCalendar.set(Calendar.SECOND, localTime.getSecond());
		lCalendar.set(Calendar.MILLISECOND, localTime.getNano() / 1000000);
		return lCalendar;
	}
	
	/**
	 * 
	 * @param calendar
	 * @return
	 */
	public static LocalDate createLocalDateFromCalendar(Calendar calendar)
	{
		if (calendar == null) return null;
		LocalDate lLocalDate = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE));
		return lLocalDate;
	}
	
	/**
	 * 
	 * @param calendar
	 * @return
	 */
	public static LocalDateTime createLocalDateTimeFromCalendar(Calendar calendar)
	{
		if (calendar == null) return null;
		LocalDateTime lLocalDateTime = LocalDateTime.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), calendar.get(Calendar.MILLISECOND) * 1000000);
		return lLocalDateTime;
	}
	
	/**
	 * 
	 * @param calendar
	 * @return
	 */
	public static LocalTime createLocalTimeFromCalendar(Calendar calendar)
	{
		if (calendar == null) return null;
		LocalTime lLocalTime = LocalTime.of(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), calendar.get(Calendar.MILLISECOND) * 1000000);
		return lLocalTime;
	}
	
	/*
	 *
	 */
	public static Date createDateFromLocalDate(LocalDate localDate) {
		if (localDate == null) {
			return null;
		}
		Instant lInstant = localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
		Date lDate = Date.from(lInstant);
		return lDate;
	}
	
	/**
	 * 
	 */
	public static LocalDate createLocaleDateFromDate(Date date) {
		if (date == null) {
			return null;
		}
		LocalDate lLocalDate = LocalDateTime.ofInstant( date.toInstant(), ZoneId.systemDefault() ).toLocalDate();
		return lLocalDate;
	}
	
	/*
	 *
	 */
	public static Date createDateFromLocalDateTime(LocalDateTime localDateTime) {
		if (localDateTime == null) {
			return null;
		}
		Instant lInstant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
		Date lDate = Date.from(lInstant);
		return lDate;
	}
	
	/**
	 * 
	 */
	public static LocalDateTime createLocaleDateTimeFromDate(Date date) {
		if (date == null) {
			return null;
		}
		// This gives strange results, for example: Sun Jan 01 02:02:02 CET 1111 -> 1111-01-08T01:55:30
		// LocalDateTime lLocalDateTime = LocalDateTime.ofInstant( date.toInstant(), ZoneId.systemDefault() );
		LocalDateTime lLocalDateTime = LocalDateTime.of(1900 + date.getYear(), date.getMonth() + 1, date.getDate(), date.getHours(), date.getMinutes(), date.getSeconds());
		return lLocalDateTime;
	}
	
	/**
	 * 
	 */
	public static LocalTime createLocaleTimeFromDate(Date date) {
		if (date == null) {
			return null;
		}
		LocalTime lLocalTime = LocalTime.of( date.getHours(), date.getMinutes(), date.getSeconds(), 0 );
		return lLocalTime;
	}

	/*
	 *
	 */
	public static Date createDateFromLocalTime(LocalTime localTime) {
		if (localTime == null) {
			return null;
		}
		Date lDate = createDateFromLocalDateTime( localTime.atDate(LocalDate.now()) );
		return lDate;
	}
	

	/**
	 * 
	 * @param calendarProperty
	 * @param localDateProperty
	 * @param localeProperty 
	 */
	static public void syncLocalDate(ObjectProperty<Calendar> calendarProperty, ObjectProperty<LocalDate> localDateProperty, ObjectProperty<Locale> localeProperty)
	{
		// initial
		calendarProperty.set(localDateProperty.get() == null ? null : createCalendarFromLocalDate(localDateProperty.get(), localeProperty.get()));
		
		// forward changes from calendar
		calendarProperty.addListener( (ObservableValue<? extends Calendar> observableValue, Calendar oldValue, Calendar newValue) -> {
			localDateProperty.set(createLocalDateFromCalendar(newValue)); 
		});
		
		// forward changes to calendar
		localDateProperty.addListener( (ObservableValue<? extends LocalDate> observableValue, LocalDate oldValue, LocalDate newValue) -> {
			calendarProperty.set(newValue == null ? null : createCalendarFromLocalDate(newValue, localeProperty.get()));
		});
	}

	/**
	 * 
	 * @param calendarProperty
	 * @param localDateTimeProperty
	 * @param localeProperty 
	 */
	static public void syncLocalDateTime(ObjectProperty<Calendar> calendarProperty, ObjectProperty<LocalDateTime> localDateTimeProperty, ObjectProperty<Locale> localeProperty)
	{
		// initial
		calendarProperty.set(localDateTimeProperty.get() == null ? null : createCalendarFromLocalDateTime(localDateTimeProperty.get(), localeProperty.get()));
		
		// forward changes from calendar
		calendarProperty.addListener( (ObservableValue<? extends Calendar> observableValue, Calendar oldValue, Calendar newValue) -> {
			localDateTimeProperty.set(createLocalDateTimeFromCalendar(newValue)); 
		});
		
		// forward changes to calendar
		localDateTimeProperty.addListener( (ObservableValue<? extends LocalDateTime> observableValue, LocalDateTime oldValue, LocalDateTime newValue) -> {
			calendarProperty.set(newValue == null ? null : createCalendarFromLocalDateTime(newValue, localeProperty.get()));
		});
	}


	/**
	 * 
	 * @param calendarProperty
	 * @param localTimeProperty
	 * @param localeProperty 
	 */
	static public void syncLocalTime(ObjectProperty<Calendar> calendarProperty, ObjectProperty<LocalTime> localTimeProperty, ObjectProperty<Locale> localeProperty)
	{
		// initial
		calendarProperty.set(localTimeProperty.get() == null ? null : createCalendarFromLocalTime(localTimeProperty.get(), localeProperty.get()));
		
		// forward changes from calendar
		calendarProperty.addListener( (ObservableValue<? extends Calendar> observableValue, Calendar oldValue, Calendar newValue) -> {
			localTimeProperty.set(createLocalTimeFromCalendar(newValue)); 
		});
		
		// forward changes to calendar
		localTimeProperty.addListener( (ObservableValue<? extends LocalTime> observableValue, LocalTime oldValue, LocalTime newValue) -> {
			calendarProperty.set(newValue == null ? null : createCalendarFromLocalTime(newValue, localeProperty.get()));
		});
	}
	
	/**
	 * 
	 * @param calendars
	 * @param localDates
	 * @param localeProperty 
	 */
	static public void syncLocalDates(ObservableList<Calendar> calendars, ObservableList<LocalDate> localDates, ObjectProperty<Locale> localeProperty)
	{
		// initial values
		for (LocalDate lLocalDate : localDates) {
			Calendar lCalendar = createCalendarFromLocalDate(lLocalDate, localeProperty.get());
			calendars.add(lCalendar);
		}
		
		// forward changes from calendar
		calendars.addListener( (ListChangeListener.Change<? extends Calendar> change) -> {
			while (change.next())
			{
				for (Calendar lCalendar : change.getRemoved())
				{
					LocalDate lLocalDate = createLocalDateFromCalendar(lCalendar);
					if (localDates.contains(lLocalDate)) {
						localDates.remove(lLocalDate);
					}				
				}
				for (Calendar lCalendar : change.getAddedSubList()) 
				{
					LocalDate lLocalDate = createLocalDateFromCalendar(lCalendar);
					if (localDates.contains(lLocalDate) == false) {
						localDates.add(lLocalDate);
					}
				}
			}
		});
		
		// forward changes to calendar
		localDates.addListener( (ListChangeListener.Change<? extends LocalDate> change) -> {
			while (change.next()) {
				for (LocalDate lLocalDate : change.getRemoved()) {
					Calendar lCalendar = createCalendarFromLocalDate(lLocalDate, localeProperty.get());
					if (calendars.contains(lCalendar)) {
						calendars.remove(lCalendar);
					}
				}
				for (LocalDate lLocalDate : change.getAddedSubList()) {
					Calendar lCalendar = createCalendarFromLocalDate(lLocalDate, localeProperty.get());
					if (calendars.contains(lCalendar) == false) {
						calendars.add(lCalendar);
					}
				}
			}
		});
	}
	
	/**
	 * 
	 * @param calendars
	 * @param localDateTimes
	 * @param localeProperty 
	 */
	static public void syncLocalDateTimes(ObservableList<Calendar> calendars, ObservableList<LocalDateTime> localDateTimes, ObjectProperty<Locale> localeProperty)
	{
		// initial values
		for (LocalDateTime lLocalDateTime : localDateTimes) {
			Calendar lCalendar = createCalendarFromLocalDateTime(lLocalDateTime, localeProperty.get());
			calendars.add(lCalendar);
		}
		
		// forward changes from calendar
		calendars.addListener( (ListChangeListener.Change<? extends Calendar> change) -> {
			while (change.next())
			{
				for (Calendar lCalendar : change.getRemoved())
				{
					LocalDateTime lLocalDateTime = createLocalDateTimeFromCalendar(lCalendar);
					if (localDateTimes.contains(lLocalDateTime)) {
						localDateTimes.remove(lLocalDateTime);
					}				
				}
				for (Calendar lCalendar : change.getAddedSubList()) 
				{
					LocalDateTime lLocalDateTime = createLocalDateTimeFromCalendar(lCalendar);
					if (localDateTimes.contains(lLocalDateTime) == false) {
						localDateTimes.add(lLocalDateTime);
					}
				}
			}
		});
		
		// forward changes to calendar
		localDateTimes.addListener( (ListChangeListener.Change<? extends LocalDateTime> change) -> {
			while (change.next()) {
				for (LocalDateTime lLocalDateTime : change.getRemoved()) {
					Calendar lCalendar = createCalendarFromLocalDateTime(lLocalDateTime, localeProperty.get());
					if (calendars.contains(lCalendar)) {
						calendars.remove(lCalendar);
					}
				}
				for (LocalDateTime lLocalDateTime : change.getAddedSubList()) {
					Calendar lCalendar = createCalendarFromLocalDateTime(lLocalDateTime, localeProperty.get());
					if (calendars.contains(lCalendar) == false) {
						calendars.add(lCalendar);
					}
				}
			}
		});
	}

	// -------
	// DateTimeFormatterForDate
	
	static public void syncDateTimeFormatterForDate(ObjectProperty<DateFormat> dateFormatProperty, ObjectProperty<DateTimeFormatter> dateTimeFormatterProperty) {
		dateFormatProperty.set( dateTimeFormatterProperty.get() == null ? null : new DateTimeFormatterToDateFormatWrapper( dateTimeFormatterProperty.get() ));
		dateTimeFormatterProperty.addListener( (observable) -> {
			dateFormatProperty.set( new DateTimeFormatterToDateFormatWrapper( dateTimeFormatterProperty.get() ));
		});
	}
	
	static public void syncDateTimeFormattersForDate(ListProperty<DateFormat> dateFormatsProperty, ListProperty<DateTimeFormatter> dateTimeFormattersProperty) {
		final Map<DateTimeFormatter, DateTimeFormatterToDateFormatWrapper> map = new WeakHashMap<>();
		
		// initial values
		for (DateTimeFormatter lDateTimeFormatter : dateTimeFormattersProperty) {
			DateTimeFormatterToDateFormatWrapper lDateTimeFormatterToDateFormatWrapper = new DateTimeFormatterToDateFormatWrapper( lDateTimeFormatter );
			map.put(lDateTimeFormatter, lDateTimeFormatterToDateFormatWrapper);
			dateFormatsProperty.add( lDateTimeFormatterToDateFormatWrapper );
		}
		
		// forward changes from localDate
		dateTimeFormattersProperty.addListener( (ListChangeListener.Change<? extends DateTimeFormatter> change) -> {
			while (change.next())
			{
				for (DateTimeFormatter lDateTimeFormatter : change.getRemoved())
				{
					DateTimeFormatterToDateFormatWrapper lDateTimeFormatterToDateFormatWrapper = map.remove(lDateTimeFormatter);
					dateFormatsProperty.remove(lDateTimeFormatterToDateFormatWrapper);
				}
				for (DateTimeFormatter lDateTimeFormatter : change.getAddedSubList()) 
				{
					DateTimeFormatterToDateFormatWrapper lDateTimeFormatterToDateFormatWrapper = new DateTimeFormatterToDateFormatWrapper( lDateTimeFormatter );
					map.put(lDateTimeFormatter, lDateTimeFormatterToDateFormatWrapper);
					dateFormatsProperty.add(lDateTimeFormatterToDateFormatWrapper);
				}
			}
		});
	}

	/**
	 * 
	 */
	static class DateTimeFormatterToDateFormatWrapper extends DateFormat {

		public DateTimeFormatterToDateFormatWrapper(DateTimeFormatter dateTimeFormatter) {
			this.dateTimeFormatter = dateTimeFormatter;
		}
		final private DateTimeFormatter dateTimeFormatter;
		
		@Override
		public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
			LocalDate lLocalDate = createLocaleDateFromDate(date);
			String s = this.dateTimeFormatter.format( lLocalDate );
			toAppendTo.append(s);
			return toAppendTo;
		}

		@Override
		public Date parse(String source, ParsePosition pos) {
			LocalDate lLocalDate = LocalDate.parse(source, this.dateTimeFormatter);
			Date lDate = createDateFromLocalDate(lLocalDate);
			pos.setIndex(source.length()); // otherwise DateFormat will thrown an exception 
			return lDate;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			return (this == obj);
		}
		
		public String toString() {
			return dateTimeFormatter.toString();
		}
	}

	// -------
	// DateTimeFormatterForDateTime
	
	static public void syncDateTimeFormatterForDateTime(ObjectProperty<DateFormat> dateFormatProperty, ObjectProperty<DateTimeFormatter> dateTimeFormatterProperty) {
		dateFormatProperty.set( dateTimeFormatterProperty.get() == null ? null : new DateTimeFormatterToDateTimeFormatWrapper( dateTimeFormatterProperty.get() ));
		dateTimeFormatterProperty.addListener( (observable) -> {
			dateFormatProperty.set( new DateTimeFormatterToDateTimeFormatWrapper( dateTimeFormatterProperty.get() ));
		});
	}
	
	static public void syncDateTimeFormattersForDateTime(ListProperty<DateFormat> dateFormatsProperty, ListProperty<DateTimeFormatter> dateTimeFormattersProperty) {
		final Map<DateTimeFormatter, DateTimeFormatterToDateTimeFormatWrapper> map = new WeakHashMap<>();
		
		// initial values
		for (DateTimeFormatter lDateTimeFormatter : dateTimeFormattersProperty) {
			DateTimeFormatterToDateTimeFormatWrapper lDateTimeFormatterToDateTimeFormatWrapper = new DateTimeFormatterToDateTimeFormatWrapper( lDateTimeFormatter );
			map.put(lDateTimeFormatter, lDateTimeFormatterToDateTimeFormatWrapper);
			dateFormatsProperty.add( lDateTimeFormatterToDateTimeFormatWrapper );
		}
		
		// forward changes from localDate
		dateTimeFormattersProperty.addListener( (ListChangeListener.Change<? extends DateTimeFormatter> change) -> {
			while (change.next())
			{
				for (DateTimeFormatter lDateTimeFormatter : change.getRemoved())
				{
					DateTimeFormatterToDateTimeFormatWrapper lDateTimeFormatterToDateTimeFormatWrapper = map.remove(lDateTimeFormatter);
					dateFormatsProperty.remove(lDateTimeFormatterToDateTimeFormatWrapper);
				}
				for (DateTimeFormatter lDateTimeFormatter : change.getAddedSubList()) 
				{
					DateTimeFormatterToDateTimeFormatWrapper lDateTimeFormatterToDateTimeFormatWrapper = new DateTimeFormatterToDateTimeFormatWrapper( lDateTimeFormatter );
					map.put(lDateTimeFormatter, lDateTimeFormatterToDateTimeFormatWrapper);
					dateFormatsProperty.add(lDateTimeFormatterToDateTimeFormatWrapper);
				}
			}
		});
	}

	/**
	 * 
	 */
	static class DateTimeFormatterToDateTimeFormatWrapper extends DateFormat {

		public DateTimeFormatterToDateTimeFormatWrapper(DateTimeFormatter dateTimeFormatter) {
			this.dateTimeFormatter = dateTimeFormatter;
		}
		final private DateTimeFormatter dateTimeFormatter;
		
		@Override
		public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
			LocalDateTime lLocalDateTime = createLocaleDateTimeFromDate(date);
			String s = this.dateTimeFormatter.format( lLocalDateTime );
			toAppendTo.append(s);
			return toAppendTo;
		}

		@Override
		public Date parse(String source, ParsePosition pos) {
			LocalDateTime lLocalDateTime = LocalDateTime.parse(source, this.dateTimeFormatter);
			Date lDate = createDateFromLocalDateTime(lLocalDateTime);
			pos.setIndex(source.length()); // otherwise DateFormat will thrown an exception 
			return lDate;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			return (this == obj);
		}
		
		public String toString() {
			return dateTimeFormatter.toString();
		}
	}


	// -------
	// DateTimeFormatterForTime
	
	static public void syncDateTimeFormatterForTime(ObjectProperty<DateFormat> dateFormatProperty, ObjectProperty<DateTimeFormatter> dateTimeFormatterProperty) {
		dateFormatProperty.set( dateTimeFormatterProperty.get() == null ? null : new DateTimeFormatterToTimeFormatWrapper( dateTimeFormatterProperty.get() ));
		dateTimeFormatterProperty.addListener( (observable) -> {
			dateFormatProperty.set( new DateTimeFormatterToTimeFormatWrapper( dateTimeFormatterProperty.get() ));
		});
	}
	
	static public void syncDateTimeFormattersForTime(ListProperty<DateFormat> dateFormatsProperty, ListProperty<DateTimeFormatter> dateTimeFormattersProperty) {
		final Map<DateTimeFormatter, DateTimeFormatterToTimeFormatWrapper> map = new WeakHashMap<>();
		
		// initial values
		for (DateTimeFormatter lDateTimeFormatter : dateTimeFormattersProperty) {
			DateTimeFormatterToTimeFormatWrapper lDateTimeFormatterToTimeFormatWrapper = new DateTimeFormatterToTimeFormatWrapper( lDateTimeFormatter );
			map.put(lDateTimeFormatter, lDateTimeFormatterToTimeFormatWrapper);
			dateFormatsProperty.add( lDateTimeFormatterToTimeFormatWrapper );
		}
		
		// forward changes from localDate
		dateTimeFormattersProperty.addListener( (ListChangeListener.Change<? extends DateTimeFormatter> change) -> {
			while (change.next())
			{
				for (DateTimeFormatter lDateTimeFormatter : change.getRemoved())
				{
					DateTimeFormatterToTimeFormatWrapper lDateTimeFormatterToTimeFormatWrapper = map.remove(lDateTimeFormatter);
					dateFormatsProperty.remove(lDateTimeFormatterToTimeFormatWrapper);
				}
				for (DateTimeFormatter lDateTimeFormatter : change.getAddedSubList()) 
				{
					DateTimeFormatterToTimeFormatWrapper lDateTimeFormatterToTimeFormatWrapper = new DateTimeFormatterToTimeFormatWrapper( lDateTimeFormatter );
					map.put(lDateTimeFormatter, lDateTimeFormatterToTimeFormatWrapper);
					dateFormatsProperty.add(lDateTimeFormatterToTimeFormatWrapper);
				}
			}
		});
	}

	/**
	 * 
	 */
	static class DateTimeFormatterToTimeFormatWrapper extends DateFormat {

		public DateTimeFormatterToTimeFormatWrapper(DateTimeFormatter dateTimeFormatter) {
			this.dateTimeFormatter = dateTimeFormatter;
		}
		final private DateTimeFormatter dateTimeFormatter;
		
		@Override
		public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
			LocalTime lLocalTime = createLocaleTimeFromDate(date);
			String s = this.dateTimeFormatter.format( lLocalTime );
			toAppendTo.append(s);
			return toAppendTo;
		}

		@Override
		public Date parse(String source, ParsePosition pos) {
			LocalTime lLocalTime = LocalTime.parse(source, this.dateTimeFormatter);
			Date lDate = createDateFromLocalTime(lLocalTime);
			pos.setIndex(source.length()); // otherwise DateFormat will thrown an exception 
			return lDate;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			return (this == obj);
		}
		
		public String toString() {
			return dateTimeFormatter.toString();
		}
	}

	static public void sync(ObservableList<Calendar> calendars, ObservableList<Calendar> localDateTimes)
	{
		// initial values
		for (Calendar lCalendar : localDateTimes) {
			calendars.add(lCalendar);
		}
		
		// forward changes from calendar
		calendars.addListener( (ListChangeListener.Change<? extends Calendar> change) -> {
			while (change.next())
			{
				for (Calendar lCalendar : change.getRemoved())
				{
					if (localDateTimes.contains(lCalendar)) {
						localDateTimes.remove(lCalendar);
					}				
				}
				for (Calendar lCalendar : change.getAddedSubList()) 
				{
					if (localDateTimes.contains(lCalendar) == false) {
						localDateTimes.add(lCalendar);
					}
				}
			}
		});
		
		// forward changes to calendar
		localDateTimes.addListener( (ListChangeListener.Change<? extends Calendar> change) -> {
			while (change.next()) {
				for (Calendar lCalendar : change.getRemoved()) {
					if (calendars.contains(lCalendar)) {
						calendars.remove(lCalendar);
					}
				}
				for (Calendar lCalendar : change.getAddedSubList()) {
					if (calendars.contains(lCalendar) == false) {
						calendars.add(lCalendar);
					}
				}
			}
		});
	}

	/**
	 * 
	 */
	static public String quickFormatCalendar(Calendar value)
	{
		if (value == null) return "";
		SimpleDateFormat lSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return lSimpleDateFormat.format(value.getTime());
	}

	/**
	 * 
	 */
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
