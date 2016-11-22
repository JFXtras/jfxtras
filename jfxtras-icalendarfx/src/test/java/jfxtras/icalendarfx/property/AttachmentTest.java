package jfxtras.icalendarfx.property;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

import jfxtras.icalendarfx.parameters.Encoding.EncodingType;
import jfxtras.icalendarfx.properties.ValueType;
import jfxtras.icalendarfx.properties.component.descriptive.Attachment;
import jfxtras.icalendarfx.utilities.ICalendarUtilities;

public class AttachmentTest
{
    @Test
    public void canParseAttachement1()
    {
        Attachment<URI> property = new Attachment<URI>(URI.class, "ATTACH:CID:jsmith.part3.960817T083000.xyzMail@example.com");
        String expectedContentLine = "ATTACH:CID:jsmith.part3.960817T083000.xyzMail@example.com";
        String madeContentLine = property.toContent();
        assertEquals(expectedContentLine, madeContentLine);
    }
    
    @Test
    public void canParseAttachement2()
    {
        String contentLine = "ATTACH;FMTTYPE=text/plain;ENCODING=BASE64;VALUE=BINARY:TG9yZW";
        Attachment<String> madeProperty = new Attachment<String>(String.class, contentLine);
        Attachment<String> expectedProperty = new Attachment<String>(String.class, "TG9yZW")
                .withFormatType("text/plain")
                .withEncoding(EncodingType.BASE64)
                .withValueType(ValueType.BINARY);
        assertEquals(expectedProperty, madeProperty);
        assertFalse(expectedProperty == madeProperty);
    }
    
    @Test
    public void canParseAttachementComplex() throws URISyntaxException
    {
        String contentLine = "ATTACH;FMTTYPE=application/postscript:ftp://example.com/pub/reports/r-960812.ps";
        Attachment<URI> madeProperty = new Attachment<URI>(URI.class, contentLine);
        Attachment<URI> expectedProperty = new Attachment<URI>(URI.class, "ftp://example.com/pub/reports/r-960812.ps")
                .withFormatType("application/postscript");
        assertEquals(expectedProperty, madeProperty);
        String foldedContent = ICalendarUtilities.foldLine(contentLine).toString();
        assertEquals(foldedContent, expectedProperty.toContent());
    }
    
    @Test
    public void canParseAttachementComplex2() throws URISyntaxException
    {
        String contentLine = "ATTACH;FMTTYPE=text/plain;ENCODING=BASE64;VALUE=BINARY:TG9yZW";
        Attachment<String> madeProperty = new Attachment<String>(String.class, contentLine);
        Attachment<String> expectedProperty = new Attachment<String>(String.class, "TG9yZW")
                .withFormatType("text/plain")
                .withEncoding(EncodingType.BASE64)
                .withValueType(ValueType.BINARY);
        assertEquals(expectedProperty, madeProperty);
        assertEquals(contentLine, expectedProperty.toContent());
    }
    
    @Test
    public void canCopyAttachement() throws URISyntaxException
    {
        String contentLine = "ATTACH;FMTTYPE=text/plain;ENCODING=BASE64;VALUE=BINARY:TG9yZW";
        Attachment<String> property1 = new Attachment<String>(String.class, contentLine);
        Attachment<String> property2 = new Attachment<String>(property1);
        System.out.println(property2.toContent());
        assertEquals(property1, property2);
        assertFalse(property1 == property2);
    }
}
