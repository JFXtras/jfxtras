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
