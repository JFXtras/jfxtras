package jfxtras.icalendarfx.property;

import static org.junit.Assert.assertEquals;

import java.time.Duration;
import java.time.Period;

import org.junit.Test;

import jfxtras.icalendarfx.properties.component.time.DurationProp;

public class DurationTest
{
    @Test
    public void canParseDuration1()
    {
        String content = "DURATION:PT15M";
        DurationProp madeProperty = DurationProp.parse(content);
        assertEquals(content, madeProperty.toContent());
        DurationProp expectedProperty = DurationProp.parse("DURATION:PT15M");
        assertEquals(expectedProperty, madeProperty);
        assertEquals(Duration.ofMinutes(15), madeProperty.getValue());
    }
    
    @Test
    public void canParseDuration2()
    {
        String content = "DURATION:P2D";
        DurationProp madeProperty = DurationProp.parse(content);
        assertEquals(content, madeProperty.toContent());
        DurationProp expectedProperty = DurationProp.parse("DURATION:P2D");
        assertEquals(expectedProperty, madeProperty);
        assertEquals(Period.ofDays(2), madeProperty.getValue());
    }
}
