package jfxtras.icalendarfx.property.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import jfxtras.icalendarfx.parameters.Language;
import jfxtras.icalendarfx.properties.component.descriptive.Summary;

public class SummaryTest
{    
    @Test
    public void canParseSummary1()
    {
        String content = "SUMMARY:TEST SUMMARY";
        Summary madeProperty = Summary.parse(content);
        assertEquals(content, madeProperty.toString());
        Summary expectedProperty = Summary.parse("TEST SUMMARY");
        assertEquals(expectedProperty, madeProperty);
        assertEquals("TEST SUMMARY", madeProperty.getValue());
    }
    
    @Test
    public void canParseSummary2()
    {
        String content = "SUMMARY;ALTREP=\"cid:part1.0001@example.org\";LANGUAGE=en:Department Party";
        Summary madeProperty = Summary.parse(content);
        assertEquals(content, madeProperty.toString());
        Summary expectedProperty = Summary.parse("Department Party")
                .withAlternateText("cid:part1.0001@example.org")
                .withLanguage("en");
        assertEquals(expectedProperty, madeProperty);
    }
    
    @Test
    public void canCopySummary()
    {
        String content = "SUMMARY;LANGUAGE=en;ALTREP=\"cid:part1.0001@example.org\":Department Party";
        Summary property1 = Summary.parse(content);
        Summary property2 = new Summary(property1);
        assertEquals(property2, property1);
        assertFalse(property2 == property1);
        assertEquals(content, property1.toString());
        assertEquals(content, property2.toString());
    }
    
    @Test
    public void canRemoveParameter()
    {
        String content = "SUMMARY;LANGUAGE=en:Department Party";
        Summary property1 = Summary.parse(content);
        property1.setLanguage((Language) null);
        Summary expectedProperty = Summary.parse("SUMMARY:Department Party");
        assertEquals(expectedProperty, property1);
    }
    
    @Test
    public void canParseSummaryOnlyValue()
    {
        String content = "TEST SUMMARY";
        Summary madeProperty = Summary.parse(content);
        assertEquals("SUMMARY:TEST SUMMARY", madeProperty.toString());
        Summary expectedProperty = Summary.parse("TEST SUMMARY");
        assertEquals(expectedProperty, madeProperty);
        assertEquals("TEST SUMMARY", madeProperty.getValue());
    }
    
    @Test
    public void canParseEmptySummary()
    {
        String content = "";
        Summary madeProperty = Summary.parse(content);
        assertEquals("SUMMARY:", madeProperty.toString());        
        assertEquals(null, madeProperty.getValue());
    }
    
    @Test
    public void canCreateEmptySummary()
    {
        String content = null;
        assertNull(Summary.parse(content));
    }
}
