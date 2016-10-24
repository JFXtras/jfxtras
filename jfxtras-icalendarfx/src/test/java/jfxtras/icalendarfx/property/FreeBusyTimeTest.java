package jfxtras.icalendarfx.property;

import static org.junit.Assert.assertEquals;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAmount;

import org.junit.Test;

import javafx.util.Pair;
import jfxtras.icalendarfx.parameters.FreeBusyType.FreeBusyTypeEnum;
import jfxtras.icalendarfx.properties.component.time.FreeBusyTime;
import jfxtras.icalendarfx.utilities.ICalendarUtilities;

public class FreeBusyTimeTest
{
    @Test
    public void canParseFreeBusyTime1()
    {
        String content = "FREEBUSY;FBTYPE=BUSY-UNAVAILABLE:19970308T160000Z/PT8H30M";
        FreeBusyTime madeProperty = FreeBusyTime.parse(content);
        assertEquals(content, madeProperty.toContent());
        FreeBusyTime expectedProperty = FreeBusyTime.parse("19970308T160000Z/PT8H30M")
                .withFreeBusyType(FreeBusyTypeEnum.BUSY_UNAVAILABLE);
        assertEquals(expectedProperty, madeProperty);
        Pair<ZonedDateTime, TemporalAmount> expectedValue = new Pair<ZonedDateTime, TemporalAmount>(
                ZonedDateTime.of(LocalDateTime.of(1997, 3, 8, 16, 0), ZoneId.of("Z")), Duration.ofHours(8).plusMinutes(30));
        assertEquals(expectedValue.getKey(), madeProperty.getValue().get(0).getKey());
        assertEquals(expectedValue.getValue(), madeProperty.getValue().get(0).getValue());
    }
    
    @Test
    public void canParseFreeBusyTime2()
    {
        String content = "FREEBUSY;FBTYPE=FREE:19970308T160000Z/PT3H,19970308T200000Z/PT1H,19970308T230000Z/19970309T000000Z";
        FreeBusyTime madeProperty = FreeBusyTime.parse(content);
        String foldedContent = ICalendarUtilities.foldLine("FREEBUSY;FBTYPE=FREE:19970308T160000Z/PT3H,19970308T200000Z/PT1H,19970308T230000Z/PT1H").toString();
        assertEquals(foldedContent, madeProperty.toContent());
        FreeBusyTime expectedProperty = FreeBusyTime.parse("19970308T160000Z/PT3H,19970308T200000Z/PT1H,19970308T230000Z/19970309T000000Z")
                .withFreeBusyType(FreeBusyTypeEnum.FREE);
        assertEquals(expectedProperty, madeProperty);
        Pair<ZonedDateTime, TemporalAmount> expectedValue1 = new Pair<ZonedDateTime, TemporalAmount>(
                ZonedDateTime.of(LocalDateTime.of(1997, 3, 8, 16, 0), ZoneId.of("Z")), Duration.ofHours(3));
        assertEquals(expectedValue1.getKey(), madeProperty.getValue().get(0).getKey());
        assertEquals(expectedValue1.getValue(), madeProperty.getValue().get(0).getValue());
        
        Pair<ZonedDateTime, TemporalAmount> expectedValue2 = new Pair<ZonedDateTime, TemporalAmount>(
                ZonedDateTime.of(LocalDateTime.of(1997, 3, 8, 20, 0), ZoneId.of("Z")), Duration.ofHours(1));
        assertEquals(expectedValue2.getKey(), madeProperty.getValue().get(1).getKey());
        assertEquals(expectedValue2.getValue(), madeProperty.getValue().get(1).getValue());
        
        Pair<ZonedDateTime, TemporalAmount> expectedValue3 = new Pair<ZonedDateTime, TemporalAmount>(
                ZonedDateTime.of(LocalDateTime.of(1997, 3, 8, 23, 0), ZoneId.of("Z")), Duration.ofHours(1));
        assertEquals(expectedValue3.getKey(), madeProperty.getValue().get(2).getKey());
        assertEquals(expectedValue3.getValue(), madeProperty.getValue().get(2).getValue());
    }
}
