package jfxtras.icalendarfx.property;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.icalendarfx.properties.component.relationship.Organizer;
import jfxtras.icalendarfx.utilities.ICalendarUtilities;

public class OrganizerTest
{
    @Test
    public void canParseOrganizer()
    {
        String content = "ORGANIZER;CN=John Smith:mailto:jsmith@example.com";
        Organizer madeProperty = Organizer.parse(content);
        Organizer expectedProperty = Organizer.parse("mailto:jsmith@example.com")
                .withCommonName("John Smith");
        assertEquals(expectedProperty, madeProperty);
        assertEquals(content, expectedProperty.toContent());
    }
    
    @Test
    public void canParseOrganizer2()
    {
        String content = "ORGANIZER;CN=John Smith;DIR=\"ldap://example.com:6666/o=ABC%20Industries,c=US???(cn=Jim%20Dolittle)\";LANGUAGE=en;SENT-BY=\"mailto:sray@example.com\":mailto:jsmith@example.com";
        Organizer madeProperty = Organizer.parse(content);
        Organizer expectedProperty = Organizer.parse("mailto:jsmith@example.com")
                .withCommonName("John Smith")
                .withDirectoryEntryReference("ldap://example.com:6666/o=ABC%20Industries,c=US???(cn=Jim%20Dolittle)")
                .withLanguage("en")
                .withSentBy("mailto:sray@example.com");
        assertEquals(expectedProperty, madeProperty);
        assertEquals(ICalendarUtilities.foldLine(content).toString(), expectedProperty.toContent());
    }
    
    @Test
    public void canParseOrganizer3()
    {
        String content = "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org";
        Organizer madeProperty = Organizer.parse(content);
        Organizer expectedProperty = Organizer.parse("mailto:papa@smurf.org")
                .withCommonName("Papa Smurf");
        assertEquals(expectedProperty, madeProperty);
        assertEquals(ICalendarUtilities.foldLine(content).toString(), expectedProperty.toContent());
    }
    
}
