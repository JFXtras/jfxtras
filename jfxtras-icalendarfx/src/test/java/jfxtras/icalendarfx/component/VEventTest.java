package jfxtras.icalendarfx.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import jfxtras.icalendarfx.ICalendarTestAbstract;
import jfxtras.icalendarfx.components.VComponent;
import jfxtras.icalendarfx.components.VComponentBase;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule2;
import jfxtras.icalendarfx.properties.component.time.DateTimeEnd;
import jfxtras.icalendarfx.properties.component.time.TimeTransparency;
import jfxtras.icalendarfx.properties.component.time.TimeTransparency.TimeTransparencyType;

/**
 * Test following components:
 * @see VEvent
 * 
 * for the following properties:
 * @see TimeTransparency
 * 
 * @author David Bal
 *
 */
public class VEventTest extends ICalendarTestAbstract
{
    @Test
    public void canBuildVEvent()
    {
        VEvent builtComponent = new VEvent()
                .withDateTimeEnd(LocalDate.of(1997, 3, 1))
                .withTimeTransparency(TimeTransparencyType.OPAQUE);
        String componentName = builtComponent.name();
        
        String content = "BEGIN:" + componentName + System.lineSeparator() +
                "DTEND;VALUE=DATE:19970301" + System.lineSeparator() +
                "TRANSP:OPAQUE" + System.lineSeparator() +
                "END:" + componentName;
                
        VEvent madeComponent = VEvent.parse(content);
        assertEquals(madeComponent, builtComponent);
        assertEquals(content, builtComponent.toContent());
    }
        
    @Test (expected = DateTimeException.class)
    public void canCatchBothDurationAndDTEnd()
    {
        Thread.setDefaultUncaughtExceptionHandler((t1, e) ->
        {
            throw (RuntimeException) e;
        });
        new VEvent()
                .withDateTimeEnd(LocalDate.of(1997, 3, 1))
                .withDuration(Duration.ofMinutes(30));
    }
    
    @Test (expected = DateTimeException.class)
    public void canCatchBothDurationAndDTEnd2()
    {
        Thread.setDefaultUncaughtExceptionHandler((t1, e) ->
        {
            throw (RuntimeException) e;
        });
       new VEvent()
             .withDuration(Duration.ofMinutes(30))
             .withDateTimeEnd(LocalDate.of(1997, 3, 1));
    }
    
    @Test
    public void canChangeDTEndToDuration()
    {
        VEvent builtComponent = new VEvent()
             .withDateTimeEnd(LocalDate.of(1997, 3, 1));
        assertEquals(LocalDate.of(1997, 3, 1), builtComponent.getDateTimeEnd().getValue());
        builtComponent.setDateTimeEnd((DateTimeEnd) null);
        builtComponent.withDateTimeEnd((DateTimeEnd) null).withDuration("PT15M");
        assertEquals(Duration.ofMinutes(15), builtComponent.getDuration().getValue());
        assertNull(builtComponent.getDateTimeEnd());
    }
    
    
    @Test
    public void canStreamWithEnd()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 20, 0))
                .withDateTimeEnd(LocalDateTime.of(2015, 11, 10, 2, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withCount(6)
                        .withFrequency("DAILY")
                        .withInterval(3));
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 15, 20, 0)
              , LocalDateTime.of(2015, 11, 18, 20, 0)
              , LocalDateTime.of(2015, 11, 21, 20, 0)
              , LocalDateTime.of(2015, 11, 24, 20, 0)
                ));
        List<Temporal> madeDates = e.streamRecurrences(LocalDateTime.of(2015, 11, 15, 22, 0))
               .collect(Collectors.toList());
        assertEquals(expectedDates, madeDates);
    }
    
    @Test
    public void canStreamWithRange()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 20, 0))
                .withDuration(Duration.ofHours(6))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency("DAILY")
                        .withInterval(3));
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 15, 20, 0)
              , LocalDateTime.of(2015, 11, 18, 20, 0)
              , LocalDateTime.of(2015, 11, 21, 20, 0)
                ));
        List<Temporal> madeDates = e.streamRecurrences(LocalDateTime.of(2015, 11, 14, 20, 0), 
                                                           LocalDateTime.of(2015, 11, 22, 0, 0))
               .collect(Collectors.toList());
        assertEquals(expectedDates, madeDates);
    }

    /** use {@link VComponentBase#copyComponentFrom} */
    @Test
    public void canCopyComponent()
    {
        VEvent e = getYearly1();
        VEvent e2 = new VEvent();
        e.copyInto(e2);
//        e2.copyChildrenFrom(e);
        assertEquals(e, e2);
    }

    // Use copy constructor
    @Test
    public void canCopyComponent2()
    {
        VEvent e = getYearly1();
        VEvent e2 = new VEvent(e);
        assertEquals(e, e2);
    }
    
    @Test // use reflection
    public void canCopyComponent3()
    {
        VComponent e = getYearly1();
        try
        {
            VComponent e2 = e.getClass().newInstance();
            e.copyInto(e2);
//            e2.copyChildrenFrom(e);
            assertEquals(e, e2);
            assertFalse(e == e2);
        } catch (InstantiationException | IllegalAccessException e1)
        {
            e1.printStackTrace();
        }
    }    
}