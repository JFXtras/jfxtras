package jfxtras.icalendarfx.component;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import jfxtras.icalendarfx.components.VComponent;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.components.VFreeBusy;
import jfxtras.icalendarfx.components.VJournal;
import jfxtras.icalendarfx.components.VPersonal;
import jfxtras.icalendarfx.components.VTodo;
import jfxtras.icalendarfx.properties.component.change.DateTimeStamp;
import jfxtras.icalendarfx.properties.component.misc.RequestStatus;
import jfxtras.icalendarfx.properties.component.relationship.Attendee;
import jfxtras.icalendarfx.properties.component.relationship.Organizer;
import jfxtras.icalendarfx.properties.component.relationship.UniformResourceLocator;
import jfxtras.icalendarfx.properties.component.relationship.UniqueIdentifier;

/**
 * Test following components:
 * @see VEvent
 * @see VTodo
 * @see VJournal
 * @see VFreeBusy
 * 
 * for the following properties:
 * @see Attendee
 * @see DateTimeStamp
 * @see Organizer
 * @see RequestStatus
 * @see UniqueIdentifier
 * @see UniformResourceLocator
 * 
 * @author David Bal
 *
 */
public class PersonalTest
{
    @Test
    public void canBuildPersonal() throws InstantiationException, IllegalAccessException
    {
        List<VPersonal<?>> components = Arrays.asList(
                new VEvent()
                    .withAttendees(Attendee.parse("ATTENDEE;MEMBER=\"mailto:DEV-GROUP@example.com\":mailto:joecool@example.com"))
                    .withDateTimeStamp(DateTimeStamp.parse("20160415T120000Z"))
                    .withOrganizer(Organizer.parse("ORGANIZER;CN=David Bal:mailto:ddbal1@yahoo.com"))
                    .withRequestStatus(RequestStatus.parse("REQUEST-STATUS:4.1;Event conflict.  Date-time is busy."), RequestStatus.parse("REQUEST-STATUS:3.7;Invalid user;ATTENDEE:mailto:joecool@example.com"))
                    .withUniqueIdentifier("19960401T080045Z-4000F192713-0052@example.com")
                    .withUniformResourceLocator("http://example.com/pub/calendars/jsmith/mytime.ics"),
                new VTodo()
                    .withAttendees(Attendee.parse("ATTENDEE;MEMBER=\"mailto:DEV-GROUP@example.com\":mailto:joecool@example.com"))
                    .withDateTimeStamp(DateTimeStamp.parse("20160415T120000Z"))
                    .withOrganizer(Organizer.parse("ORGANIZER;CN=David Bal:mailto:ddbal1@yahoo.com"))
                    .withRequestStatus(RequestStatus.parse("REQUEST-STATUS:4.1;Event conflict.  Date-time is busy."))
                    .withRequestStatus(RequestStatus.parse("REQUEST-STATUS:3.7;Invalid user;ATTENDEE:mailto:joecool@example.com"))
                    .withUniqueIdentifier("19960401T080045Z-4000F192713-0052@example.com")
                    .withUniformResourceLocator("http://example.com/pub/calendars/jsmith/mytime.ics"),
                new VJournal()
                    .withAttendees(Attendee.parse("ATTENDEE;MEMBER=\"mailto:DEV-GROUP@example.com\":mailto:joecool@example.com"))
                    .withDateTimeStamp(DateTimeStamp.parse("20160415T120000Z"))
                    .withOrganizer(Organizer.parse("ORGANIZER;CN=David Bal:mailto:ddbal1@yahoo.com"))
                    .withRequestStatus(RequestStatus.parse("REQUEST-STATUS:4.1;Event conflict.  Date-time is busy."))
                    .withRequestStatus(RequestStatus.parse("REQUEST-STATUS:3.7;Invalid user;ATTENDEE:mailto:joecool@example.com"))
                    .withUniqueIdentifier("19960401T080045Z-4000F192713-0052@example.com")
                    .withUniformResourceLocator("http://example.com/pub/calendars/jsmith/mytime.ics"),
                new VFreeBusy()
                    .withAttendees(Attendee.parse("ATTENDEE;MEMBER=\"mailto:DEV-GROUP@example.com\":mailto:joecool@example.com"))
                    .withDateTimeStamp(DateTimeStamp.parse("20160415T120000Z"))
                    .withOrganizer(Organizer.parse("ORGANIZER;CN=David Bal:mailto:ddbal1@yahoo.com"))
                    .withRequestStatus(RequestStatus.parse("REQUEST-STATUS:4.1;Event conflict.  Date-time is busy."), RequestStatus.parse("REQUEST-STATUS:3.7;Invalid user;ATTENDEE:mailto:joecool@example.com"))
                    .withUniqueIdentifier("19960401T080045Z-4000F192713-0052@example.com")
                    .withUniformResourceLocator("http://example.com/pub/calendars/jsmith/mytime.ics")
                );
        
        for (VPersonal<?> builtComponent : components)
        {
            String componentName = builtComponent.name().toString();            
            String expectedContent = "BEGIN:" + componentName + System.lineSeparator() +
                    "ATTENDEE;MEMBER=\"mailto:DEV-GROUP@example.com\":mailto:joecool@example.com" + System.lineSeparator() +
                    "DTSTAMP:20160415T120000Z" + System.lineSeparator() +
                    "ORGANIZER;CN=David Bal:mailto:ddbal1@yahoo.com" + System.lineSeparator() +
                    "REQUEST-STATUS:4.1;Event conflict.  Date-time is busy." + System.lineSeparator() +
                    "REQUEST-STATUS:3.7;Invalid user;ATTENDEE:mailto:joecool@example.com" + System.lineSeparator() +
                    "UID:19960401T080045Z-4000F192713-0052@example.com" + System.lineSeparator() +
                    "URL:http://example.com/pub/calendars/jsmith/mytime.ics" + System.lineSeparator() +
                    "END:" + componentName;

            VComponent parsedComponent = builtComponent.getClass().newInstance();
            parsedComponent.parseContent(expectedContent);

            assertEquals(parsedComponent, builtComponent);
            assertEquals(expectedContent, builtComponent.toContent());            
        }
    }
}
