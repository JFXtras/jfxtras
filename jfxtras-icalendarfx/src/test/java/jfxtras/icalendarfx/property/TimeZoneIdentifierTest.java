package jfxtras.icalendarfx.property;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.time.ZoneId;

import org.junit.Test;

import jfxtras.icalendarfx.properties.ValueType;
import jfxtras.icalendarfx.properties.component.timezone.TimeZoneIdentifier;

public class TimeZoneIdentifierTest
{
    @Test
    public void canParseTimeZoneIdentifier1()
    {
        String content = "TZID:America/Los_Angeles";
        TimeZoneIdentifier madeProperty = TimeZoneIdentifier.parse(content);
        assertEquals(content, madeProperty.toContent());
        TimeZoneIdentifier expectedProperty = new TimeZoneIdentifier(ZoneId.of("America/Los_Angeles"));
//        Class<? extends ZoneId> c = ZoneId.of("America/Los_Angeles").getClass();
//        System.out.println(ZoneId.class.isAssignableFrom(c));
        assertEquals(expectedProperty, madeProperty);
    }
    
    @Test
    public void canParseTimeZoneIdentifier2()
    {
        String content = "TZID;VALUE=TEXT:America/Los_Angeles";
        TimeZoneIdentifier madeProperty = TimeZoneIdentifier.parse(content);
        assertEquals(content, madeProperty.toContent());
        TimeZoneIdentifier expectedProperty = new TimeZoneIdentifier(ZoneId.of("America/Los_Angeles"))
                .withValueType(ValueType.TEXT);
        assertEquals(expectedProperty, madeProperty);
    }

    @Test
    public void canParseTimeZoneIdentifier3()
    {
        String content = "TZID:/US-New_York-New_York";
        TimeZoneIdentifier madeProperty = TimeZoneIdentifier.parse(content);
        assertEquals(content, madeProperty.toContent());
        TimeZoneIdentifier expectedProperty = TimeZoneIdentifier.parse("/US-New_York-New_York");
        assertEquals(expectedProperty, madeProperty);
        assertNull(expectedProperty.getValue());
    }
}
