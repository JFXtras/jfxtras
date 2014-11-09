/**
 * AgendaTrial1.java
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

package jfxtras.scene.control.agenda.trial;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import jfxtras.scene.control.CalendarTextField;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;
import jfxtras.scene.control.agenda.Agenda.DateTimeRange;

/**
 * @author Tom Eugelink
 */
public class AgendaTrial1 extends Application {
	
    public static void main(String[] args) {
        launch(args);       
    }

	@Override
	public void start(Stage stage) {

        // add a node
		final Agenda lAgenda = new Agenda();		
//    	lAgenda.setLocale(new java.util.Locale("de")); // weeks starts on monday
//		lAgenda.setSkin(new jfxtras.internal.scene.control.skin.agenda.AgendaDaySkin(lAgenda));
		
		// setup appointment groups
		final Map<String, Agenda.AppointmentGroup> lAppointmentGroupMap = new TreeMap<String, Agenda.AppointmentGroup>();
		lAppointmentGroupMap.put("group00", new Agenda.AppointmentGroupImpl().withStyleClass("group0"));
		lAppointmentGroupMap.put("group01", new Agenda.AppointmentGroupImpl().withStyleClass("group1"));
		lAppointmentGroupMap.put("group02", new Agenda.AppointmentGroupImpl().withStyleClass("group2"));
		lAppointmentGroupMap.put("group03", new Agenda.AppointmentGroupImpl().withStyleClass("group3"));
		lAppointmentGroupMap.put("group04", new Agenda.AppointmentGroupImpl().withStyleClass("group4"));
		lAppointmentGroupMap.put("group05", new Agenda.AppointmentGroupImpl().withStyleClass("group5"));
		lAppointmentGroupMap.put("group06", new Agenda.AppointmentGroupImpl().withStyleClass("group6"));
		lAppointmentGroupMap.put("group07", new Agenda.AppointmentGroupImpl().withStyleClass("group7"));
		lAppointmentGroupMap.put("group08", new Agenda.AppointmentGroupImpl().withStyleClass("group8"));
		lAppointmentGroupMap.put("group09", new Agenda.AppointmentGroupImpl().withStyleClass("group9"));
		lAppointmentGroupMap.put("group10", new Agenda.AppointmentGroupImpl().withStyleClass("group10"));
		lAppointmentGroupMap.put("group11", new Agenda.AppointmentGroupImpl().withStyleClass("group11"));
		lAppointmentGroupMap.put("group12", new Agenda.AppointmentGroupImpl().withStyleClass("group12"));
		lAppointmentGroupMap.put("group13", new Agenda.AppointmentGroupImpl().withStyleClass("group13"));
		lAppointmentGroupMap.put("group14", new Agenda.AppointmentGroupImpl().withStyleClass("group14"));
		lAppointmentGroupMap.put("group15", new Agenda.AppointmentGroupImpl().withStyleClass("group15"));
		lAppointmentGroupMap.put("group16", new Agenda.AppointmentGroupImpl().withStyleClass("group16"));
		lAppointmentGroupMap.put("group17", new Agenda.AppointmentGroupImpl().withStyleClass("group17"));
		lAppointmentGroupMap.put("group18", new Agenda.AppointmentGroupImpl().withStyleClass("group18"));
		lAppointmentGroupMap.put("group19", new Agenda.AppointmentGroupImpl().withStyleClass("group19"));
		lAppointmentGroupMap.put("group20", new Agenda.AppointmentGroupImpl().withStyleClass("group20"));
		lAppointmentGroupMap.put("group21", new Agenda.AppointmentGroupImpl().withStyleClass("group21"));
		lAppointmentGroupMap.put("group22", new Agenda.AppointmentGroupImpl().withStyleClass("group22"));
		lAppointmentGroupMap.put("group23", new Agenda.AppointmentGroupImpl().withStyleClass("group23"));
		for (String lId : lAppointmentGroupMap.keySet())
		{
			AppointmentGroup lAppointmentGroup = lAppointmentGroupMap.get(lId);
			lAppointmentGroup.setDescription(lId);
			lAgenda.appointmentGroups().add(lAppointmentGroup);
		}
			
		// accept new appointments
		lAgenda.newAppointmentCallbackProperty().set(new Callback<Agenda.DateTimeRange, Agenda.Appointment>()
		{
			@Override
			public Agenda.Appointment call(DateTimeRange dateTimeRange)
			{
				return new Agenda.AppointmentImpl2()
				.withStartDateTime( dateTimeRange.getStartDateTime() )
				.withEndDateTime( dateTimeRange.getEndDateTime() )
				.withSummary("new")
				.withDescription("new")
				.withAppointmentGroup(lAppointmentGroupMap.get("group01"));
			}
		});
		
		// initial set
		LocalDate lToday = LocalDate.now();
		int lTodayYear = lToday.getYear();
		int lTodayMonth = lToday.getMonthValue();
		int lTodayDay = lToday.getDayOfMonth();
		int idx = 0;
		final String lIpsum = "Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Vestibulum tortor quam, feugiat vitae, ultricies eget, tempor sit amet, ante. Donec eu libero sit amet quam egestas semper. Aenean ultricies mi vitae est. Mauris placerat eleifend leo. Quisque sit amet est et sapien ullamcorper pharetra. Vestibulum erat wisi, condimentum sed, commodo vitae, ornare sit amet, wisi. Aenean fermentum, elit eget tincidunt condimentum, eros ipsum rutrum orci, sagittis tempus lacus enim ac dui. Donec non enim in turpis pulvinar facilisis. Ut felis. Praesent dapibus, neque id cursus faucibus, tortor neque egestas augue, eu vulputate magna eros eu erat. Aliquam erat volutpat. Nam dui mi, tincidunt quis, accumsan porttitor, facilisis luctus, metus";
		LocalDateTime lMultipleDaySpannerStartDateTime = lToday.atStartOfDay().plusHours(5);
		lMultipleDaySpannerStartDateTime = lMultipleDaySpannerStartDateTime.minusDays(lMultipleDaySpannerStartDateTime.getDayOfWeek().getValue() > 3 && lMultipleDaySpannerStartDateTime.getDayOfWeek().getValue() < 7 ? 3 : -1);
		LocalDateTime lMultipleDaySpannerEndDateTime = lMultipleDaySpannerStartDateTime.plusDays(2);
		
		Appointment[] lTestAppointments = new Appointment[]{
		/*
		 *  . . . .
		 *  . . . . 
		 *  A . . .  8:00
		 *  A B C .  8:30
		 *  A B C D  9:00
		 *  A B . D  9:30
		 *  A . . D 10:00
		 *  A E . D 10:30
		 *  A . . D 11:00
		 *  . . . D 11:30
		 *  . . . D 12:00
		 *  F . . D 12:30
		 *  F H . D 13:00
		 *  . . . . 13:30
		 *  G . . . 14:00
		 *  . . . . 14:30
		 * 
		 */
			
			new Agenda.AppointmentImpl2()
				.withStartDateTime(LocalDateTime.of(lTodayYear, lTodayMonth, lTodayDay, 8, 00))
				.withEndDateTime(LocalDateTime.of(lTodayYear, lTodayMonth, lTodayDay, 11, 30))
				.withSummary("A")
				.withDescription("A much longer test description")
				.withAppointmentGroup(lAppointmentGroupMap.get("group07"))
		, 	new Agenda.AppointmentImpl2()
				.withStartDateTime(LocalDateTime.of(lTodayYear, lTodayMonth, lTodayDay, 8, 30))
				.withEndDateTime(LocalDateTime.of(lTodayYear, lTodayMonth, lTodayDay, 10, 00))
				.withSummary("B")
				.withDescription("A description " + (++idx))
				.withAppointmentGroup(lAppointmentGroupMap.get("group08"))
		, 	new Agenda.AppointmentImpl2()
				.withStartDateTime(LocalDateTime.of(lTodayYear, lTodayMonth, lTodayDay, 8, 30))
				.withEndDateTime(LocalDateTime.of(lTodayYear, lTodayMonth, lTodayDay, 9, 30))
				.withSummary("C")
				.withDescription("A description " + (++idx))
				.withAppointmentGroup(lAppointmentGroupMap.get("group09"))
		, 	new Agenda.AppointmentImpl2()
				.withStartDateTime(LocalDateTime.of(lTodayYear, lTodayMonth, lTodayDay, 9, 00))
				.withEndDateTime(LocalDateTime.of(lTodayYear, lTodayMonth, lTodayDay, 13, 30))
				.withSummary("D")
				.withDescription("A description " + (++idx))
				.withAppointmentGroup(lAppointmentGroupMap.get("group07"))
		, 	new Agenda.AppointmentImpl2()
				.withStartDateTime(LocalDateTime.of(lTodayYear, lTodayMonth, lTodayDay, 10, 30))
				.withEndDateTime(LocalDateTime.of(lTodayYear, lTodayMonth, lTodayDay, 11, 00))
				.withSummary("E")
				.withDescription("A description " + (++idx))
				.withAppointmentGroup(lAppointmentGroupMap.get("group07"))
		, 	new Agenda.AppointmentImpl2()
				.withStartDateTime(LocalDateTime.of(lTodayYear, lTodayMonth, lTodayDay, 12, 30))
				.withEndDateTime(LocalDateTime.of(lTodayYear, lTodayMonth, lTodayDay, 13, 30))
				.withSummary("F")
				.withDescription("A description " + (++idx))
				.withAppointmentGroup(lAppointmentGroupMap.get("group07"))
		, 	new Agenda.AppointmentImpl2()
				.withStartDateTime(LocalDateTime.of(lTodayYear, lTodayMonth, lTodayDay, 13, 00))
				.withEndDateTime(LocalDateTime.of(lTodayYear, lTodayMonth, lTodayDay, 13, 30))
				.withSummary("H")
				.withDescription("A description " + (++idx))
				.withAppointmentGroup(lAppointmentGroupMap.get("group07"))
		, 	new Agenda.AppointmentImpl2()
				.withStartDateTime(LocalDateTime.of(lTodayYear, lTodayMonth, lTodayDay, 14, 00))
				.withEndDateTime(LocalDateTime.of(lTodayYear, lTodayMonth, lTodayDay, 14, 45))
				.withSummary("G")
				.withDescription("A description " + (++idx))
				.withAppointmentGroup(lAppointmentGroupMap.get("group07"))
		, 	new Agenda.AppointmentImpl2()
				.withStartDateTime(LocalDateTime.of(lTodayYear, lTodayMonth, lTodayDay, 15, 00))
				.withEndDateTime(LocalDateTime.of(lTodayYear, lTodayMonth, lTodayDay, 16, 00))
				.withSummary("I")
				.withDescription("A description " + (++idx))
				.withAppointmentGroup(lAppointmentGroupMap.get("group07"))
		, 	new Agenda.AppointmentImpl2()
				.withStartDateTime(LocalDateTime.of(lTodayYear, lTodayMonth, lTodayDay, 15, 30))
				.withEndDateTime(LocalDateTime.of(lTodayYear, lTodayMonth, lTodayDay, 16, 00))
				.withSummary("J")
				.withDescription("A description " + (++idx))
				.withAppointmentGroup(lAppointmentGroupMap.get("group07"))
		// -----
		// too short for actual rendering
		, 	new Agenda.AppointmentImpl2()
				.withStartDateTime(LocalDateTime.of(lTodayYear, lTodayMonth, lTodayDay, 20, 30))
				.withEndDateTime(LocalDateTime.of(lTodayYear, lTodayMonth, lTodayDay, 20, 31))
				.withSummary("S")
				.withDescription("Too short")
				.withAppointmentGroup(lAppointmentGroupMap.get("group07"))
		// -----
		// tasks
		, 	new Agenda.AppointmentImpl2()
				.withStartDateTime(LocalDateTime.of(lTodayYear, lTodayMonth, lTodayDay, 8, 10))
				.withSummary("K asfsfd dsfsdfs fsfds sdgsds dsdfsd ")
				.withDescription("A description " + (++idx))
				.withAppointmentGroup(lAppointmentGroupMap.get("group17"))
		, 	new Agenda.AppointmentImpl2()
				.withStartDateTime(LocalDateTime.of(lTodayYear, lTodayMonth, lTodayDay, 6, 00))
				.withSummary("L asfsfd dsfsdfs fsfds sdgsds dsdfsd ")
				.withDescription("A description " + (++idx))
				.withAppointmentGroup(lAppointmentGroupMap.get("group11"))
		// -----
		// wholeday
		, 	new Agenda.AppointmentImpl2()
				.withStartDateTime(LocalDate.of(lTodayYear, lTodayMonth, lTodayDay).atStartOfDay())
				.withSummary("whole1")
				.withDescription("A description " + (++idx))
				.withAppointmentGroup(lAppointmentGroupMap.get("group17"))
				.withWholeDay(true)
		, 	new Agenda.AppointmentImpl2()
				.withStartDateTime(LocalDate.of(lTodayYear, lTodayMonth, lTodayDay).atStartOfDay())
				.withSummary("whole2")
				.withDescription("A description " + (++idx))
				.withAppointmentGroup(lAppointmentGroupMap.get("group18"))
				.withWholeDay(true)
		, 	new Agenda.AppointmentImpl2()
				.withStartDateTime(LocalDate.of(lTodayYear, lTodayMonth, lTodayDay).atStartOfDay())
				.withEndDateTime(LocalDate.of(lTodayYear, lTodayMonth, lTodayDay + 1).atStartOfDay()) // at we going to do en
				.withSummary("whole3")
				.withDescription("A description " + (++idx))
				.withAppointmentGroup(lAppointmentGroupMap.get("group19"))
				.withWholeDay(true)
		, 	new Agenda.AppointmentImpl2()
				.withStartDateTime(LocalDate.of(lTodayYear, lTodayMonth, lTodayDay + 1).atStartOfDay())
				.withEndDateTime(LocalDate.of(lTodayYear, lTodayMonth, lTodayDay + 1).atTime(13, 00))
				.withSummary("whole+end")
				.withDescription("A description " + (++idx))
				.withAppointmentGroup(lAppointmentGroupMap.get("group13"))
				.withWholeDay(true)
		, 	new Agenda.AppointmentImpl2()
				.withStartDateTime(lMultipleDaySpannerStartDateTime)
				.withEndDateTime(lMultipleDaySpannerEndDateTime)
				.withSummary(lIpsum.substring(0, 20 + new Random().nextInt(lIpsum.length() - 20)))
				.withDescription("A description " + (++idx))
				.withAppointmentGroup(lAppointmentGroupMap.get("group20"))
		};
		

		// update range
		lAgenda.dateTimeRangeCallbackProperty().set( (DateTimeRange range) ->  {

			lAgenda.appointments().clear();
			LocalDateTime now = LocalDateTime.now();
			
			if ( (range.getStartDateTime().isBefore(now) || range.getStartDateTime().isEqual(now))
			  && (range.getEndDateTime().isAfter(now) || range.getEndDateTime().isEqual(now))
			) {
				// add predefined appointments 
				lAgenda.appointments().addAll(lTestAppointments);
			}
			else {
				WeekFields weekFields = WeekFields.of(Locale.getDefault());
				int firstDayOfWeek = weekFields.getFirstDayOfWeek().getValue();
				int currentDayOfWeek = now.getDayOfWeek().getValue();
				LocalDateTime lFirstDayOfWeekCalendar = now.plusDays( currentDayOfWeek >= firstDayOfWeek ? currentDayOfWeek - firstDayOfWeek : firstDayOfWeek - currentDayOfWeek);
				
				// add a whole bunch of random appointments
				for (int i = 0; i < 20; i++)
				{
					LocalDateTime lStart = lFirstDayOfWeekCalendar
						.plusDays(new Random().nextInt(7))
						.plusHours(new Random().nextInt(24))
						.plusMinutes(new Random().nextInt(60));
					
					LocalDateTime lEnd = (new Random().nextInt(7) == 0 ? null : lStart.plusMinutes(15 + new Random().nextInt(24 * 60)));
					
					Agenda.Appointment lAppointment = new Agenda.AppointmentImpl2()
						.withStartDateTime(lStart)
						.withEndDateTime(lEnd)
						.withWholeDay(new Random().nextInt(50) > 40)
						.withSummary(lIpsum.substring(0, new Random().nextInt(50)))
						.withDescription(lIpsum.substring(0, new Random().nextInt(lIpsum.length())))
						.withAppointmentGroup(lAppointmentGroupMap.get("group0" + (new Random().nextInt(10))));					
					lAgenda.appointments().add(lAppointment);
				}
			}
			return null;
		});
		
		HBox lHBox = new HBox();
		CalendarTextField lCalendarTextField = new CalendarTextField();
		lCalendarTextField.calendarProperty().bindBidirectional(lAgenda.displayedCalendar());		
        lHBox.getChildren().add(lCalendarTextField);
        
        // create scene
        BorderPane lBorderPane = new BorderPane();
        lBorderPane.setCenter(lAgenda);
        lBorderPane.setBottom(lHBox);
        //lBorderPane.setLeft(new Label("AAAAAAA"));
        Scene scene = new Scene(lBorderPane, 900, 900);

        // create stage
        stage.setTitle(this.getClass().getSimpleName());
        stage.setScene(scene);
        stage.show();	
    }
	
	/**
	 * get the calendar for the first day of the week
	 */
	static private Calendar getFirstDayOfWeekCalendar(Locale locale, Calendar c)
	{
		// result
		int lFirstDayOfWeek = Calendar.getInstance(locale).getFirstDayOfWeek();
		int lCurrentDayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		int lDelta = 0;
		if (lFirstDayOfWeek <= lCurrentDayOfWeek)
		{
			lDelta = -lCurrentDayOfWeek + lFirstDayOfWeek;
		}
		else
		{
			lDelta = -lCurrentDayOfWeek - (7-lFirstDayOfWeek);
		}
		c = ((Calendar)c.clone());
		c.add(Calendar.DATE, lDelta);
		return c;
	}

}
