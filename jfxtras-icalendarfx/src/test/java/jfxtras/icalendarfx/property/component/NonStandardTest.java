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
        assertEquals(content, madeProperty.toString());
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
        assertEquals(content, madeProperty.toString());
        NonStandardProperty expectedProperty = NonStandardProperty.parse("http://www.example.org/mysubj.au")
                .withValueType(ValueType.UNIFORM_RESOURCE_IDENTIFIER)
                .withFormatType("audio/basic")
                .withPropertyName("X-ABC-MMSUBJ");
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
        assertEquals(content, copiedProperty.toString());
    }
    
//    @Test
//    public void canCreateEmptyNonStandard()
//    {
//        String content = "X-MYPROP;VALUE=BOOLEAN:FALSE";
//        NonStandardProperty p = new NonStandardProperty();
//        p.parseContent(content);
//        System.out.println(p);
//        System.out.println(p.name());
//    }
}
