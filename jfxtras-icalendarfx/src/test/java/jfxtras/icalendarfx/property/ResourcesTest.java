package jfxtras.icalendarfx.property;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.icalendarfx.properties.component.descriptive.Resources;
import jfxtras.icalendarfx.utilities.ICalendarUtilities;

public class ResourcesTest
{
    @Test
    public void canParseResources()
    {
        String content = "RESOURCES:EASEL,PROJECTOR,VCR";
        Resources madeProperty = Resources.parse(content);
        assertEquals(content, madeProperty.toContent());
        Resources expectedProperty = Resources.parse("RESOURCES:EASEL,PROJECTOR,VCR");
        assertEquals(expectedProperty, madeProperty);
        assertEquals(3, madeProperty.getValue().size());
    }
    
    @Test
    public void canParseResources2()
    {
        String content = "RESOURCES;ALTREP=\"http://xyzcorp.com/conf-rooms/f123.vcf\";LANGUAGE=fr:Nettoyeur haute pression";
        Resources madeProperty = Resources.parse(content);
        String foldedContent = ICalendarUtilities.foldLine(content).toString();
        assertEquals(foldedContent, madeProperty.toContent());
        Resources expectedProperty = new Resources("Nettoyeur haute pression")
                .withAlternateText("http://xyzcorp.com/conf-rooms/f123.vcf")
                .withLanguage("fr");
        assertEquals(expectedProperty, madeProperty);
    }
}
