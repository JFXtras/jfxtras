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
package jfxtras.icalendarfx.misc;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.Test;

import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.parameters.Encoding.EncodingType;
import jfxtras.icalendarfx.properties.ValueType;
import jfxtras.icalendarfx.properties.component.descriptive.Attachment;
import jfxtras.icalendarfx.properties.component.descriptive.Categories;
import jfxtras.icalendarfx.properties.component.descriptive.Comment;
import jfxtras.icalendarfx.properties.component.descriptive.Summary;
import jfxtras.icalendarfx.properties.component.recurrence.ExceptionDates;
import jfxtras.icalendarfx.properties.component.recurrence.RecurrenceRule;

public class OrdererTest
{
    @Test
    public void canReplaceListElement()
    {
        VEvent builtComponent = new VEvent()
                .withCategories("category1")
                .withSummary("test");
        Categories c1 = new Categories("category3");
		Categories c2 = new Categories("category4");
		builtComponent.setCategories(Arrays.asList(c1, c2));
		builtComponent.orderChild(0, c1);
		builtComponent.orderChild(2, c2);
        assertEquals(2, builtComponent.getCategories().size());
        String expectedContent = "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:category3" + System.lineSeparator() +
                "SUMMARY:test" + System.lineSeparator() +
                "CATEGORIES:category4" + System.lineSeparator() +
                "END:VEVENT";
        VEvent expectedVEvent = VEvent.parse(expectedContent);
        assertEquals(expectedVEvent, builtComponent);
    }

    @Test
    public void canReplaceListElement2()
    {
        VEvent v = new VEvent()
                .withAttachments(new Attachment<URI>(URI.class, "ATTACH:CID:jsmith.part3.960817T083000.xyzMail@example.com"))
                .withSummary("test")
                .withAttachments(Attachment.parse("ATTACH;FMTTYPE=text/plain;ENCODING=BASE64;VALUE=BINARY:TG9yZW"));
        Attachment<?> a1 = Attachment.parse("ATTACH;FMTTYPE=application/postscript:ftp://example.com/reports/r-960812.ps");
        Attachment<?> a2 = new Attachment<String>(String.class, "TG9yZW")
	        .withFormatType("text/plain")
	        .withEncoding(EncodingType.BASE64)
	        .withValueType(ValueType.BINARY);
        v.setAttachments(Arrays.asList(a1,a2));
        v.orderChild(0,a1);
		v.orderChild(2,a2);
        String expectedContent = "BEGIN:VEVENT" + System.lineSeparator() +
                "ATTACH;FMTTYPE=application/postscript:ftp://example.com/reports/r-960812.ps" + System.lineSeparator() +
                "SUMMARY:test" + System.lineSeparator() +
                "ATTACH;FMTTYPE=text/plain;ENCODING=BASE64;VALUE=BINARY:TG9yZW" + System.lineSeparator() +
                "END:VEVENT";
        VEvent expectedVEvent = VEvent.parse(expectedContent);
        assertEquals(expectedVEvent, v);
        assertEquals(expectedContent, v.toString());
    }
    
    @Test
    public void canReplaceIndividualElement()
    {
        VEvent v = new VEvent()
                .withComments("dogs")
                .withSummary("test")
                .withDescription("cats");
        v.setSummary("birds");
        String expectedContent = "BEGIN:VEVENT" + System.lineSeparator() +
                "COMMENT:dogs" + System.lineSeparator() +
                "SUMMARY:birds" + System.lineSeparator() +
                "DESCRIPTION:cats" + System.lineSeparator() +
                "END:VEVENT";
        VEvent expectedVEvent = VEvent.parse(expectedContent);
        assertEquals(expectedVEvent, v);
    }
    
    @Test
    public void canRemoveIndividualElement()
    {
        VEvent v = new VEvent()
                .withComments("dogs")
                .withSummary("test")
                .withDescription("cats");
        v.setSummary((Summary) null);
        String expectedContent = "BEGIN:VEVENT" + System.lineSeparator() +
                "COMMENT:dogs" + System.lineSeparator() +
                "DESCRIPTION:cats" + System.lineSeparator() +
                "END:VEVENT";
        VEvent expectedVEvent = VEvent.parse(expectedContent);
        assertEquals(expectedVEvent, v);
    }
    
    @Test
    public void canAutoOrderListProperty()
    {
        VEvent v = new VEvent().withExceptionDates(LocalDate.of(2016, 4, 27));
        v.setSummary("here:");
        ExceptionDates e2 = new ExceptionDates(LocalDateTime.of(2016, 4, 28, 12, 0));
        v.addChild(e2);
        ExceptionDates e3 = new ExceptionDates(LocalDateTime.of(2016, 4, 29, 12, 0));
        v.addChild(e3);
        String expectedContent = "BEGIN:VEVENT" + System.lineSeparator() +
        		"EXDATE;VALUE=DATE:20160427" + System.lineSeparator() +
        		"SUMMARY:here:" + System.lineSeparator() +
        		"EXDATE:20160428T120000" + System.lineSeparator() +
        		"EXDATE:20160429T120000" + System.lineSeparator() +
        		"END:VEVENT";
        assertEquals(expectedContent, v.toString());
    }
    
    @Test
    public void canManuallyOrderListProperty()
    {
        VEvent v = new VEvent().withExceptionDates(LocalDate.of(2016, 4, 27));
        v.setSummary("here:");
        ExceptionDates e2 = new ExceptionDates(LocalDateTime.of(2016, 4, 28, 12, 0));
        v.getExceptionDates().add(e2);
        v.orderChild(e2);
        String expectedContent = "BEGIN:VEVENT" + System.lineSeparator() +
        		"EXDATE;VALUE=DATE:20160427" + System.lineSeparator() +
        		"SUMMARY:here:" + System.lineSeparator() +
        		"EXDATE:20160428T120000" + System.lineSeparator() +
        		"END:VEVENT";
        assertEquals(expectedContent, v.toString());
    }
    
    @Test // can remove a property and avoid null pointer with toString
    public void canRemoveProperty()
    {
        VEvent vComponent = new VEvent()
                .withSummary("example")
                .withRecurrenceRule("RRULE:FREQ=DAILY");
        vComponent.setRecurrenceRule((RecurrenceRule) null); // remove Recurrence Rule
        assertEquals(1, vComponent.childrenUnmodifiable().size());
        String expectedContent = "BEGIN:VEVENT" + System.lineSeparator() +
                                 "SUMMARY:example" + System.lineSeparator() +
                                 "END:VEVENT";
        assertEquals(expectedContent, vComponent.toString());
    }
    
    @Test // can remove a property and avoid null pointer with toString
    public void canRemoveListProperty()
    {
        VEvent vComponent = new VEvent()
                .withSummary("example")
                .withComments("DOG","CAT","FROG");
        Comment secondComment = vComponent.getComments().get(1);
        vComponent.removeChild(secondComment);
        assertEquals(3, vComponent.childrenUnmodifiable().size());
        assertEquals(2, vComponent.getComments().size());
        String expectedContent = "BEGIN:VEVENT" + System.lineSeparator() +
                                 "SUMMARY:example" + System.lineSeparator() +
                                 "COMMENT:DOG" + System.lineSeparator() +
                                 "COMMENT:FROG" + System.lineSeparator() +
                                 "END:VEVENT";
        assertEquals(expectedContent, vComponent.toString());
    }
    
    @Test // shows removing null property does nothing
    public void canRemoveEmptyProperty()
    {
        VEvent vComponent = new VEvent();
        vComponent.setRecurrenceRule((RecurrenceRule) null); // remove null Recurrence Rule
        assertEquals(0, vComponent.childrenUnmodifiable().size());
        String expectedContent = "BEGIN:VEVENT" + System.lineSeparator() +
                                 "END:VEVENT";
        assertEquals(expectedContent, vComponent.toString());
    }
}
