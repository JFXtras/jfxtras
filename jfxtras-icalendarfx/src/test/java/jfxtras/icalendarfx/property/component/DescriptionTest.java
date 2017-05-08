package jfxtras.icalendarfx.property.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.net.URISyntaxException;

import org.junit.Test;

import jfxtras.icalendarfx.properties.component.descriptive.Description;
import jfxtras.icalendarfx.utilities.ICalendarUtilities;

public class DescriptionTest
{
    @Test
    public void canParseDescriptionSimple() throws URISyntaxException
    {
        Description description = Description.parse("this is a simple description without parameters");
        String expectedContentLine = "DESCRIPTION:this is a simple description without parameters";
        String madeContentLine = description.toString();
        assertEquals(expectedContentLine, madeContentLine);
    }
    
    @Test
    public void canParseDescriptionComplex() throws URISyntaxException
    {
        String contentLine = "DESCRIPTION;ALTREP=\"CID:part3.msg.970415T083000@example.com\";LANGUAGE=en:Project XYZ Review Meeting will include the following agenda items: (a) Market Overview\\, (b) Finances\\, (c) Project Management";
        Description madeDescription = Description.parse(contentLine);
        Description expectedDescription = Description.parse("Project XYZ Review Meeting will include the following agenda items: (a) Market Overview\\, (b) Finances\\, (c) Project Management")
                .withAlternateText("CID:part3.msg.970415T083000@example.com")
                .withLanguage("en");
        assertEquals(expectedDescription, madeDescription);
        String foldedContent = ICalendarUtilities.foldLine(contentLine).toString();
        assertEquals(foldedContent, expectedDescription.toString());
    }
    
    @Test
    public void canParseDescriptionWithOtherParameters()
    {
        String contentLine = "DESCRIPTION;X-MYPARAMETER=some value;IGNORE ME;X-PARAMETER2=other value:Example description";        
        Description madeDescription = Description.parse(contentLine);
        Description expectedDescription = Description.parse("Example description")
        		.withNonStandard("X-MYPARAMETER=some value", "X-PARAMETER2=other value");
        assertEquals(expectedDescription, madeDescription);
        String foldedContent = ICalendarUtilities.foldLine("DESCRIPTION;X-MYPARAMETER=some value;X-PARAMETER2=other value:Example description").toString();
        assertEquals(foldedContent, expectedDescription.toString());
    }
    
    @Test
    public void canParseEmptyDescription()
    {
        String contentLine = "DESCRIPTION:";
        Description madeDescription = Description.parse(contentLine);
        madeDescription.toString();
        Description expectedDescription = new Description();
        assertEquals(expectedDescription, madeDescription);
        assertEquals("DESCRIPTION:", expectedDescription.toString());
    }
    
    @Test
    public void canBuildEmptyDescription()
    {
        String contentLine = "DESCRIPTION:";
        Description madeDescription = new Description();
        madeDescription.toString();
        assertEquals(contentLine, madeDescription.toString());
        assertNull(madeDescription.getValue());
    }
}
