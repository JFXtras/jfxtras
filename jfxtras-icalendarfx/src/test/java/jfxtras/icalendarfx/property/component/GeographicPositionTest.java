package jfxtras.icalendarfx.property.component;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.icalendarfx.properties.component.descriptive.GeographicPosition;

public class GeographicPositionTest
{
    @Test
    public void canParseGeographicPosition()
    {
        String content = "GEO:37.386013;-122.082932";
        GeographicPosition madeProperty = GeographicPosition.parse(content);
        assertEquals(content, madeProperty.toString());
        GeographicPosition expectedProperty = new GeographicPosition()
                .withLatitude(37.386013)
                .withLongitude(-122.082932);

        assertEquals(expectedProperty, madeProperty);
        assertEquals(content, madeProperty.toString());
        assertEquals(content, expectedProperty.toString());
    }
    
    @Test
    public void canBuildGeographicPosition()
    {
        Double latitude = 37.386013;
        Double longitude = -122.082932;
        GeographicPosition property = new GeographicPosition(latitude, longitude);
        String content = "GEO:37.386013;-122.082932";
        assertEquals(content, property.toString());
        assertEquals(latitude, property.getLatitude());
        assertEquals(longitude, property.getLongitude());
    }

}
