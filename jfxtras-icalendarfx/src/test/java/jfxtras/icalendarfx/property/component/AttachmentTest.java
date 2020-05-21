/**
 * Copyright (c) 2011-2020, JFXtras
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *    Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *    Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *    Neither the name of the organization nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.icalendarfx.property.component;

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
        String madeContentLine = property.toString();
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
    public void canParseAttachement3()
    {
        String expectedContentLine = "ATTACH;FMTTYPE=audio/basic:ftp://example.com/pub/sounds/bell-01.aud";
        Attachment<URI> property = Attachment.parse(expectedContentLine);
        String madeContentLine = property.toString();
        assertEquals(expectedContentLine, madeContentLine);
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
        assertEquals(foldedContent, expectedProperty.toString());
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
        assertEquals(contentLine, expectedProperty.toString());
    }
    
    @Test
    public void canCopyAttachement() throws URISyntaxException
    {
        String contentLine = "ATTACH;FMTTYPE=text/plain;ENCODING=BASE64;VALUE=BINARY:TG9yZW";
        Attachment<String> property1 = new Attachment<String>(String.class, contentLine);
        Attachment<String> property2 = new Attachment<String>(property1);
        assertEquals(property1, property2);
        assertFalse(property1 == property2);
    }
    
    @Test
    public void canBuildEmptyAttachment()
    {
    	Attachment a = new Attachment();
    	assertEquals("ATTACH:", a.toString());
    }
}
