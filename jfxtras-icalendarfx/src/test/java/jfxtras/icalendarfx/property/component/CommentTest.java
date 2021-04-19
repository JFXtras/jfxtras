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
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.properties.component.descriptive.Comment;
import jfxtras.icalendarfx.utilities.ICalendarUtilities;

public class CommentTest
{
    @Test
    public void canParseComment()
    {
        String content = "COMMENT:The meeting needs to be canceled";
        Comment madeProperty = Comment.parse(content);
        assertEquals(content, madeProperty.toString());
        Comment expectedProperty = Comment.parse("The meeting needs to be canceled");
        assertEquals(expectedProperty, madeProperty);
    }
    
    @Test
    public void canParseComment2()
    {
        String content = "COMMENT;ALTREP=\"CID:part3.msg.970415T083000@example.com\";LANGUAGE=en:The meeting needs to be canceled";
        Comment madeProperty = Comment.parse(content);
        String foldedContent = ICalendarUtilities.foldLine(content).toString();
        assertEquals(foldedContent, madeProperty.toString());
        Comment expectedProperty = Comment.parse("The meeting needs to be canceled")
                .withAlternateText("CID:part3.msg.970415T083000@example.com")
                .withLanguage("en");
        assertEquals(expectedProperty, madeProperty);
    }
    
    @Test
    public void canParseComment3()
    {
        String content = "The meeting needs to be canceled";
        Comment madeProperty = Comment.parse(content);
        assertEquals("COMMENT:" + content, madeProperty.toString());
        Comment expectedProperty = Comment.parse("The meeting needs to be canceled");
        assertEquals(expectedProperty, madeProperty);
    }
    
    @Test
    public void canSetParent()
    {
        String content = "COMMENT;LANGUAGE=en:Department Party";
        Comment property1 = Comment.parse(content);
        VEvent v = new VEvent().withComments(property1);
        assertTrue(v == property1.getParent());
        Comment propertyCopy = new Comment(property1);
        assertEquals(propertyCopy, property1);
        v.getComments().add(propertyCopy);
        assertTrue(v == property1.getParent());
    }
    
//    @Test (expected = IllegalArgumentException.class)
//    public void canCatchDifferentCopyType()
//    {
//        String content = "COMMENT;LANGUAGE=en:Department Party";
//        Comment property1 = Comment.parse(content);
//        Summary propertyCopy = new Summary();
//        property1.copyChildrenInto(propertyCopy);
//        System.out.println(propertyCopy);
////        propertyCopy.copyChildrenFrom(property1);
//    }
}
