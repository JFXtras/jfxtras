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

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;

import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.properties.component.recurrence.ExceptionDates;

public class ExceptionDatesTest
{
    @Test
    public void canParseExceptions1()
    {
        String content = "EXDATE:20151112T100000,20151115T100000";
        ExceptionDates madeProperty = ExceptionDates.parse(LocalDateTime.class, content);
        assertEquals(content, madeProperty.toString());
        ExceptionDates expectedProperty = new ExceptionDates(LocalDateTime.of(2015, 11, 12, 10, 0), LocalDateTime.of(2015, 11, 15, 10, 0));
        assertEquals(expectedProperty, madeProperty);
        
        List<LocalDateTime> expectedValues = new ArrayList<>(Arrays.asList(LocalDateTime.of(2015, 11, 12, 10, 0), LocalDateTime.of(2015, 11, 15, 10, 0)));
        List<Temporal> madeValues =  madeProperty.getValue().stream().sorted().collect(Collectors.toList());
        assertEquals(expectedValues, madeValues);
    }

    @Test
    public void canParseExceptions2()
    {
        String content = "EXDATE:19960402T010000Z,19960403T010000Z,19960404T010000Z";
        ExceptionDates madeProperty = ExceptionDates.parse(content);
        assertEquals(content, madeProperty.toString());
        Set<Temporal> set = new HashSet<Temporal>( Arrays.asList(
                ZonedDateTime.of(LocalDateTime.of(1996, 4, 2, 1, 0), ZoneId.of("Z")),
                ZonedDateTime.of(LocalDateTime.of(1996, 4, 3, 1, 0), ZoneId.of("Z")),
                ZonedDateTime.of(LocalDateTime.of(1996, 4, 4, 1, 0), ZoneId.of("Z")) ));
        ExceptionDates expectedProperty = new ExceptionDates(set);
        assertEquals(expectedProperty, madeProperty);
        
        Set<ZonedDateTime> expectedValues = new HashSet<>(Arrays.asList(
                ZonedDateTime.of(LocalDateTime.of(1996, 4, 2, 1, 0), ZoneId.of("Z")),
                ZonedDateTime.of(LocalDateTime.of(1996, 4, 3, 1, 0), ZoneId.of("Z")),
                ZonedDateTime.of(LocalDateTime.of(1996, 4, 4, 1, 0), ZoneId.of("Z")) ));
        assertEquals(expectedValues, madeProperty.getValue());
        
        set.add(ZonedDateTime.of(LocalDateTime.of(1996, 4, 5, 1, 0), ZoneId.of("Z")));
        assertEquals(4, expectedProperty.getValue().size());
    }
    
    @Test
    public void canParseExceptions3()
    {
        String content = "EXDATE;VALUE=DATE:20160402";
        ExceptionDates madeProperty = ExceptionDates.parse(LocalDate.class, content);
        assertEquals(content, madeProperty.toString());
        ExceptionDates expectedProperty = new ExceptionDates(
                LocalDate.of(2016, 4, 2) );
        assertEquals(expectedProperty, madeProperty);
        
        Set<LocalDate> expectedValues = new HashSet<>(Arrays.asList( LocalDate.of(2016, 4, 2) ));
        assertEquals(expectedValues, madeProperty.getValue());
    }

    @Test
    public void canCatchWrongTypeExceptions1()
    {
        ExceptionDates e = ExceptionDates.parse("20160228T093000");
        e.getValue().add(LocalDateTime.of(2016, 4, 25, 1, 0));
        e.getValue().add(LocalDate.of(2016, 4, 25));
        assertEquals(1, e.errors().size());
        String expectedError = "Recurrences DateTimeType \"DATE\" doesn't match previous recurrences DateTimeType \"DATE_WITH_LOCAL_TIME\"";
        assertEquals(expectedError, e.errors().get(0));
    }
    
    @Test
    public void canCatchWrongTypeInTwoProperty()
    {
        VEvent v = new VEvent().withExceptionDates(LocalDate.of(2016, 4, 27));
        v.setSummary("here:");
        ExceptionDates e2 = new ExceptionDates(LocalDateTime.of(2016, 4, 27, 12, 0));
        v.getExceptionDates().add(e2);
        v.orderChild(e2);
    }

    @Test
    public void canCatchWrongTypeInProperty()
    {
        ExceptionDates e = ExceptionDates.parse("20160228T093000");
        e.getValue().add(LocalDateTime.of(2016, 4, 25, 1, 0));
        e.getValue().add(LocalDate.of(2016, 4, 25));
        assertEquals(1, e.errors().size());
        String expectedMessage = "Recurrences DateTimeType \"DATE\" doesn't match previous recurrences DateTimeType \"DATE_WITH_LOCAL_TIME\"";
        assertEquals(expectedMessage, e.errors().get(0));
    }
    
    @Test
    public void canCatchWrongTimeZone()
    {
        ExceptionDates e = new ExceptionDates();
        e.setValue(new LinkedHashSet<>(Arrays.asList(ZonedDateTime.of(LocalDateTime.of(1996, 4, 2, 1, 0), ZoneId.of("America/Los_Angeles")))));
        e.getValue().add(ZonedDateTime.of(LocalDateTime.of(1996, 4, 4, 1, 0), ZoneId.of("America/Los_Angeles")));
        e.getValue().add(ZonedDateTime.of(LocalDateTime.of(1996, 4, 5, 1, 0), ZoneId.of("America/New_York")));
        assertEquals(1, e.errors().size());
        String expectedMessage = "ZoneId \"America/New_York\" doesn't match previous ZoneId \"America/Los_Angeles\"";
        assertEquals(expectedMessage, e.errors().get(0));        
    }
    
    @Test (expected = DateTimeException.class)
    public void canCatchWrongExceptionTypeInComponent()
    {
        VEvent e = new VEvent().withExceptionDates(LocalDate.of(2016, 4, 27),
                LocalDateTime.of(2016, 4, 27, 12, 0));
    }
    
    @Test
    public void canCopyExceptions()
    {
        String content = "EXDATE:19960402T010000Z,19960403T010000Z,19960404T010000Z";
        ExceptionDates property1 = ExceptionDates.parse(content);
        ExceptionDates property2 = new ExceptionDates(property1);
        assertEquals(property1, property2);
        assertFalse(property1 == property2);
        assertFalse(property1.getValue() == property2.getValue());
    }
    
    @Test
    public void canCopyExceptions2()
    {
        String content = "EXDATE:19960402T010000Z,19960403T010000Z,19960404T010000Z";
        ExceptionDates property1 = ExceptionDates.parse(content);
        ExceptionDates property2 = new ExceptionDates(property1);
        assertEquals(property1, property2);
        assertFalse(property1 == property2);
        assertFalse(property1.getValue() == property2.getValue());
        
        // make sure wrapped collection is different
        Temporal first = property1.getValue().iterator().next();
        property1.getValue().remove(first);
        assertEquals(2, property1.getValue().size());
        assertEquals(3, property2.getValue().size());
    }
    
    @Test
    public void canCopyEmptyExceptions()
    {
        ExceptionDates property1 = new ExceptionDates();
        ExceptionDates property2 = new ExceptionDates(property1);
        assertEquals(property1, property2);
        assertFalse(property1 == property2);
        assertFalse(property1.getValue() == property2.getValue());
    }
}
