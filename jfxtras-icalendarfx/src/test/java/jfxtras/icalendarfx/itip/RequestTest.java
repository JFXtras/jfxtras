package jfxtras.icalendarfx.itip;

import static org.junit.Assert.assertEquals;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.Test;

import javafx.collections.ObservableList;
import jfxtras.icalendarfx.ICalendarStaticComponents;
import jfxtras.icalendarfx.VCalendar;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.properties.calendar.Version;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.FrequencyType;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule2;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay;

public class RequestTest
{
    @Test
    public void canShiftWeeklyAll() // shift day of weekly
    {
        VCalendar mainVCalendar = new VCalendar();
        final ObservableList<VEvent> vComponents = mainVCalendar.getVEvents();
        VEvent vComponentOriginal = ICalendarStaticComponents.getWeekly3();
        vComponents.add(vComponentOriginal);

        String iTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:REQUEST" + System.lineSeparator() +
                              "PRODID:-//Example/ExampleCalendarClient//EN" + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "DTSTART:20151110T090000" + System.lineSeparator() + // one day later
                "DTEND:20151110T103000" + System.lineSeparator() + // one day later, half hour longer
                "UID:20150110T080000-002@jfxtras.org" + System.lineSeparator() +
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "ORGANIZER;CN=Issac Newton:mailto:isaac@greatscientists.org" + System.lineSeparator() +
                "RRULE:FREQ=WEEKLY;BYDAY=TU" + System.lineSeparator() + // Changed Monday to Tuesday
                "SUMMARY:Edited summary" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        mainVCalendar.processITIPMessage(iTIPMessage);

        assertEquals(1, vComponents.size());
        VEvent myComponent = vComponents.get(0);
        VEvent expectedVComponent = ICalendarStaticComponents.getWeekly3()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 10, 9, 0))
                .withDateTimeEnd(LocalDateTime.of(2015, 11, 10, 10, 30))
                .withRecurrenceRule(new RecurrenceRule2()
                                .withFrequency(FrequencyType.WEEKLY)
                                .withByRules(new ByDay(DayOfWeek.TUESDAY)))
                .withSummary("Edited summary")
                .withSequence(1);
        assertEquals(expectedVComponent, myComponent);
    }
    
    @Test
    public void canShiftMonthlyAll() // shift day of week with monthly ordinal
    {
        VCalendar mainVCalendar = new VCalendar();
        final ObservableList<VEvent> vComponents = mainVCalendar.getVEvents();
        VEvent vComponentOriginal = ICalendarStaticComponents.getMonthly7();
        vComponents.add(vComponentOriginal);

        String iTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:REQUEST" + System.lineSeparator() +
                              "PRODID:-//Example/ExampleCalendarClient//EN" + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "DTSTART:20151110T090000" + System.lineSeparator() + // one day later
                "DTEND:20151110T103000" + System.lineSeparator() + // one day later, half hour longer
                "UID:20150110T080000-002@jfxtras.org" + System.lineSeparator() +
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "ORGANIZER;CN=Issac Newton:mailto:isaac@greatscientists.org" + System.lineSeparator() +
                "RRULE:FREQ=MONTHLY;BYDAY=3TU" + System.lineSeparator() + // Changed Monday to Tuesday
                "SUMMARY:Edited summary" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        mainVCalendar.processITIPMessage(iTIPMessage);

        assertEquals(1, vComponents.size());
        VEvent myComponent = vComponents.get(0);
        VEvent expectedVComponent = ICalendarStaticComponents.getWeekly3()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 10, 9, 0))
                .withDateTimeEnd(LocalDateTime.of(2015, 11, 10, 10, 30))
                .withRecurrenceRule(new RecurrenceRule2()
                                .withFrequency(FrequencyType.MONTHLY)
                                .withByRules(new ByDay(new ByDay.ByDayPair(DayOfWeek.TUESDAY, 3))))
                .withSummary("Edited summary")
                .withSequence(1);
        assertEquals(expectedVComponent, myComponent);
    }
    
    @Test
    public void canChangeToWholeDayAll()
    {
        VCalendar mainVCalendar = new VCalendar();
        final ObservableList<VEvent> vComponents = mainVCalendar.getVEvents();
        
        VEvent vComponentOriginal = ICalendarStaticComponents.getDaily1();
        vComponents.add(vComponentOriginal);

        String iTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:REQUEST" + System.lineSeparator() +
                              "PRODID:-//Example/ExampleCalendarClient//EN" + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
                "DTSTART;VALUE=DATE:20151108" + System.lineSeparator() +
                "DTEND;VALUE=DATE:20151109" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:Edited summary" + System.lineSeparator() +
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "RRULE:FREQ=DAILY" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR"
                ;
        mainVCalendar.processITIPMessage(iTIPMessage);

        assertEquals(1, vComponents.size());
        VEvent vComponentExpected = ICalendarStaticComponents.getDaily1()
                .withDateTimeStart(LocalDate.of(2015, 11, 8))
                .withDateTimeEnd(LocalDate.of(2015, 11, 9))
                .withSummary("Edited summary")
                .withSequence(1);
        
        assertEquals(vComponentExpected, vComponents.get(0));
    }
    
    @Test
    public void canRemoveExceptionDate()
    {
        VCalendar mainVCalendar = new VCalendar();
        final ObservableList<VEvent> vComponents = mainVCalendar.getVEvents();
        
        VEvent vComponentOriginal = ICalendarStaticComponents.getDailyWithException1();
        vComponents.add(vComponentOriginal);

        String iTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:REQUEST" + System.lineSeparator() +
                              "PRODID:-//Example/ExampleCalendarClient//EN" + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group03" + System.lineSeparator() +
                "DTSTART:20151109T100000" + System.lineSeparator() +
                "DTEND:20151109T113000" + System.lineSeparator() +
                "DESCRIPTION:Daily2 Description" + System.lineSeparator() +
                "SUMMARY:Daily2 Summary" + System.lineSeparator() +
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "UID:20150110T080000-005@jfxtras.org" + System.lineSeparator() +
                "RRULE:COUNT=6;FREQ=DAILY;INTERVAL=3" + System.lineSeparator() +
                "ORGANIZER;CN=Issac Newton:mailto:isaac@greatscientists.org" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR"
                ;
        mainVCalendar.processITIPMessage(iTIPMessage);

        assertEquals(1, vComponents.size());
        VEvent vComponentExpected = ICalendarStaticComponents.getDaily2()
                .withSequence(1);
        
        assertEquals(vComponentExpected, vComponents.get(0));
    }
}
