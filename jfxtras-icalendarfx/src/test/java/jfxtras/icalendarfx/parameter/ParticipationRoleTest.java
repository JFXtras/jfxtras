package jfxtras.icalendarfx.parameter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.icalendarfx.parameters.ParticipationRole;
import jfxtras.icalendarfx.parameters.ParticipationRole.ParticipationRoleType;

public class ParticipationRoleTest
{
    @Test
    public void canParseKnownRole()
    {
        String expectedContent = "CHAIR";
        ParticipationRole p = ParticipationRole.parse(expectedContent);
        assertEquals("ROLE=" + expectedContent, p.toContent());
        assertEquals(ParticipationRoleType.CHAIR, p.getValue());
    }

    @Test
    public void canParseUnknownRole()
    {
        String expectedContent = "GRAND-POOBAH";
        ParticipationRole p = ParticipationRole.parse(expectedContent);
        assertEquals("ROLE=" + expectedContent, p.toContent());
        assertEquals(ParticipationRoleType.UNKNOWN, p.getValue());
    }

}
