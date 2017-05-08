package jfxtras.icalendarfx.misc;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

import jfxtras.icalendarfx.properties.component.descriptive.Summary;

public class CreateElementsTests
{   
    @Test
    public void canUseParseFactory() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
    {
    	Method parseMethod = Summary.class.getMethod("parse", String.class);
    	String content = "test summary";
		Summary s = (Summary) parseMethod.invoke(null, content);
    	assertEquals("SUMMARY:" + content, s.toString());
    	assertEquals(s, Summary.parse(content));
    }
    
    @Test
    public void canMakeEmptySummary()
    {
    	Summary s = Summary.parse("SUMMARY:");
    	Summary s2 = new Summary();
		assertEquals(s, s2);
    }
}
