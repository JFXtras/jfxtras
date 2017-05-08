package jfxtras.icalendarfx.property.component;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.icalendarfx.properties.component.alarm.RepeatCount;

public class RepeatCountTest
{
    @Test
    public void canParseRepeatCount()
    {
        String expectedContent = "REPEAT:0";
        RepeatCount madeProperty = RepeatCount.parse(expectedContent);
        assertEquals(expectedContent, madeProperty.toString());
        assertEquals((Integer) 0, madeProperty.getValue());
    }
    
    @Test
    public void canParseRepeatCount2()
    {
        RepeatCount madeProperty = new RepeatCount(2);
        String expectedContent = "REPEAT:2";
        assertEquals(expectedContent, madeProperty.toString());
        assertEquals((Integer) 2, madeProperty.getValue());
    }
}
