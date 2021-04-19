/**
 * Copyright (c) 2011-2021, JFXtras
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
 * DISCLAIMED. IN NO EVENT SHALL JFXTRAS BE LIABLE FOR ANY
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
