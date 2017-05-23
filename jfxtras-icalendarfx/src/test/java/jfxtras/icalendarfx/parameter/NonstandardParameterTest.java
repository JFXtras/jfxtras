package jfxtras.icalendarfx.parameter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.icalendarfx.parameters.NonStandardParameter;

public class NonstandardParameterTest
{
    @Test
    public void canCreateNonStandardParameter()
    {
        String content = "X-PARAM=STRING";
        NonStandardParameter parameter = NonStandardParameter.parse(content);
        assertEquals(content, parameter.toString());
        assertEquals("X-PARAM", parameter.name());
    }

//    @Test
//    public void canCreateEmptyNonStandardParameter()
//    {
//        String content = "X-PARAM=STRING";
//    	NonStandardParameter parameter = new NonStandardParameter();
//        parameter.parseContent(content);
//        assertEquals(content, parameter.toString());
//        assertEquals("X-PARAM", parameter.name());
//    }
}
