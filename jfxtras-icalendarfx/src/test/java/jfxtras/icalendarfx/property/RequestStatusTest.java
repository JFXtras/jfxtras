package jfxtras.icalendarfx.property;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.icalendarfx.properties.component.misc.RequestStatus;
import jfxtras.icalendarfx.utilities.ICalendarUtilities;

public class RequestStatusTest
{
    @Test
    public void canParseRequestStatus()
    {
        String content = "REQUEST-STATUS:2.0;Success";
        RequestStatus madeProperty = RequestStatus.parse(content);
        assertEquals(content, madeProperty.toContent());
        RequestStatus expectedProperty = new RequestStatus()
                .withStatusCode(2.0)
                .withDescription("Success");
        assertEquals(expectedProperty, madeProperty);
        
        madeProperty.setStatusCode(2.81);
        assertEquals("REQUEST-STATUS:2.81;Success", madeProperty.toContent());
        
        madeProperty.setValue("3.7;Invalid calendar user;ATTENDEE:mailto:jsmith@example.com");
        assertEquals((Double) 3.7, madeProperty.getStatusCode());
        assertEquals("Invalid calendar user", madeProperty.getDescription());
        assertEquals("ATTENDEE:mailto:jsmith@example.com", madeProperty.getException());
    }
    
    @Test
    public void canParseRequestStatus2()
    {
        String content = "REQUEST-STATUS:2.8;Success\\, repeating event ignored. Scheduled as a single event.;RRULE:FREQ=WEEKLY\\;INTERVAL=2";
        RequestStatus madeProperty = RequestStatus.parse(content);
        assertEquals(ICalendarUtilities.foldLine(content).toString(), madeProperty.toContent());
        RequestStatus expectedProperty = new RequestStatus()
                .withStatusCode(2.8)
                .withDescription("Success, repeating event ignored. Scheduled as a single event.")
                .withException("RRULE:FREQ=WEEKLY;INTERVAL=2");
        assertEquals(expectedProperty, madeProperty);
    }
}
