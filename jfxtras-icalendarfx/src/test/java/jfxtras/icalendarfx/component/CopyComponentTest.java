package jfxtras.icalendarfx.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;

import org.junit.Test;

import jfxtras.icalendarfx.ICalendarTestAbstract;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.properties.component.recurrence.ExceptionDates;

public class CopyComponentTest extends ICalendarTestAbstract
{
    @Test
    public void canCopyVEvent1()
    {
        VEvent vevent = new VEvent()
                .withDateTimeStart("20150831")
                .withComments("comment1", "comment2");
        VEvent veventCopy = new VEvent(vevent);
//        assertTrue(VCalendarUtilities.isEqualTo(vevent, veventCopy));
        assertEquals(vevent, veventCopy);
    }
    
    @Test
    public void canCopyVEvent2()
    {
        VEvent component1 = getWeekly3()
                .withExceptionDates(new ExceptionDates(
                LocalDateTime.of(2016, 2, 10, 12, 30)
              , LocalDateTime.of(2016, 2, 12, 12, 30)
              , LocalDateTime.of(2016, 2, 9, 12, 30) ));
        VEvent component2 = new VEvent(component1);
        assertEquals(component1, component2);
        assertFalse(component1 == component2);
    }
    
    @Test
    public void canCopyVEvent3()
    {
       String content = "BEGIN:VEVENT" + System.lineSeparator() +
       "SUMMARY:test2" + System.lineSeparator() +
       "DESCRIPTION:test notes" + System.lineSeparator() +
       "CLASS:PUBLIC" + System.lineSeparator() +
       "DTSTART;TZID=Etc/GMT:20160306T060000Z" + System.lineSeparator() +
       "DTEND;TZID=Etc/GMT:20160306T103000Z" + System.lineSeparator() +
       "LOCATION:there" + System.lineSeparator() +
       "PRIORITY:0" + System.lineSeparator() +
       "SEQUENCE:1" + System.lineSeparator() +
       "STATUS:CONFIRMED" + System.lineSeparator() +
       "UID:3a87e308-1b48-48c6-8f69-742e8645efa6" + System.lineSeparator() +
       "CATEGORIES:fun" + System.lineSeparator() +
       "DTSTAMP:20160312T024041Z" + System.lineSeparator() +
       "ORGANIZER;CN=David Bal;SENT-BY=\"mailto:ddbal1@yahoo.com\":mailto:ddbal1@yaho" + System.lineSeparator() +
       " o.com" + System.lineSeparator() +
       "RRULE:FREQ=WEEKLY;BYDAY=SU,TU,FR" + System.lineSeparator() +
       "X-YAHOO-YID:daviddbal" + System.lineSeparator() +
       "TRANSP:OPAQUE" + System.lineSeparator() +
//       "STATUS:CONFIRMED" + System.lineSeparator() +
       "X-YAHOO-USER-STATUS:BUSY" + System.lineSeparator() +
       "X-YAHOO-EVENT-STATUS:BUSY" + System.lineSeparator() +
       "END:VEVENT";
        VEvent component1 = VEvent.parse(content);
        VEvent component2 = new VEvent(component1);
        assertEquals(component1, component2);
        assertEquals(component1.toContent(), component2.toContent());
        assertEquals(content, component2.toContent());
    }

    @Test
    public void canCopyVEvent4()
    {
        String content = 
   "BEGIN:VEVENT" + System.lineSeparator() +
   "SUMMARY:Personal" + System.lineSeparator() +
   "DESCRIPTION:Mr. Bal – please call into 123-456-7890. If you have any issues" + System.lineSeparator() +
   " \\, please contact my office manager\\, Wicked Witch.\\n\\nv/r\\,\\nWicked Witch" + System.lineSeparator() +
   " \\nExecutive Associate to\\nTrash Collector and\\nHead of Waste Removal Engin" + System.lineSeparator() +
   " eering\\n1 Green Drive\\nCity of Oz\\, Oz  12345-1234\\n123-456-7890\\nFax 123-" + System.lineSeparator() +
   " 456-7890\\n\\n\\n\\n\\n\\n" + System.lineSeparator() +
   "CLASS:PUBLIC" + System.lineSeparator() +
   "DTSTART;TZID=Etc/GMT:20150616T160000Z" + System.lineSeparator() +
   "DTEND;TZID=Etc/GMT:20150616T170000Z" + System.lineSeparator() +
   "LOCATION:Emerald City" + System.lineSeparator() +
   "PRIORITY:0" + System.lineSeparator() +
   "SEQUENCE:3" + System.lineSeparator() +
   "STATUS:CONFIRMED" + System.lineSeparator() +
   "UID:040000008200E00074C5B7101A02E0080000000070E8A4A909A5D001000000000000000" + System.lineSeparator() +
   " 0100000003C112A360433BF49A1274D3AE5B4FBA0" + System.lineSeparator() +
   "DTSTAMP:20150612T194320Z" + System.lineSeparator() +
   "ATTENDEE;PARTSTAT=NEEDS-ACTION;CN=\"The Wizard of Oz\";ROLE=REQ" + System.lineSeparator() +
   " _PARTICIPANT;RSVP=TRUE:mailto:MAILTO:wizard@oz.org" + System.lineSeparator() +
   "ATTENDEE;PARTSTAT=NEEDS-ACTION;CN=ddbal1@yahoo.com;ROLE=REQ_PARTICIPANT;RSV" + System.lineSeparator() +
   " P=TRUE:mailto:MAILTO:ddbal1@yahoo.com" + System.lineSeparator() +
   "ATTENDEE;PARTSTAT=ACCEPTED;CN=David Bal;ROLE=REQ_PARTICIPANT;RSVP=TRUE:mail" + System.lineSeparator() +
   " to:ddbal1@yahoo.com" + System.lineSeparator() +
   "ORGANIZER;CN=\"Wicked Witch of the East\";SENT-BY=\"mailto:MAILTO:cowardly" + System.lineSeparator() +
   " .lion@oz.org\":mailto:MAILTO:wicked.witch@oz.org" + System.lineSeparator() +
   "X-MICROSOFT-CDO-APPT-SEQUENCE:2" + System.lineSeparator() +
   "X-MICROSOFT-CDO-OWNERAPPTID:-833053493" + System.lineSeparator() +
   "X-MICROSOFT-CDO-BUSYSTATUS:TENTATIVE" + System.lineSeparator() +
   "X-MICROSOFT-CDO-INTENDEDSTATUS:BUSY" + System.lineSeparator() +
   "X-MICROSOFT-CDO-ALLDAYEVENT:FALSE" + System.lineSeparator() +
   "X-MICROSOFT-CDO-IMPORTANCE:1" + System.lineSeparator() +
   "X-MICROSOFT-CDO-INSTTYPE:0" + System.lineSeparator() +
   "X-MICROSOFT-DISALLOW-COUNTER:FALSE" + System.lineSeparator() +
   "X-YAHOO-YID:yahoo.calendar.acl.writer" + System.lineSeparator() +
   "X-YAHOO-YID:yahoo.calendar.acl.writer" + System.lineSeparator() +
   "X-YAHOO-YID:yahoo.calendar.acl.writer" + System.lineSeparator() +
   "X-YAHOO-YID:yahoo.calendar.acl.writer" + System.lineSeparator() +
   "TRANSP:OPAQUE" + System.lineSeparator() +
//   "STATUS:CONFIRMED" + System.lineSeparator() +
   "X-YAHOO-USER-STATUS:BUSY" + System.lineSeparator() +
   "X-YAHOO-EVENT-STATUS:BUSY" + System.lineSeparator() +
   "BEGIN:VALARM" + System.lineSeparator() +
   "ACTION:DISPLAY" + System.lineSeparator() +
   "DESCRIPTION:" + System.lineSeparator() +
   "TRIGGER;RELATED=START:-PT15M" + System.lineSeparator() +
   "END:VALARM" + System.lineSeparator() +
   "END:VEVENT";
        
        VEvent component1 = VEvent.parse(content);
        System.out.println("getVAlarms:" + component1.getVAlarms().size());
        System.out.println("hash:" + System.identityHashCode(component1));
        VEvent component2 = new VEvent(component1);
//        System.out.println(component2);
        assertEquals(component1, component2);
        assertEquals(component1.toContent(), component2.toContent());
    }
    
    /*
     * BEGIN:VEVENT
DTSTART:20151107T100000
DURATION:PT45M
DESCRIPTION:Weekly3 Description
LOCATION:here
SUMMARY:Weekly3 Summary
RRULE:FREQ=WEEKLY;BYDAY=MO,WE,FR
END:VEVENT

BEGIN:VEVENT
CATEGORIES:group13
CREATED:20151109T082900Z
DESCRIPTION:Yearly1 Description
DTSTAMP:20151109T083000Z
DTSTART:20151109T100000
DURATION:PT1H
LAST-MODIFIED:20151110T183000Z
RRULE:FREQ=YEARLY
SUMMARY:Yearly1 Summary
UID:20151109T082900-0@jfxtras.org
END:VEVENT

BEGIN:VEVENT
CATEGORIES:group13
CREATED:20151109T082900Z
DESCRIPTION:Yearly1 Description
DTSTAMP:20151109T083000Z
DTSTART:20151109T100000
DURATION:PT1H
LAST-MODIFIED:20151110T183000Z
RRULE:FREQ=YEARLY
SUMMARY:Yearly1 Summary
UID:20151109T082900-0@jfxtras.org
LOCATION:here
END:VEVENT
     */
    @Test
    public void canCopyVEvent5()
    {
        VEvent component1 = getWeekly3();
        VEvent component2 = getYearly1();
        component2.copyInto(component1);
//        component1.copyChildrenFrom(component2);
        String expectedContent = 
            "BEGIN:VEVENT" + System.lineSeparator() +
            "DTSTART:20151109T100000" + System.lineSeparator() +
            "DURATION:PT1H" + System.lineSeparator() +
            "DESCRIPTION:Yearly1 Description" + System.lineSeparator() +
            "LOCATION:here" + System.lineSeparator() +
            "SUMMARY:Yearly1 Summary" + System.lineSeparator() +
            "RRULE:FREQ=YEARLY" + System.lineSeparator() +
            "CATEGORIES:group13" + System.lineSeparator() +
            "CREATED:20151109T082900Z" + System.lineSeparator() +
            "DTSTAMP:20151109T083000Z" + System.lineSeparator() +
            "LAST-MODIFIED:20151110T183000Z" + System.lineSeparator() +
            "UID:20151109T082900-0@jfxtras.org" + System.lineSeparator() +
            "END:VEVENT";
        assertEquals(expectedContent, component1.toContent());
    }
    
    @Test
    public void canCopyVEvent6() throws InstantiationException, IllegalAccessException
    {
        VEvent component2 = getYearly1();
        VEvent component1 = component2.getClass().newInstance();
        component2.copyInto(component1);
//        component1.copyChildrenFrom(component2);
        assertEquals(component1, component2);
        assertTrue(component1 != component2);
    }
    
    @Test
    public void canCopyVEvent7()
    {
        VEvent component1 = getWeekly3();
        VEvent component2 = getYearly1();
        component1.copyInto(component2);
//        component2.copyChildrenFrom(component1);
        String expectedContent = 
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group13" + System.lineSeparator() +
                "CREATED:20151109T082900Z" + System.lineSeparator() +
                "DESCRIPTION:Weekly3 Description" + System.lineSeparator() +
                "DTSTAMP:20151109T083000Z" + System.lineSeparator() +
                "DTSTART:20151107T100000" + System.lineSeparator() +
                "DURATION:PT45M" + System.lineSeparator() +
                "LAST-MODIFIED:20151110T183000Z" + System.lineSeparator() +
                "RRULE:FREQ=WEEKLY;BYDAY=MO,WE,FR" + System.lineSeparator() +
                "SUMMARY:Weekly3 Summary" + System.lineSeparator() +
                "UID:20151109T082900-0@jfxtras.org" + System.lineSeparator() +
                "LOCATION:here" + System.lineSeparator() +
                "END:VEVENT";
        assertEquals(expectedContent, component2.toContent());
    }
//    
//    @Test
//    public void canCopyVEvent8()
//    {
//        VCalendar vCalendar = new VCalendar();
//        final ObservableList<VEvent> vComponents = vCalendar.getVEvents();
//        VEvent vComponentOriginal = ICalendarStaticComponents.getDaily1();
//        VEvent vComponentOriginalCopy = new VEvent(vComponentOriginal);
//        vComponents.add(vComponentOriginal);
//        // make recurrence
//        VEvent vComponentRecurrence = ICalendarStaticComponents.getDaily1();
//        vComponentRecurrence.setRecurrenceRule((RecurrenceRule2) null);
//        vComponentRecurrence.setRecurrenceId(LocalDateTime.of(2016, 5, 17, 10, 0));
//        vComponentRecurrence.setSummary("recurrence summary");
//        vComponentRecurrence.setDateTimeStart(LocalDateTime.of(2016, 5, 17, 8, 30));
//        vComponentRecurrence.setDateTimeEnd(LocalDateTime.of(2016, 5, 17, 9, 30));
//        vComponents.add(vComponentRecurrence);
//        System.out.println(vComponentOriginal.childComponents().size());
//        System.out.println(vComponentOriginalCopy.childComponents().size());
//        System.out.println(vComponentOriginal.toContent());
//        System.out.println(vComponentOriginalCopy.toContent());
//    }
}
