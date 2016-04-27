/**
 * CalendarPickerMonthlySkinAbstract.java
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.beans.InvalidationListener;
import javafx.scene.control.SkinBase;
import jfxtras.scene.control.CalendarPicker;

/**
 * This class contains common code to support skins that shows a month at once.
 * It assumes that there is a grid of clickables, one for every day of the month, and provides methods to help handle these.
 *  
 * @author Tom Eugelink
 *
 * @param <S> the actual skin class, so fluent methods return the correct class (see "return (S)this;")
 */
abstract public class CalendarPickerMonthlySkinAbstract<S> extends SkinBase<CalendarPicker>
{
    private static final List<String> sunWeekendDaysCountries = Arrays.asList(new String[]{"GQ", "IN", "TH", "UG"});
    private static final List<String> fryWeekendDaysCountries = Arrays.asList(new String[]{"DJ", "IR"});
    private static final List<String> frySunWeekendDaysCountries = Arrays.asList(new String[]{"BN"});
    private static final List<String> thuFryWeekendDaysCountries = Arrays.asList(new String[]{"AF"});
    private static final List<String> frySatWeekendDaysCountries = Arrays.asList(new String[]{"AE", "DZ", "BH", "BD", "EG", "IQ", "IL", "JO", "KW", "LY", "MV", "MR", "OM", "PS", "QA", "SA", "SD", "SY", "YE"});
	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public CalendarPickerMonthlySkinAbstract(CalendarPicker control)
	{
		super(control);//, new CalendarPickerBehavior(control));
		construct();
	}

	/*
	 * 
	 */
	private void construct()
	{
		// react to changes in the locale
		getSkinnable().localeProperty().addListener( (InvalidationListener) observable -> {
			refreshLocale();
		});
		refreshLocale();
	}

	// ==================================================================================================================
	// PROPERTIES
	
	/**
	 * 
	 */
	private void refreshLocale()
	{
		// create the formatter to use
		simpleDateFormat = (SimpleDateFormat)SimpleDateFormat.getDateInstance(SimpleDateFormat.LONG, getSkinnable().getLocale());
	}
	private SimpleDateFormat simpleDateFormat = null;


	// ==================================================================================================================
	// SUPPORT

	// refresh the skin
	abstract protected void refresh();

	/**
	 * 
	 */
	protected void calendarRangeCallback()
	{
		if (getSkinnable().calendarRangeCallbackProperty().get() != null) {
			// start and end
			Calendar lStartCalendar = periodStartCalendar(); 
			Calendar lEndCalendar = periodEndCalendar();
			try
			{
				calendarRangeCallbackAtomicInteger.incrementAndGet();
				getSkinnable().calendarRangeCallbackProperty().get().call(new CalendarPicker.CalendarRange(lStartCalendar, lEndCalendar));
			}
			finally
			{
				calendarRangeCallbackAtomicInteger.decrementAndGet();
			}
		}
	}
	protected final AtomicInteger calendarRangeCallbackAtomicInteger = new AtomicInteger(0);

	/**
	 * 
	 * @return
	 */
	protected Calendar periodStartCalendar()
	{
		return (Calendar)getSkinnable().getDisplayedCalendar().clone();
	}
	
	/**
	 * 
	 * @return
	 */
	protected Calendar periodEndCalendar()
	{
		Calendar lEndCalendar = (Calendar)getSkinnable().getDisplayedCalendar().clone();
		lEndCalendar.add(java.util.Calendar.MONTH, 1);
		lEndCalendar.set(java.util.Calendar.DATE, 1);
		lEndCalendar.add(java.util.Calendar.DATE, -1);
		return lEndCalendar;				
	}
	
	/**
	 * get the weekday labels starting with the weekday that is the first-day-of-the-week according to the locale in the displayed calendar
	 */
	protected List<String> getWeekdayLabels()
	{
		// result
		List<String> lWeekdayLabels = new ArrayList<String>();

		// setup the dayLabels
		// Calendar.SUNDAY = 1 and Calendar.SATURDAY = 7
		simpleDateFormat.applyPattern("E");
		Calendar lCalendar = new java.util.GregorianCalendar(2009, 6, 5); // july 5th 2009 is a Sunday
		for (int i = 0; i < 7; i++)
		{
			// next
			lCalendar.set(java.util.Calendar.DATE, 4 + getSkinnable().getDisplayedCalendar().getFirstDayOfWeek() + i);

			// assign day
			lWeekdayLabels.add( simpleDateFormat.format(lCalendar.getTime()));
		}
		
		// done
		return lWeekdayLabels;
	}
	
	/**
	 * Get a list with the weeklabels
	 */
	protected List<Integer> getWeeknumbers()
	{
		// result
		List<Integer> lWeekLabels = new ArrayList<Integer>();

		// setup the weekLabels
		Calendar lCalendar = (Calendar)getSkinnable().getDisplayedCalendar().clone();
		for (int i = 0; i <= 5; i++)
		{
			// set label
			lWeekLabels.add( lCalendar.get(java.util.Calendar.WEEK_OF_YEAR) );

			// next week
			lCalendar.add(java.util.Calendar.DATE, 7);
		}

		// done
		return lWeekLabels;
	}

	/**
	 * get the weekday labels starting with the weekday that is the first-day-of-the-week according to the locale in the displayed calendar
	 */
	protected List<String> getMonthLabels()
	{
		// result
		List<String> lMonthLabels = new ArrayList<String>();

		// setup the month
		simpleDateFormat.applyPattern("MMMM");
		Calendar lCalendar = new java.util.GregorianCalendar(2009, 0, 1); 
		for (int i = 0; i < 12; i++)
		{
			// next
			lCalendar.set(java.util.Calendar.MONTH, i);

			// assign day
			lMonthLabels.add( simpleDateFormat.format(lCalendar.getTime()));
		}
		
		// done
		return lMonthLabels;
	}
	
	/**
	 * check if a certain weekday name is a certain day-of-the-week
	 */
	protected boolean isWeekday(int idx, int weekdaynr)
	{
		// setup the dayLabels
		// Calendar.SUNDAY = 1 and Calendar.SATURDAY = 7
		Calendar lCalendar = new java.util.GregorianCalendar(2009, 6, 4 + getSkinnable().getDisplayedCalendar().getFirstDayOfWeek()); // july 5th 2009 is a Sunday
		lCalendar.add(java.util.Calendar.DATE, idx);
		int lDayOfWeek = lCalendar.get(java.util.Calendar.DAY_OF_WEEK);

		// check
		return (lDayOfWeek == weekdaynr);
	}

	/**
	 * check if a certain weekday name is a certain day-of-the-week
	 */
	protected boolean isWeekdayWeekend(int idx) 
	{
            Locale locale = getSkinnable().getLocale();
            if (thuFryWeekendDaysCountries.contains(locale.getCountry())) {
                return (isWeekday(idx, java.util.Calendar.THURSDAY) || isWeekday(idx, java.util.Calendar.FRIDAY));
            } else if (frySunWeekendDaysCountries.contains(locale.getCountry())) {
                return (isWeekday(idx, java.util.Calendar.FRIDAY) || isWeekday(idx, java.util.Calendar.SUNDAY));
            } else if (fryWeekendDaysCountries.contains(locale.getCountry())) {
                return isWeekday(idx, java.util.Calendar.FRIDAY);
            } else if (sunWeekendDaysCountries.contains(locale.getCountry())) {
                return isWeekday(idx, java.util.Calendar.SUNDAY);
            } else if (frySatWeekendDaysCountries.contains(locale.getCountry())) {
                return (isWeekday(idx, java.util.Calendar.FRIDAY) || isWeekday(idx, java.util.Calendar.SATURDAY));
            } else {
                return (isWeekday(idx, java.util.Calendar.SATURDAY) || isWeekday(idx, java.util.Calendar.SUNDAY));
            }
	}
	
	/**
	 * determine on which day of week idx is the first of the month
	 */
	protected int determineFirstOfMonthDayOfWeek()
	{
		Calendar lCalendar = (Calendar)getSkinnable().getDisplayedCalendar().clone();
		lCalendar.set(Calendar.DATE, 1);
		int lDayOfWeek = lCalendar.get(java.util.Calendar.DAY_OF_WEEK);
		int lFirstDayOfWeek = lCalendar.getFirstDayOfWeek();
		int lFirstOfMonthIdx = lDayOfWeek - lFirstDayOfWeek;
		if (lFirstOfMonthIdx < 0) {
			lFirstOfMonthIdx += 7;
		}
		return lFirstOfMonthIdx;
	}
	
	/**
	 * determine the number of days in the month
	 */
	protected int determineDaysInMonth()
	{
		// determine the number of days in the month
		Calendar lCalendar = (Calendar)getSkinnable().getDisplayedCalendar().clone();
		lCalendar.add(java.util.Calendar.MONTH, 1);
		lCalendar.set(java.util.Calendar.DATE, 1);
		lCalendar.add(java.util.Calendar.DATE, -1);
		return lCalendar.get(java.util.Calendar.DAY_OF_MONTH);
	}

	/**
	 * determine if a date is today
	 */
	protected boolean isToday(Calendar calendar)
	{
		int lYear = calendar.get(java.util.Calendar.YEAR);
		int lMonth = calendar.get(java.util.Calendar.MONTH);
		int lDay = calendar.get(java.util.Calendar.DATE);
		
		Calendar today = java.util.Calendar.getInstance();
		int lTodayYear = today.get(java.util.Calendar.YEAR);
		int lTodayMonth = today.get(java.util.Calendar.MONTH);
		int lTodayDay = today.get(java.util.Calendar.DATE);
		
		return (lYear == lTodayYear && lMonth == lTodayMonth && lDay == lTodayDay);
	}
}
