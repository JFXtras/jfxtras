package jfxtras.icalendarfx.parameter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.icalendarfx.parameters.DirectoryEntry;

public class DirectoryEntryReferenceTest
{
    @Test
    public void canCopyDirectory()
    {
        DirectoryEntry parameter = DirectoryEntry.parse("\"ldap://example.com:6666/o=ABC%20Industries,c=US???(cn=Jim%20Dolittle)\"");
        DirectoryEntry parameter2 = new DirectoryEntry(parameter);
        assertEquals(parameter, parameter2);
    }
}
