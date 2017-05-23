package jfxtras.icalendarfx.parameter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.icalendarfx.parameters.CommonName;

public class CommonNameTest
{
    @Test // tests String as value
    public void canParseCommonName()
    {
        CommonName parameter = CommonName.parse("CN=David Bal");
        String expectedContent = "CN=David Bal";
        assertEquals(expectedContent, parameter.toString());
    }
    
    @Test // tests String as value
    public void canParseCommonName2()
    {
        CommonName parameter = CommonName.parse("CN=\"John Smith\"");
        String expectedContent = "CN=John Smith";
        assertEquals(expectedContent, parameter.toString());
    }
    
    @Test
    public void canMakeEmptyCommonName()
    {
    	CommonName parameter = new CommonName();
    	assertEquals("CN=", parameter.toString());
    }
}
