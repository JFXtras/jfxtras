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
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import javafx.application.Application;
import javafx.geometry.NodeOrientation;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import jfxtras.scene.control.LocalDateTimeTextField;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.LocalDateTimeRange;
import jfxtras.scene.control.agenda.AgendaSkinSwitcher;

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
//		lAgenda.setAllowDragging(false);
//		lAgenda.setAllowResize(false);
		lAgenda.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
		
		// setup appointment groups
        final Map<String, Agenda.AppointmentGroup> lAppointmentGroupMap = new TreeMap<String, Agenda.AppointmentGroup>();
        for (Agenda.AppointmentGroup lAppointmentGroup : lAgenda.appointmentGroups()) {
        	lAppointmentGroupMap.put(lAppointmentGroup.getDescription(), lAppointmentGroup);
        }
			
		// accept new appointments
		lAgenda.newAppointmentCallbackProperty().set(new Callback<Agenda.LocalDateTimeRange, Agenda.Appointment>()
		{
			@Override
			public Agenda.Appointment call(LocalDateTimeRange dateTimeRange)
			{
				return new Agenda.AppointmentImplLocal()
				.withStartLocalDateTime( dateTimeRange.getStartLocalDateTime() )
				.withEndLocalDateTime( dateTimeRange.getEndLocalDateTime() )
				.withSummary("new")
				.withDescription("new")
				.withAppointmentGroup(lAppointmentGroupMap.get("group01"));
			}
		});
		
		// initial set
		LocalDate lTodayLocalDate = LocalDate.now();
		LocalDate lTomorrowLocalDate = LocalDate.now().plusDays(1);
		int idx = 0;
		final String lIpsum = "Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Vestibulum tortor quam, feugiat vitae, ultricies eget, tempor sit amet, ante. Donec eu libero sit amet quam egestas semper. Aenean ultricies mi vitae est. Mauris placerat eleifend leo. Quisque sit amet est et sapien ullamcorper pharetra. Vestibulum erat wisi, condimentum sed, commodo vitae, ornare sit amet, wisi. Aenean fermentum, elit eget tincidunt condimentum, eros ipsum rutrum orci, sagittis tempus lacus enim ac dui. Donec non enim in turpis pulvinar facilisis. Ut felis. Praesent dapibus, neque id cursus faucibus, tortor neque egestas augue, eu vulputate magna eros eu erat. Aliquam erat volutpat. Nam dui mi, tincidunt quis, accumsan porttitor, facilisis luctus, metus";
		LocalDateTime lMultipleDaySpannerStartDateTime = lTodayLocalDate.atStartOfDay().plusHours(5);
		lMultipleDaySpannerStartDateTime = lMultipleDaySpannerStartDateTime.minusDays(lMultipleDaySpannerStartDateTime.getDayOfWeek().getValue() > 3 && lMultipleDaySpannerStartDateTime.getDayOfWeek().getValue() < 7 ? 3 : -1);
		LocalDateTime lMultipleDaySpannerEndDateTime = lMultipleDaySpannerStartDateTime.plusDays(2);
		
		Appointment[] lTestAppointments = new Appointment[]{
			new Agenda.AppointmentImplLocal()
				.withStartLocalDateTime(LocalDateTime.of(lTodayLocalDate, LocalTime.of(8, 00)))
				.withEndLocalDateTime(LocalDateTime.of(lTodayLocalDate, LocalTime.of(11, 30)))
				.withSummary("A")
				.withDescription("A much longer test description")
				.withAppointmentGroup(lAppointmentGroupMap.get("group07"))
		, 	new Agenda.AppointmentImplLocal()
				.withStartLocalDateTime(LocalDateTime.of(lTodayLocalDate, LocalTime.of(8, 30)))
				.withEndLocalDateTime(LocalDateTime.of(lTodayLocalDate, LocalTime.of(10, 00)))
				.withSummary("B")
				.withDescription("A description " + (++idx))
				.withAppointmentGroup(lAppointmentGroupMap.get("group08"))
		, 	new Agenda.AppointmentImplLocal()
				.withStartLocalDateTime(LocalDateTime.of(lTodayLocalDate, LocalTime.of(8, 30)))
				.withEndLocalDateTime(LocalDateTime.of(lTodayLocalDate, LocalTime.of(9, 30)))
				.withSummary("C")
				.withDescription("A description " + (++idx))
				.withAppointmentGroup(lAppointmentGroupMap.get("group09"))
		, 	new Agenda.AppointmentImplLocal()
				.withStartLocalDateTime(LocalDateTime.of(lTodayLocalDate, LocalTime.of(9, 00)))
				.withEndLocalDateTime(LocalDateTime.of(lTodayLocalDate, LocalTime.of(13, 30)))
				.withSummary("D")
				.withDescription("A description " + (++idx))
				.withAppointmentGroup(lAppointmentGroupMap.get("group07"))
		, 	new Agenda.AppointmentImplLocal()
				.withStartLocalDateTime(LocalDateTime.of(lTodayLocalDate, LocalTime.of(10, 30)))
				.withEndLocalDateTime(LocalDateTime.of(lTodayLocalDate, LocalTime.of(11, 00)))
				.withSummary("E")
				.withDescription("A description " + (++idx))
				.withAppointmentGroup(lAppointmentGroupMap.get("group07"))
		, 	new Agenda.AppointmentImplLocal()
				.withStartLocalDateTime(LocalDateTime.of(lTodayLocalDate, LocalTime.of(12, 30)))
				.withEndLocalDateTime(LocalDateTime.of(lTodayLocalDate, LocalTime.of(13, 30)))
				.withSummary("F")
				.withDescription("A description " + (++idx))
				.withAppointmentGroup(lAppointmentGroupMap.get("group07"))
		, 	new Agenda.AppointmentImplLocal()
				.withStartLocalDateTime(LocalDateTime.of(lTodayLocalDate, LocalTime.of(13, 00)))
				.withEndLocalDateTime(LocalDateTime.of(lTodayLocalDate, LocalTime.of(13, 30)))
				.withSummary("H")
				.withDescription("A description " + (++idx))
				.withAppointmentGroup(lAppointmentGroupMap.get("group07"))
		, 	new Agenda.AppointmentImplLocal()
				.withStartLocalDateTime(LocalDateTime.of(lTodayLocalDate, LocalTime.of(14, 00)))
				.withEndLocalDateTime(LocalDateTime.of(lTodayLocalDate, LocalTime.of(14, 45)))
				.withSummary("G")
				.withDescription("A description " + (++idx))
				.withAppointmentGroup(lAppointmentGroupMap.get("group07"))
		, 	new Agenda.AppointmentImplLocal()
				.withStartLocalDateTime(LocalDateTime.of(lTodayLocalDate, LocalTime.of(15, 00)))
				.withEndLocalDateTime(LocalDateTime.of(lTodayLocalDate, LocalTime.of(16, 00)))
				.withSummary("I")
				.withDescription("A description " + (++idx))
				.withAppointmentGroup(lAppointmentGroupMap.get("group07"))
		, 	new Agenda.AppointmentImplLocal()
				.withStartLocalDateTime(LocalDateTime.of(lTodayLocalDate, LocalTime.of(15, 30)))
				.withEndLocalDateTime(LocalDateTime.of(lTodayLocalDate, LocalTime.of(16, 00)))
				.withSummary("J")
				.withDescription("A description " + (++idx))
				.withAppointmentGroup(lAppointmentGroupMap.get("group07"))
		// backwards compatibility: calendar based appointment
		, 	new Agenda.AppointmentImpl()
				.withStartTime(new GregorianCalendar(lTodayLocalDate.getYear(), lTodayLocalDate.getMonthValue() - 1, lTodayLocalDate.getDayOfMonth(), 4, 00))
				.withEndTime(new GregorianCalendar(lTodayLocalDate.getYear(), lTodayLocalDate.getMonthValue() - 1, lTodayLocalDate.getDayOfMonth(), 5, 30))
				.withSummary("Cal")
				.withDescription("Calendar based")
				.withAppointmentGroup(lAppointmentGroupMap.get("group08"))
		// ZonedDateTime: there is no additional value in using ZonedDateTime everywhere, so we just have one test appointment
		, 	new Agenda.AppointmentImplZoned()
				.withStartZonedDateTime(ZonedDateTime.of(lTodayLocalDate, LocalTime.of(2, 00), ZoneId.systemDefault()) )
				.withEndZonedDateTime(ZonedDateTime.of(lTodayLocalDate, LocalTime.of(3, 30), ZoneId.systemDefault()) )
				.withSummary("Zoned")
				.withDescription("Zoned based")
				.withAppointmentGroup(lAppointmentGroupMap.get("group08"))
		// -----
		// too short for actual rendering
		, 	new Agenda.AppointmentImplLocal()
				.withStartLocalDateTime(LocalDateTime.of(lTodayLocalDate, LocalTime.of(20, 30)))
				.withEndLocalDateTime(LocalDateTime.of(lTodayLocalDate, LocalTime.of(20, 31)))
				.withSummary("S")
				.withDescription("Too short")
				.withAppointmentGroup(lAppointmentGroupMap.get("group07"))
		// -----
		// tasks
		, 	new Agenda.AppointmentImplLocal()
				.withStartLocalDateTime(LocalDateTime.of(lTodayLocalDate, LocalTime.of(8, 10)))
				.withSummary("K kk kkkkk k k k k ")
				.withDescription("A description " + (++idx))
				.withAppointmentGroup(lAppointmentGroupMap.get("group17"))
		, 	new Agenda.AppointmentImplLocal()
				.withStartLocalDateTime(LocalDateTime.of(lTodayLocalDate, LocalTime.of(8, 10)))
				.withSummary("M mmm m m m m m mmmm")
				.withDescription("A description " + (++idx))
				.withAppointmentGroup(lAppointmentGroupMap.get("group18"))
		, 	new Agenda.AppointmentImplLocal()
				.withStartLocalDateTime(LocalDateTime.of(lTodayLocalDate, LocalTime.of(8, 11)))
				.withSummary("N nnnn n n n  nnnnn")
				.withDescription("A description " + (++idx))
				.withAppointmentGroup(lAppointmentGroupMap.get("group19"))
		, 	new Agenda.AppointmentImplLocal()
				.withStartLocalDateTime(LocalDateTime.of(lTodayLocalDate, LocalTime.of(6, 00)))
				.withSummary("L asfsfd dsfsdfs fsfds sdgsds dsdfsd ")
				.withDescription("A description " + (++idx))
				.withAppointmentGroup(lAppointmentGroupMap.get("group11"))
		// -----
		// wholeday
		, 	new Agenda.AppointmentImplLocal()
				.withStartLocalDateTime(lTodayLocalDate.atStartOfDay())
				.withSummary("whole1")
				.withDescription("A description " + (++idx))
				.withAppointmentGroup(lAppointmentGroupMap.get("group17"))
				.withWholeDay(true)
		, 	new Agenda.AppointmentImplLocal()
				.withStartLocalDateTime(lTodayLocalDate.atStartOfDay())
				.withSummary("whole2")
				.withDescription("A description " + (++idx))
				.withAppointmentGroup(lAppointmentGroupMap.get("group18"))
				.withWholeDay(true)
		, 	new Agenda.AppointmentImplLocal()
				.withStartLocalDateTime(lTodayLocalDate.atStartOfDay())
				.withEndLocalDateTime(lTomorrowLocalDate.atStartOfDay()) // at we going to do en
				.withSummary("whole3")
				.withDescription("A description " + (++idx))
				.withAppointmentGroup(lAppointmentGroupMap.get("group19"))
				.withWholeDay(true)
		, 	new Agenda.AppointmentImplLocal()
				.withStartLocalDateTime(lTomorrowLocalDate.atStartOfDay())
				.withEndLocalDateTime(lTomorrowLocalDate.atTime(13, 00))
				.withSummary("whole+end")
				.withDescription("A description " + (++idx))
				.withAppointmentGroup(lAppointmentGroupMap.get("group13"))
				.withWholeDay(true)
		, 	new Agenda.AppointmentImplLocal()
				.withStartLocalDateTime(lTodayLocalDate.atStartOfDay())
				.withEndLocalDateTime(lTomorrowLocalDate.atTime(13, 00))
				.withSummary("whole+spanning")
				.withDescription("A description " + (++idx))
				.withAppointmentGroup(lAppointmentGroupMap.get("group14"))
				.withWholeDay(true)
		// -----
		// regular spanning
		, 	new Agenda.AppointmentImplLocal()
				.withStartLocalDateTime(lMultipleDaySpannerStartDateTime)
				.withEndLocalDateTime(lMultipleDaySpannerEndDateTime)
				.withSummary(lIpsum.substring(0, 20 + new Random().nextInt(lIpsum.length() - 20)))
				.withDescription("A description " + (++idx))
				.withAppointmentGroup(lAppointmentGroupMap.get("group20"))
		};
		lAgenda.appointments().addAll(lTestAppointments);

		// action
		lAgenda.setActionCallback( (appointment) -> {
			System.out.println("Action on " + appointment);
			return null;
		});

		// update range
		lAgenda.localDateTimeRangeCallbackProperty().set( (LocalDateTimeRange range) ->  {

			System.out.println("dateTimeRangeCallback " + range);
			lAgenda.appointments().clear();
			LocalDateTime now = LocalDateTime.now();
			
			WeekFields weekFields = WeekFields.of(Locale.getDefault()); 
			int startWeekNumber = range.getStartLocalDateTime().get(weekFields.weekOfWeekBasedYear());
			int nowWeekNumber = now.get(weekFields.weekOfWeekBasedYear());
			
			if (startWeekNumber == nowWeekNumber) {
				// add predefined appointments 
				lAgenda.appointments().addAll(lTestAppointments);
			}
			else {
				int firstDayOfWeek = weekFields.getFirstDayOfWeek().getValue();
				int currentDayOfWeek = range.getStartLocalDateTime().getDayOfWeek().getValue();
				LocalDateTime lFirstDayOfWeekLocalDateTime = now.plusDays( currentDayOfWeek >= firstDayOfWeek ? currentDayOfWeek - firstDayOfWeek : firstDayOfWeek - currentDayOfWeek);
				
				// add a whole bunch of random appointments
				for (int i = 0; i < 20; i++)
				{
					LocalDateTime lStart = lFirstDayOfWeekLocalDateTime
						.plusDays(new Random().nextInt(7))
						.plusHours(new Random().nextInt(24))
						.plusMinutes(new Random().nextInt(60));
					
					LocalDateTime lEnd = (new Random().nextInt(7) == 0 ? null : lStart.plusMinutes(15 + new Random().nextInt(24 * 60)));
					
					Agenda.Appointment lAppointment = new Agenda.AppointmentImplLocal()
						.withStartLocalDateTime(lStart)
						.withEndLocalDateTime(lEnd)
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
		// add the skin switcher
		AgendaSkinSwitcher skinSwitcher = new AgendaSkinSwitcher(lAgenda);
		lHBox.getChildren().add(skinSwitcher);
		// add the displayed date textfield
		LocalDateTimeTextField lLocalDateTimeTextField = new LocalDateTimeTextField();
		lLocalDateTimeTextField.localDateTimeProperty().bindBidirectional(lAgenda.displayedLocalDateTime());		
        lHBox.getChildren().add(lLocalDateTimeTextField);
        
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
