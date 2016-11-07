package jfxtras.icalendarfx.property;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Test;

import jfxtras.icalendarfx.properties.component.change.LastModified;

public class LastModifiedTest
{
    @Test
    public void canParseLastModified()
    {
        String expectedContentLine = "LAST-MODIFIED:19960817T133000Z";
        LastModified property = LastModified.parse(expectedContentLine);
        String madeContentLine = property.toContent();
        assertEquals(expectedContentLine, madeContentLine);
        assertEquals(ZonedDateTime.of(LocalDateTime.of(1996, 8, 17, 13, 30), ZoneId.of("Z")), property.getValue());
    }
}
