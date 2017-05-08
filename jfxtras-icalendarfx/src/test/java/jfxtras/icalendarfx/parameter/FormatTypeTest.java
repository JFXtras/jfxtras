package jfxtras.icalendarfx.parameter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.icalendarfx.parameters.FormatType;

public class FormatTypeTest
{
    @Test
    public void canParseFormatType()
    {
    	String content = "FMTTYPE=audio/basic";
		FormatType parameter = FormatType.parse(content);
        assertEquals(content, parameter.toString());
    }
    
    @Test
    public void canMakeEmptyFormatType()
    {
		FormatType parameter = new FormatType();
    	assertEquals("FMTTYPE=", parameter.toString());
    }
}
