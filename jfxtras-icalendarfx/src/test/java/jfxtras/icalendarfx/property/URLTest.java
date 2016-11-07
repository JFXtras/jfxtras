package jfxtras.icalendarfx.property;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

import jfxtras.icalendarfx.properties.component.relationship.UniformResourceLocator;

public class URLTest
{
    @Test
    public void canParseUniformResourceLocator() throws URISyntaxException
    {
        String expectedContentLine = "URL:http://example.com/pub/calendars/jsmith/mytime.ics";
        UniformResourceLocator property = UniformResourceLocator.parse(expectedContentLine);
        String madeContentLine = property.toContent();
        System.out.println(madeContentLine);
        assertEquals(expectedContentLine, madeContentLine);
        assertEquals(new URI("http://example.com/pub/calendars/jsmith/mytime.ics"), property.getValue());
    }
}
