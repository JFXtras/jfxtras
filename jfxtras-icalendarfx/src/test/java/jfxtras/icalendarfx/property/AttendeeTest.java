package jfxtras.icalendarfx.property;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

import org.junit.Test;

import jfxtras.icalendarfx.parameters.GroupMembership;
import jfxtras.icalendarfx.parameters.CalendarUser.CalendarUserType;
import jfxtras.icalendarfx.parameters.ParticipationRole.ParticipationRoleType;
import jfxtras.icalendarfx.parameters.ParticipationStatus.ParticipationStatusType;
import jfxtras.icalendarfx.properties.component.relationship.Attendee;
import jfxtras.icalendarfx.utilities.ICalendarUtilities;

public class AttendeeTest
{
    @Test
    public void canParseAttendee1() throws URISyntaxException
    {
        String content = "ATTENDEE;MEMBER=\"mailto:DEV-GROUP@example.com\":mailto:joecool@example.com";
        Attendee madeProperty = Attendee.parse(content);
        Attendee expectedProperty = Attendee.parse("mailto:joecool@example.com").withGroupMembership("\"mailto:DEV-GROUP@example.com\"");
        assertEquals(expectedProperty, madeProperty);
        assertEquals(content, expectedProperty.toContent());
    }

    @Test
    public void canParseAttendee2() throws URISyntaxException
    {
        String content = "ATTENDEE;MEMBER=\"mailto:projectA@example.com\",\"mailto:projectB@example.com\":mailto:joecool@example.com";
        Attendee madeProperty = Attendee.parse(content);
        Attendee expectedProperty = Attendee.parse("mailto:joecool@example.com")
                .withGroupMembership(
                        new GroupMembership(
                                Arrays.asList(new URI("mailto:projectA@example.com"), new URI("mailto:projectB@example.com"))));
        assertEquals(expectedProperty, madeProperty);
        String foldedContent = ICalendarUtilities.foldLine(content).toString();
        assertEquals(foldedContent, expectedProperty.toContent());
    }
    
    @Test
    public void canParseAttendee3() throws URISyntaxException
    {
        String content = "ATTENDEE;CN=\"John Smith\":mailto:jsmith@example.com";
        Attendee madeProperty = Attendee.parse(content);
        Attendee expectedProperty = Attendee.parse("mailto:jsmith@example.com")
                .withCommonName("John Smith");
        assertEquals(expectedProperty, madeProperty);
        assertEquals("ATTENDEE;CN=John Smith:mailto:jsmith@example.com", expectedProperty.toContent()); // quotes should be removed from common name
    }
    
    @Test
    public void canParseAttendee4() throws URISyntaxException
    {
        String content = "ATTENDEE;CUTYPE=GROUP:mailto:ietf-calsch@example.org";
        Attendee madeProperty = Attendee.parse(content);
        Attendee expectedProperty = Attendee.parse("mailto:ietf-calsch@example.org")
                .withCalendarUser(CalendarUserType.GROUP);
        assertEquals(expectedProperty, madeProperty);
        assertEquals(content, expectedProperty.toContent());
    }
    
    @Test
    public void canParseAttendee5() throws URISyntaxException
    {
        String content = "ATTENDEE;DELEGATED-FROM=\"mailto:jsmith@example.com\":mailto:jdoe@example.com";
        Attendee madeProperty = Attendee.parse(content);
        Attendee expectedProperty = Attendee.parse("mailto:jdoe@example.com")
                .withDelegators("mailto:jsmith@example.com");
        assertEquals(expectedProperty, madeProperty);
        assertEquals(content, expectedProperty.toContent());
    }
    
    @Test
    public void canParseAttendee6() throws URISyntaxException
    {
        String content = "ATTENDEE;DELEGATED-TO=\"mailto:jdoe@example.com\",\"mailto:jqpublic@example.com\":mailto:jsmith@example.com";
        Attendee madeProperty = Attendee.parse(content);
        Attendee expectedProperty = Attendee.parse("mailto:jsmith@example.com")
                .withDelegatees("\"mailto:jdoe@example.com\",\"mailto:jqpublic@example.com\"");
        assertEquals(expectedProperty, madeProperty);
        String foldedContent = ICalendarUtilities.foldLine(content).toString();
        assertEquals(foldedContent, expectedProperty.toContent());
    }
    
    @Test
    public void canParseAttendee7() throws URISyntaxException
    {
        String content = "ATTENDEE;RSVP=TRUE:mailto:jsmith@example.com";
        Attendee madeProperty = Attendee.parse(content);
        Attendee expectedProperty = Attendee.parse("mailto:jsmith@example.com")
                .withRSVP(true);
        assertEquals(expectedProperty, madeProperty);
        assertEquals(content, expectedProperty.toContent());
    }
    
    @Test
    public void canParseAttendee8() throws URISyntaxException
    {
        String content = "ATTENDEE;ROLE=CHAIR:mailto:mrbig@example.com";
        Attendee madeProperty = Attendee.parse(content);
        Attendee expectedProperty = Attendee.parse("mailto:mrbig@example.com")
                .withParticipationRole(ParticipationRoleType.CHAIR);
        assertEquals(expectedProperty, madeProperty);
        assertEquals(content, expectedProperty.toContent());
    }
    
    @Test
    public void canParseAttendee9() throws URISyntaxException
    {
        String content = "ATTENDEE;PARTSTAT=DECLINED:mailto:jsmith@example.com";
        Attendee madeProperty = Attendee.parse(content);
        Attendee expectedProperty = Attendee.parse("mailto:jsmith@example.com")
                .withParticipationStatus(ParticipationStatusType.DECLINED);
        assertEquals(expectedProperty, madeProperty);
        assertEquals(content, expectedProperty.toContent());
    }
    
    @Test
    public void canParseAttendee10() throws URISyntaxException
    {
        String content = "ATTENDEE;SENT-BY=\"mailto:sray@example.com\":mailto:jsmith@example.com";
        Attendee madeProperty = Attendee.parse(content);
        Attendee expectedProperty = Attendee.parse("mailto:jsmith@example.com")
                .withSentBy("mailto:sray@example.com");
        assertEquals(expectedProperty, madeProperty);
        assertEquals(content, expectedProperty.toContent());
    }
    
    @Test
    public void canParseAttendee11() throws URISyntaxException
    {
        String content = "ATTENDEE;CN=John Smith;CUTYPE=GROUP;DELEGATED-TO=\"mailto:jdoe@example.com\",\"mailto:jqpublic@example.com\";DELEGATED-FROM=\"mailto:jsmith@example.com\";MEMBER=\"mailto:projectA@example.com\",\"mailto:projectB@example.com\";ROLE=CHAIR;PARTSTAT=DECLINED;RSVP=TRUE;SENT-BY=\"mailto:sray@example.com\":mailto:jsmith@example.com";
        Attendee madeProperty = Attendee.parse(content);
        Attendee expectedProperty = Attendee.parse("mailto:jsmith@example.com")
                .withCommonName("John Smith")
                .withCalendarUser(CalendarUserType.GROUP)
                .withDelegatees("\"mailto:jdoe@example.com\",\"mailto:jqpublic@example.com\"")
                .withDelegators("mailto:jsmith@example.com")
                .withGroupMembership(Arrays.asList(new URI("mailto:projectA@example.com"), new URI("mailto:projectB@example.com")))
                .withParticipationRole(ParticipationRoleType.CHAIR)
                .withParticipationStatus(ParticipationStatusType.DECLINED)
                .withRSVP(true)
                .withSentBy("mailto:sray@example.com");
        assertEquals(ParticipationRoleType.CHAIR, madeProperty.getParticipationRole().getValue());
        assertEquals(ParticipationStatusType.DECLINED, madeProperty.getParticipationStatus().getValue());
        assertEquals(CalendarUserType.GROUP, madeProperty.getCalendarUser().getValue());
        assertEquals(expectedProperty, madeProperty);
        String foldedContent = ICalendarUtilities.foldLine(content).toString();
        assertEquals(foldedContent, expectedProperty.toContent());
    }
    
    @Test // test non-standard ROLE
    public void canParseAttendee12() throws URISyntaxException
    {
        String content = "ATTENDEE;ROLE=GRAND POOBAH:mailto:mrbig@example.com";
        Attendee madeProperty = Attendee.parse(content);
        Attendee expectedProperty = Attendee.parse("mailto:mrbig@example.com")
                .withParticipationRole("GRAND POOBAH");
        assertEquals(expectedProperty, madeProperty);
        assertEquals(content, expectedProperty.toContent());
        assertEquals(madeProperty.getParticipationRole().getValue(), ParticipationRoleType.UNKNOWN);
    }
}
