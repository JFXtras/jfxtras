package jfxtras.icalendarfx.property.component;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.icalendarfx.properties.component.timezone.TimeZoneName;

public class TimeZoneNameTest
{
    @Test
    public void canParseTimeZoneName()
    {
        String content = "TZNAME;LANGUAGE=fr-CA:HNE";
        TimeZoneName madeProperty = TimeZoneName.parse(content);
        assertEquals(content, madeProperty.toString());
        TimeZoneName expectedProperty = new TimeZoneName()
                .withValue("HNE")
                .withLanguage("fr-CA");
        assertEquals(expectedProperty, madeProperty);
        assertEquals("HNE", madeProperty.getValue());
        assertEquals("fr-CA", madeProperty.getLanguage().getValue());
    }
}
