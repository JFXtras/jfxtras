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
        assertEquals(content, parameter.toContent());
        assertEquals("X-PARAM", parameter.name());
    }

//    @Test
//    public void canCreateNonStandardParameter()
//    {
//        Parameter parameter = NonStandardParameter.parse("X-PARAM=STRING");
//        Delegatees parameter2 = new Delegatees(parameter);
//        assertEquals(parameter, parameter2);
//    }
}
