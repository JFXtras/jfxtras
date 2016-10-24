package jfxtras.scene.control.agenda.icalendar.test.misc;

import static org.junit.Assert.assertTrue;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;

import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.properties.component.descriptive.Summary;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.FrequencyType;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule2;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;
import jfxtras.scene.control.agenda.icalendar.factories.DefaultRecurrenceFactory;
import jfxtras.scene.control.agenda.icalendar.factories.RecurrenceFactory;
import jfxtras.scene.control.agenda.icalendar.test.ICalendarStaticComponents;

public class MakeAppointmentsTest
{
    final public static List<AppointmentGroup> DEFAULT_APPOINTMENT_GROUPS = IntStream.range(0, 24)
            .mapToObj(i -> new Agenda.AppointmentGroupImpl()
                  .withStyleClass("group" + i)
                  .withDescription("group" + (i < 10 ? "0" : "") + i))
            .collect(Collectors.toList());
    
    /** Tests daily stream with FREQ=DAILY */
    @Test
    public void makeAppointmentsDailyTest1()
    {
        VEvent vevent = ICalendarStaticComponents.getDaily1();
        RecurrenceFactory<Appointment> recurrenceFactory = new DefaultRecurrenceFactory(DEFAULT_APPOINTMENT_GROUPS);
        LocalDateTime startRange = LocalDateTime.of(2015, 11, 15, 0, 0);
        LocalDateTime endRange = LocalDateTime.of(2015, 11, 22, 0, 0);
        recurrenceFactory.setStartRange(startRange);
        recurrenceFactory.setEndRange(endRange);
        List<Appointment> newAppointments = recurrenceFactory.makeRecurrences(vevent);
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 15, 10, 0)
              , LocalDateTime.of(2015, 11, 16, 10, 0)
              , LocalDateTime.of(2015, 11, 17, 10, 0)
              , LocalDateTime.of(2015, 11, 18, 10, 0)
              , LocalDateTime.of(2015, 11, 19, 10, 0)
              , LocalDateTime.of(2015, 11, 20, 10, 0)
              , LocalDateTime.of(2015, 11, 21, 10, 0)
                ));

        List<Appointment> expectedAppointments = expectedDates
                .stream()
                .map(d -> {
                    return new Agenda.AppointmentImplTemporal()
                            .withStartTemporal(d)
                            .withEndTemporal(d.plus(1, ChronoUnit.HOURS))
                            .withSummary("Daily1 Summary");
                })
                .collect(Collectors.toList());
        for (int i=0; i<expectedAppointments.size(); i++)
        {
            assertTrue(isEqualTo(expectedAppointments.get(i), newAppointments.get(i)));
        }
    }
    
    @Test
    public void makeAppointmentsDailyTest2()
    {
        LocalDate startDate = LocalDate.of(2015, 11, 15);
        VEvent vevent = new VEvent()
              .withDateTimeEnd(ZonedDateTime.of(LocalDateTime.of(startDate.plusDays(1), LocalTime.of(9, 45)), ZoneId.of("America/Los_Angeles")))
              .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 11, 10, 8, 0), ZoneOffset.UTC))
              .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(startDate.plusDays(1), LocalTime.of(8, 15)), ZoneId.of("America/Los_Angeles")))
              .withDescription("WeeklyZoned Description")
              .withRecurrenceRule(new RecurrenceRule2()
                      .withUntil(ZonedDateTime.of(LocalDateTime.of(startDate.plusDays(15), LocalTime.of(8, 15)), ZoneId.of("America/Los_Angeles")).withZoneSameInstant(ZoneId.of("Z")))
                      .withFrequency(FrequencyType.WEEKLY)
                      .withByRules(new ByDay(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)))
              .withSummary(Summary.parse("WeeklyZoned Ends"))
              .withUniqueIdentifier("20150110T080000-1@jfxtras.org");
        RecurrenceFactory<Appointment> recurrenceFactory = new DefaultRecurrenceFactory(DEFAULT_APPOINTMENT_GROUPS);
        LocalDateTime startRange = LocalDateTime.of(2015, 11, 15, 0, 0);
        LocalDateTime endRange = LocalDateTime.of(2015, 11, 22, 0, 0);
        recurrenceFactory.setStartRange(startRange);
        recurrenceFactory.setEndRange(endRange);
        List<Appointment> newAppointments = recurrenceFactory.makeRecurrences(vevent);
        List<Temporal> expectedDates = new ArrayList<>(Arrays.asList(
                ZonedDateTime.of(LocalDateTime.of(2015, 11, 16, 8, 15), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 18, 8, 15), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 20, 8, 15), ZoneId.of("America/Los_Angeles"))
                ));

        List<Appointment> expectedAppointments = expectedDates
                .stream()
                .map(d -> {
                    return new Agenda.AppointmentImplTemporal()
                            .withStartTemporal(d)
                            .withEndTemporal(d.plus(90, ChronoUnit.MINUTES))
                            .withSummary("WeeklyZoned Ends");
                })
                .collect(Collectors.toList());
        for (int i=0; i<expectedAppointments.size(); i++)
        {
            assertTrue(isEqualTo(expectedAppointments.get(i), newAppointments.get(i)));
        }
    }
    
    @Test
    public void makeAppointmentsDailyTest3()
    {
        LocalDate startDate = LocalDate.of(2015, 11, 15);
        VEvent vevent = new VEvent()
                .withCategories(DEFAULT_APPOINTMENT_GROUPS.get(15).getDescription())
                .withDateTimeStart(startDate)
                .withDateTimeEnd(startDate.plusDays(1))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withDescription("LocalDate Description")
                .withSummary("LocalDate")
                .withUniqueIdentifier("20150110T080000-3@jfxtras.org")
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.WEEKLY)
                        .withInterval(3));
        RecurrenceFactory<Appointment> recurrenceFactory = new DefaultRecurrenceFactory(DEFAULT_APPOINTMENT_GROUPS);
        LocalDateTime startRange = LocalDateTime.of(2015, 11, 15, 0, 0);
        LocalDateTime endRange = LocalDateTime.of(2015, 11, 22, 0, 0);
        recurrenceFactory.setStartRange(startRange);
        recurrenceFactory.setEndRange(endRange);
        List<Appointment> newAppointments = recurrenceFactory.makeRecurrences(vevent);
        List<Temporal> expectedDates = new ArrayList<>(Arrays.asList(
                LocalDate.of(2015, 11, 15)
                ));

        List<Appointment> expectedAppointments = expectedDates
                .stream()
                .map(d -> {
                    return new Agenda.AppointmentImplTemporal()
                            .withStartTemporal(d)
                            .withEndTemporal(d.plus(1, ChronoUnit.DAYS))
                            .withSummary("LocalDate");
                })
                .collect(Collectors.toList());
        for (int i=0; i<expectedAppointments.size(); i++)
        {
            assertTrue(isEqualTo(expectedAppointments.get(i), newAppointments.get(i)));
        }
    }
    
    public static boolean isEqualTo(Appointment a1, Appointment a2)
    {
        boolean startEquals = a1.getStartTemporal().equals(a2.getStartTemporal());
        if (! startEquals)
        {
            System.out.println("startTemporal not equal:" + a1.getStartTemporal() + ", " + a2.getStartTemporal());
        }
        boolean endEquals = a1.getEndTemporal().equals(a2.getEndTemporal());
        if (! endEquals)
        {
            System.out.println("endTemporal not equal:" + a1.getEndTemporal() + ", " + a2.getEndTemporal());
        }
        boolean summaryEquals = a1.getSummary().equals(a2.getSummary());
        if (! summaryEquals)
        {
            System.out.println("summary not equal:" + a1.getSummary() + ", " + a2.getSummary());
        }
        return startEquals && endEquals && summaryEquals;
    }
}
