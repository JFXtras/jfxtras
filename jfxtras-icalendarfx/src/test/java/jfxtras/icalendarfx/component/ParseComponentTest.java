package jfxtras.icalendarfx.component;

import static org.junit.Assert.assertEquals;

import java.time.Duration;

import org.junit.Test;

import jfxtras.icalendarfx.ICalendarTestAbstract;
import jfxtras.icalendarfx.components.SimpleVComponentFactory;
import jfxtras.icalendarfx.components.VAlarm;
import jfxtras.icalendarfx.components.VComponent;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.parameters.AlarmTriggerRelationship;
import jfxtras.icalendarfx.parameters.AlarmTriggerRelationship.AlarmTriggerRelationshipType;
import jfxtras.icalendarfx.properties.component.alarm.Trigger;
import jfxtras.icalendarfx.properties.component.alarm.Action.ActionType;

public class ParseComponentTest extends ICalendarTestAbstract
{           
    /** Tests FREQ=YEARLY */
    @Test
    public void canParseYearly1()
    {
        String vEventString = "BEGIN:VEVENT" + System.lineSeparator()
                              + "CATEGORIES:group13" + System.lineSeparator()
                              + "CREATED:20151109T082900Z" + System.lineSeparator()
                              + "DESCRIPTION:Yearly1 Description" + System.lineSeparator()
                              + "DTSTAMP:20151109T083000Z" + System.lineSeparator()
                              + "DTSTART:20151109T100000" + System.lineSeparator()
                              + "DURATION:PT1H" + System.lineSeparator()
                              + "LAST-MODIFIED:20151110T183000Z" + System.lineSeparator()
                              + "RRULE:FREQ=YEARLY" + System.lineSeparator()
                              + "SUMMARY:Yearly1 Summary" + System.lineSeparator()
                              + "UID:20151109T082900-0@jfxtras.org" + System.lineSeparator()
                              + "END:VEVENT";
        VEvent vEvent = VEvent.parse(vEventString);
        VEvent expectedVEvent = getYearly1();
        assertEquals(expectedVEvent, vEvent);
        assertEquals(vEventString, expectedVEvent.toContent());
    }

    @Test
    public void canParseDaily3()
    {
        String vEventString = "BEGIN:VEVENT" + System.lineSeparator()
                              + "DTEND:20151109T110000" + System.lineSeparator()
                              + "DTSTAMP:20150110T080000Z" + System.lineSeparator()
                              + "DTSTART:20151109T100000" + System.lineSeparator()
                              + "RRULE:FREQ=DAILY;INTERVAL=3;COUNT=10;BYMONTHDAY=9,10,11,12,13,14" + System.lineSeparator()
                              + "UID:20150110T080000-0@jfxtras.org" + System.lineSeparator()
                              + "END:VEVENT";
        VEvent vEvent = VEvent.parse(vEventString);
        VEvent expectedVEvent = getDaily3();
        assertEquals(expectedVEvent, vEvent);
        assertEquals(vEventString, expectedVEvent.toContent());
    }
    
    @Test
    public void canIgnoreBlankLines()
    {
        String vEventString = "BEGIN:VEVENT" + System.lineSeparator()
                              + "DTEND:20151109T110000" + System.lineSeparator()
                              + "DTSTAMP:20150110T080000Z" + System.lineSeparator()
                              + System.lineSeparator()
                              + "DTSTART:20151109T100000" + System.lineSeparator()
                              + "RRULE:FREQ=DAILY;INTERVAL=3;COUNT=10;BYMONTHDAY=9,10,11,12,13,14" + System.lineSeparator()
                              + System.lineSeparator()
                              + System.lineSeparator()
                              + "UID:20150110T080000-0@jfxtras.org" + System.lineSeparator()
                              + "END:VEVENT";
        VEvent vEvent = VEvent.parse(vEventString);
        VEvent expectedVEvent = getDaily3();
        assertEquals(expectedVEvent, vEvent);
        String vEventString2 = "BEGIN:VEVENT" + System.lineSeparator()
                + "DTEND:20151109T110000" + System.lineSeparator()
                + "DTSTAMP:20150110T080000Z" + System.lineSeparator()
                + "DTSTART:20151109T100000" + System.lineSeparator()
                + "RRULE:FREQ=DAILY;INTERVAL=3;COUNT=10;BYMONTHDAY=9,10,11,12,13,14" + System.lineSeparator()
                + "UID:20150110T080000-0@jfxtras.org" + System.lineSeparator()
                + "END:VEVENT";
        assertEquals(vEventString2, expectedVEvent.toContent());
    }
        
    @Test
    public void canParseDailyUTC()
    {
        String vEventString = "BEGIN:VEVENT" + System.lineSeparator()
                              + "DTSTART:20151109T100000Z" + System.lineSeparator()
                              + "DTEND:20151109T110000Z" + System.lineSeparator()
                              + "DTSTAMP:20150110T080000Z" + System.lineSeparator()
                              + "CATEGORIES:group03" + System.lineSeparator()
                              + "DESCRIPTION:DailyUTC Description" + System.lineSeparator()
                              + "SUMMARY:DailyUTC Summary" + System.lineSeparator()
                              + "UID:20150110T080000-0@jfxtras.org" + System.lineSeparator()
                              + "RRULE:FREQ=DAILY;INTERVAL=2;UNTIL=20151201T100000Z" + System.lineSeparator()
                              + "END:VEVENT";
        VEvent vEvent = VEvent.parse(vEventString);
        VEvent expectedVEvent = getDailyUTC();
        assertEquals(expectedVEvent, vEvent);
        assertEquals(vEventString, expectedVEvent.toContent());
    }
    
    /** Tests FREQ=YEARLY */
    @Test
    public void canParseDailyWithException1()
    {
        String vEventString = "BEGIN:VEVENT" + System.lineSeparator()
                + "DTSTART:20151109T100000" + System.lineSeparator()
                + "DURATION:PT1H30M" + System.lineSeparator()
                + "DTSTAMP:20150110T080000Z" + System.lineSeparator()
                + "CATEGORIES:group03" + System.lineSeparator()
                + "DESCRIPTION:Daily2 Description" + System.lineSeparator()
                + "SUMMARY:Daily2 Summary" + System.lineSeparator()
                + "UID:20150110T080000-0@jfxtras.org" + System.lineSeparator()
                + "RRULE:FREQ=DAILY;INTERVAL=3;COUNT=6" + System.lineSeparator()
                + "EXDATE:20151112T100000,20151115T100000" + System.lineSeparator()
                + "END:VEVENT";
        VEvent vEvent = VEvent.parse(vEventString);
        VEvent expectedVEvent = getDailyWithException1();
        assertEquals(expectedVEvent, vEvent);
        assertEquals(vEventString, expectedVEvent.toContent());
    }
    
    @Test
    public void canParseWholeDay1()
    {
        String vEventString = "BEGIN:VEVENT" + System.lineSeparator()
                              + "DTSTART;VALUE=DATE:20151109" + System.lineSeparator()
                              + "DTEND;VALUE=DATE:20151112" + System.lineSeparator()
                              + "DTSTAMP:20150110T080000Z" + System.lineSeparator()
                              + "UID:20150110T080000-0@jfxtras.org" + System.lineSeparator()
                              + "END:VEVENT";
        VEvent vEvent = VEvent.parse(vEventString);
        VEvent expectedVEvent = getWholeDayDaily1();
        assertEquals(expectedVEvent, vEvent);
    }
    
    @Test
    public void canParseGoogleIndividual()
    {
        String vEventString = "BEGIN:VEVENT" + System.lineSeparator()
                + "DTSTART:20160214T123000Z" + System.lineSeparator()
                + "DTEND:20160214T150000Z" + System.lineSeparator()
                + "DTSTAMP:20160214T022532Z" + System.lineSeparator()
                + "UID:vpqej26mlpg3adcncqqs7t7a34@google.com" + System.lineSeparator()
                + "CREATED:20160214T022513Z" + System.lineSeparator()
                + "DESCRIPTION:" + System.lineSeparator()
                + "LAST-MODIFIED:20160214T022513Z" + System.lineSeparator()
                + "LOCATION:" + System.lineSeparator()
                + "SEQUENCE:0" + System.lineSeparator()
                + "STATUS:CONFIRMED" + System.lineSeparator() // currently not supported
                + "SUMMARY:test1" + System.lineSeparator()
                + "TRANSP:OPAQUE" + System.lineSeparator() // currently not supported
                + "END:VEVENT";
        VEvent vEvent = VEvent.parse(vEventString);
        VEvent expectedVEvent = getGoogleIndividual();
        vEvent.equals(expectedVEvent);
        assertEquals(expectedVEvent, vEvent);
    }
    
    @Test
    public void canParseGoogleRepeat()
    {
        String vEventString = "BEGIN:VEVENT" + System.lineSeparator()
                + "DTSTART;TZID=America/Los_Angeles:20160214T080000" + System.lineSeparator()
                + "DTEND;TZID=America/Los_Angeles:20160214T110000" + System.lineSeparator()
                + "RRULE:FREQ=WEEKLY;BYDAY=SU,TU,FR" + System.lineSeparator()
                + "DTSTAMP:20160214T022532Z" + System.lineSeparator()
                + "UID:im8hmpakeigu3d85j3vq9q8bcc@google.com" + System.lineSeparator()
                + "CREATED:20160214T022525Z" + System.lineSeparator()
                + "DESCRIPTION:" + System.lineSeparator()
                + "LAST-MODIFIED:20160214T022525Z" + System.lineSeparator()
                + "LOCATION:" + System.lineSeparator()
                + "SEQUENCE:0" + System.lineSeparator()
                + "STATUS:CONFIRMED" + System.lineSeparator() // currently not supported
                + "SUMMARY:test2" + System.lineSeparator()
                + "TRANSP:OPAQUE" + System.lineSeparator() // currently not supported
                + "END:VEVENT";
        VEvent vEvent = VEvent.parse(vEventString);
        VEvent expectedVEvent = getGoogleRepeatable();
        assertEquals(expectedVEvent, vEvent);
    }
    
    @Test
    public void canParseGoogleRepeatWithExDates()
    {
        String content = "BEGIN:VEVENT" + System.lineSeparator()
                + "DTSTART;TZID=America/Los_Angeles:20160207T123000" + System.lineSeparator()
                + "DTEND;TZID=America/Los_Angeles:20160207T153000" + System.lineSeparator()
                + "RRULE:FREQ=DAILY;UNTIL=20160512T193000Z" + System.lineSeparator()
                + "EXDATE;TZID=America/Los_Angeles:20160210T123000" + System.lineSeparator()
                + "EXDATE;TZID=America/Los_Angeles:20160212T123000" + System.lineSeparator()
                + "EXDATE;TZID=America/Los_Angeles:20160209T123000" + System.lineSeparator()
                + "DTSTAMP:20160214T072231Z" + System.lineSeparator()
                + "UID:86801l7316n97h75cefk1ruc00@google.com" + System.lineSeparator()
                + "CREATED:20160214T022525Z" + System.lineSeparator()
                + "DESCRIPTION:" + System.lineSeparator()
                + "LAST-MODIFIED:20160214T022525Z" + System.lineSeparator()
                + "LOCATION:" + System.lineSeparator()
                + "SEQUENCE:0" + System.lineSeparator()
                + "STATUS:CONFIRMED" + System.lineSeparator() // currently not supported
                + "SUMMARY:test3" + System.lineSeparator()
                + "TRANSP:OPAQUE" + System.lineSeparator() // currently not supported
                + "END:VEVENT";
        VEvent vEvent = VEvent.parse(content);
        VEvent expectedVEvent = getGoogleWithExceptions();
        assertEquals(expectedVEvent, vEvent);
    }
    

    @Test
    public void canParseEmptyVevent()
    {
        VComponent vEvent = SimpleVComponentFactory.emptyVComponent("VEVENT");
        VComponent vExpected = new VEvent();
        assertEquals(vExpected, vEvent);
    }
    
    @Test
    public void canParseEmptyVevent2()
    {
        String content = "BEGIN:VEVENT" + System.lineSeparator() +
        "UID:19970610T172345Z-AF23B2@example.com" + System.lineSeparator() +
        "DTSTAMP:19970610T172345Z" + System.lineSeparator() +
        "DTSTART:19970714T170000Z" + System.lineSeparator() +
        "DTEND:19970715T040000Z" + System.lineSeparator() +
        "SUMMARY:Bastille Day Party" + System.lineSeparator() +
        "END:VEVENT";
        VComponent vEvent = SimpleVComponentFactory.emptyVComponent(content);
        VComponent vExpected = new VEvent();
        assertEquals(vExpected, vEvent);
    }

    @Test
    public void canParseWithFoldedLines()
    {
        String vEventString = "BEGIN:VEVENT" + System.lineSeparator()
                              + "DTSTART;VALUE=DATE:20151109" + System.lineSeparator()
                              + "DTEND;VALUE=DATE:20151112" + System.lineSeparator()
                              + "DTSTAMP:20150110T080000Z" + System.lineSeparator()
                              + "UID:20150110T080000-0@jfxtras.org" + System.lineSeparator()
                              + "COMMENT:This is a multiline " + System.lineSeparator()
                              + " comment." + System.lineSeparator()
                              + "DESCRIPTION:A dog ran " + System.lineSeparator()
                              + "" + System.lineSeparator() // ignore blank line
                              + " far away." + System.lineSeparator()
                              + "END:VEVENT";
        VComponent vEvent = SimpleVComponentFactory.emptyVComponent(vEventString);
        vEvent.parseContent(vEventString);
        VEvent expectedVEvent = getWholeDayDaily1()
                .withComments("This is a multiline comment.")
                .withDescription("A dog ran far away.");
        assertEquals(expectedVEvent, vEvent);
    }
    
    @Test
    public void canParseWithSubComponent()
    {
        String vEventString = "BEGIN:VEVENT" + System.lineSeparator()
                              + "DTSTART;VALUE=DATE:20151109" + System.lineSeparator()
                              + "DTEND;VALUE=DATE:20151112" + System.lineSeparator()
                              + "DTSTAMP:20150110T080000Z" + System.lineSeparator()
                              + "UID:20150110T080000-0@jfxtras.org" + System.lineSeparator()
                              + "COMMENT:This is a multiline " + System.lineSeparator()
                              + " comment." + System.lineSeparator()
                              + "BEGIN:VALARM" + System.lineSeparator()
                              + "ACTION:DISPLAY" + System.lineSeparator()
                              + "DESCRIPTION:This is a multiline " + System.lineSeparator()
                              + " description." + System.lineSeparator()
                              + "TRIGGER;RELATED=START:-PT30M" + System.lineSeparator()
                              + "END:VALARM" + System.lineSeparator()
                              + "END:VEVENT";
        VComponent vEvent = SimpleVComponentFactory.emptyVComponent(vEventString);
        vEvent.parseContent(vEventString);
        VEvent expectedVEvent = getWholeDayDaily1()
                .withComments("This is a multiline comment.")
                .withVAlarms(new VAlarm()
                        .withAction(ActionType.DISPLAY)
                        .withDescription("This is a multiline description.")
                        .withTrigger(new Trigger<Duration>(Duration.ofMinutes(-30))
                                .withAlarmTrigger(new AlarmTriggerRelationship(AlarmTriggerRelationshipType.START))));
    
        assertEquals(expectedVEvent, vEvent);
    }
}
