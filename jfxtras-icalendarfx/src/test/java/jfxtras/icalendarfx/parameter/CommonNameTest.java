package jfxtras.icalendarfx.parameter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.icalendarfx.parameters.CommonName;

public class CommonNameTest
{
    @Test // tests String as value
    public void canParseCommonName()
    {
        CommonName parameter = CommonName.parse("David Bal");
        String expectedContent = "CN=David Bal";
        assertEquals(expectedContent, parameter.toContent());
    }
    
    @Test // tests String as value
    public void canParseCommonName2()
    {
        CommonName parameter = CommonName.parse("\"John Smith\"");
        String expectedContent = "CN=John Smith";
        assertEquals(expectedContent, parameter.toContent());
    }
}
