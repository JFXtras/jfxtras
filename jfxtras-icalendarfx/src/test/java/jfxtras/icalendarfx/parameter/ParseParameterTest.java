package jfxtras.icalendarfx.parameter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.icalendarfx.parameters.AlternateText;
import jfxtras.icalendarfx.parameters.CalendarUser;
import jfxtras.icalendarfx.parameters.Delegatees;
import jfxtras.icalendarfx.parameters.DirectoryEntry;
import jfxtras.icalendarfx.parameters.FormatType;
import jfxtras.icalendarfx.parameters.GroupMembership;

public class ParseParameterTest
{
    @Test // tests enum as value
    public void canParseCalendarUser()
    {
        CalendarUser parameter = CalendarUser.parse("GROUP");
        String expectedContent = "CUTYPE=GROUP";
        assertEquals(expectedContent, parameter.toContent());
    }
    
    @Test // tests list of URI value
    public void canParseDelegatees()
    {
        Delegatees parameter = Delegatees.parse("\"mailto:jdoe@example.com\",\"mailto:jqpublic@example.com\"");
        String expectedContent = "DELEGATED-TO=\"mailto:jdoe@example.com\",\"mailto:jqpublic@example.com\"";
        assertEquals(expectedContent, parameter.toContent());
        assertEquals(2, parameter.getValue().size());
    }
    
    @Test // tests single URI as value
    public void canParseAlternateText()
    {
        AlternateText parameter = AlternateText.parse("\"CID:part3.msg.970415T083000@example.com\"");
        String expectedContent = "ALTREP=\"CID:part3.msg.970415T083000@example.com\"";
        assertEquals(expectedContent, parameter.toContent());
    }
    
    @Test // tests single URI as value
    public void canParseGroupMembership()
    {
        String expectedContent = "\"mailto:projectA@example.com\",\"mailto:projectB@example.com\"";        
        GroupMembership parameter = GroupMembership.parse(expectedContent);
        assertEquals("MEMBER=" + expectedContent, parameter.toContent());
        assertEquals(2, parameter.getValue().size());
    }
    
    @Test // tests list as value
    public void canParseDirectory()
    {
        DirectoryEntry parameter = DirectoryEntry.parse("\"ldap://example.com:6666/o=ABC%20Industries,c=US???(cn=Jim%20Dolittle)\"");
        String expectedContent = "DIR=\"ldap://example.com:6666/o=ABC%20Industries,c=US???(cn=Jim%20Dolittle)\"";
        assertEquals(expectedContent, parameter.toContent());
    }
    
    @Test // tests two-value parameter
    public void canParseFormatType()
    {
        FormatType parameter = FormatType.parse("application/msword");
        String expectedContent = "FMTTYPE=application/msword";
        assertEquals(expectedContent, parameter.toContent());
        assertEquals("application", parameter.getTypeName());
        assertEquals("msword", parameter.getSubtypeName());
    }
}
