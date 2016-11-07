package jfxtras.icalendarfx.property.calendar;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.icalendarfx.properties.calendar.Method;
import jfxtras.icalendarfx.properties.calendar.Method.MethodType;

public class MethodTest
{
    @Test
    public void canParseAction()
    {
        Method madeProperty = Method.parse("method:publish");
        String expectedContent = "METHOD:PUBLISH";
        assertEquals(expectedContent, madeProperty.toContent());
        assertEquals(MethodType.PUBLISH, madeProperty.getValue());
    }
}
