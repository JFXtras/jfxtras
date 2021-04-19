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
package jfxtras.icalendarfx.calendar;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import jfxtras.icalendarfx.ICalendarTestAbstract;
import jfxtras.icalendarfx.VCalendar;
import jfxtras.icalendarfx.components.VEvent;

public class VCalendarRecurrenceIDTest extends ICalendarTestAbstract
{
    @Test
    public void canHandleRecurrenceID1()
    {
        VEvent parent = getYearly1();
        VEvent child = getRecurrenceForYearly1();
        VCalendar c = new VCalendar()
                .withVEvents(parent, child);

        assertEquals(2, c.getVEvents().size());
        assertEquals(1, parent.recurrenceChildren().size());
        List<Temporal> expectedRecurrences = Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 10, 0),
                LocalDateTime.of(2017, 11, 9, 10, 0),
                LocalDateTime.of(2018, 11, 9, 10, 0),
                LocalDateTime.of(2019, 11, 9, 10, 0),
                LocalDateTime.of(2020, 11, 9, 10, 0)
                );
        List<Temporal> madeRecurrences = parent.streamRecurrences().limit(5).collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }
    
    @Test
    public void canHandleRecurrenceID2()
    {
        VEvent parent = getYearly1();
        VEvent child = getRecurrenceForYearly1();
        VEvent child2 = getRecurrenceForYearly2();
        VCalendar c = new VCalendar();
        
        // add components out-of-order
        c.setVEvents(new ArrayList<>());
        c.getVEvents().add(child);
        c.orderChild(child);
        c.getVEvents().add(parent);
        c.orderChild(parent);
//        c.childrenUnmodifiable().forEach(System.out::println);
        assertEquals(1, parent.recurrenceChildren().size());
        c.getVEvents().add(child2);
        c.orderChild(child2);
        assertEquals(2, parent.recurrenceChildren().size());
        {
            List<Temporal> expectedRecurrences = Arrays.asList(
                    LocalDateTime.of(2015, 11, 9, 10, 0),
                    LocalDateTime.of(2017, 11, 9, 10, 0),
                    LocalDateTime.of(2019, 11, 9, 10, 0),
                    LocalDateTime.of(2020, 11, 9, 10, 0),
                    LocalDateTime.of(2021, 11, 9, 10, 0)
                    );
            List<Temporal> madeRecurrences = parent.streamRecurrences().limit(5).collect(Collectors.toList());
            assertEquals(expectedRecurrences, madeRecurrences);
        }
        
        // remove child
        c.getVEvents().remove(child);
        assertEquals(1, parent.recurrenceChildren().size());
        {
            List<Temporal> expectedRecurrences = Arrays.asList(
                    LocalDateTime.of(2015, 11, 9, 10, 0),
                    LocalDateTime.of(2016, 11, 9, 10, 0),
                    LocalDateTime.of(2017, 11, 9, 10, 0),
                    LocalDateTime.of(2019, 11, 9, 10, 0),
                    LocalDateTime.of(2020, 11, 9, 10, 0)
                    );
            List<Temporal> madeRecurrences = parent.streamRecurrences().limit(5).collect(Collectors.toList());
            assertEquals(expectedRecurrences, madeRecurrences);
        }
    }
    
    @Test
    public void canHandleRecurrenceID3()
    {
        VEvent parent = getYearly1();
        VEvent child = getRecurrenceForYearly1();
        VEvent child2 = getRecurrenceForYearly2();
        VCalendar cal = new VCalendar();
        
        // add components all at once
        List<VEvent> children = Arrays.asList(child, parent, child2);
        children.forEach(c -> cal.addChild(c));
        assertEquals(2, parent.recurrenceChildren().size());
        {
            List<Temporal> expectedRecurrences = Arrays.asList(
                    LocalDateTime.of(2015, 11, 9, 10, 0),
                    LocalDateTime.of(2017, 11, 9, 10, 0),
                    LocalDateTime.of(2019, 11, 9, 10, 0),
                    LocalDateTime.of(2020, 11, 9, 10, 0),
                    LocalDateTime.of(2021, 11, 9, 10, 0)
                    );
            List<Temporal> madeRecurrences = parent.streamRecurrences().limit(5).collect(Collectors.toList());
            assertEquals(expectedRecurrences, madeRecurrences);
        }
    }

}
