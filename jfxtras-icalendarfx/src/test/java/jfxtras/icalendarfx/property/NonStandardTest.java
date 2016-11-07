package jfxtras.icalendarfx.property;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

import jfxtras.icalendarfx.properties.ValueType;
import jfxtras.icalendarfx.properties.component.misc.NonStandardProperty;

public class NonStandardTest
{
    @Test
    public void canParseNonStandard1()
    {
        String content = "X-MYPROP;VALUE=BOOLEAN:FALSE";
        NonStandardProperty madeProperty = NonStandardProperty.parse(content);
        assertEquals(content, madeProperty.toContent());
        NonStandardProperty expectedProperty = NonStandardProperty.parse("FALSE")
                .withPropertyName("X-MYPROP")
                .withValueType(ValueType.BOOLEAN);
        assertEquals(expectedProperty, madeProperty);
        assertEquals(Boolean.FALSE, madeProperty.getValue());
    }
    
    @Test
    public void canParseNonStandard2() throws URISyntaxException
    {
        String content = "X-ABC-MMSUBJ;VALUE=URI;FMTTYPE=audio/basic:http://www.example.org/mysubj.au";
        NonStandardProperty madeProperty = NonStandardProperty.parse(content);
        System.out.println(madeProperty.toContent());
//        System.out.println(madeProperty.toContent());
        assertEquals(content, madeProperty.toContent());
        NonStandardProperty expectedProperty = NonStandardProperty.parse("http://www.example.org/mysubj.au")
                .withValueType(ValueType.UNIFORM_RESOURCE_IDENTIFIER)
                .withFormatType("audio/basic")
                .withPropertyName("X-ABC-MMSUBJ");

        System.out.println(madeProperty.getValueType().getValue());
        System.out.println(expectedProperty.getValueType().getValue());
        assertEquals(expectedProperty, madeProperty);
        assertEquals(ValueType.UNIFORM_RESOURCE_IDENTIFIER, madeProperty.getValueType().getValue());
        assertEquals(new URI("http://www.example.org/mysubj.au"), madeProperty.getValue());
    }

    @Test
    public void canCopyNonStandard()
    {
        String content = "X-MYPROP;VALUE=BOOLEAN:FALSE";
        NonStandardProperty madeProperty = NonStandardProperty.parse(content);
        NonStandardProperty copiedProperty = new NonStandardProperty(madeProperty);
        assertEquals(copiedProperty, madeProperty);
        assertEquals(content, copiedProperty.toContent());
    }
}
