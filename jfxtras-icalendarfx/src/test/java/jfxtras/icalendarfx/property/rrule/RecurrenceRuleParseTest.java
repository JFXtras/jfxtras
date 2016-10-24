package jfxtras.icalendarfx.property.rrule;

import static org.junit.Assert.assertEquals;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Test;

import jfxtras.icalendarfx.properties.component.recurrence.rrule.FrequencyType;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule2;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByMonth;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByMonthDay;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByWeekNumber;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay.ByDayPair;

public class RecurrenceRuleParseTest
{
    /** tests parsing FREQ=YEARLY;INTERVAL=2;BYMONTH=1;BYDAY=SU */
    @Test
    public void canParseRRule1()
    {
        String s = "FREQ=YEARLY;INTERVAL=2;BYMONTH=1;BYDAY=SU";
        RecurrenceRule2 rRule = RecurrenceRule2.parse(s);
        RecurrenceRule2 expectedRRule = new RecurrenceRule2()
                .withFrequency(FrequencyType.YEARLY)
                .withInterval(2)
                .withByRules(new ByMonth(Month.JANUARY), new ByDay(DayOfWeek.SUNDAY));
        assertEquals(s, expectedRRule.toContent());
        assertEquals(expectedRRule, rRule);
    }
    
    @Test
    public void canParseRRule2()
    {
        String s = "FREQ=MONTHLY;BYDAY=SA;BYMONTHDAY=7,8,9,10,11,12,13";
        RecurrenceRule2 rRule = RecurrenceRule2.parse(s);
        RecurrenceRule2 expectedRRule = new RecurrenceRule2()
                .withFrequency(FrequencyType.MONTHLY)
                .withByRules(new ByDay(DayOfWeek.SATURDAY), new ByMonthDay(7,8,9,10,11,12,13));
        assertEquals(s, expectedRRule.toContent());
        assertEquals(s, rRule.toContent());
        assertEquals(expectedRRule, rRule);
    }
    
    @Test
    public void canParseRRule3()
    {
        String s = "FREQ=YEARLY;BYWEEKNO=20;BYDAY=2MO,3MO";
        RecurrenceRule2 rRule = RecurrenceRule2.parse(s);
        RecurrenceRule2 expectedRRule = new RecurrenceRule2()
                .withFrequency(FrequencyType.YEARLY)
                .withByRules(new ByWeekNumber(20), new ByDay(new ByDayPair(DayOfWeek.MONDAY, 2), new ByDayPair(DayOfWeek.MONDAY, 3)));
        assertEquals(expectedRRule, rRule);
    }

    @Test
    public void canParseRRule4()
    {
        String s = "UNTIL=20151201T100000Z;INTERVAL=2;FREQ=DAILY";
        RecurrenceRule2 rRule = RecurrenceRule2.parse(s);
        RecurrenceRule2 expectedRRule = new RecurrenceRule2()
                .withUntil(ZonedDateTime.of(LocalDateTime.of(2015, 12, 1, 10, 0),ZoneId.of("Z")))
                .withInterval(2)
                .withFrequency(FrequencyType.DAILY);
        assertEquals(expectedRRule, rRule);
    }
}
