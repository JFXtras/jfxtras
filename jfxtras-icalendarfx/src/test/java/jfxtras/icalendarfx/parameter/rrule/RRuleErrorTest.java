package jfxtras.icalendarfx.parameter.rrule;

import static org.junit.Assert.assertEquals;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import jfxtras.icalendarfx.properties.component.recurrence.rrule.FrequencyType;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.RecurrenceRuleValue;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay;

public class RRuleErrorTest
{
    @Test
    public void canDetectIntervalError()
    {
        RecurrenceRuleValue rrule = new RecurrenceRuleValue()
                .withFrequency(FrequencyType.YEARLY)
                .withInterval(0); // invalid
        assertEquals(1, rrule.errors().size());
    }

    @Test
    public void canDetectMissingFrequency()
    {
        RecurrenceRuleValue rrule = new RecurrenceRuleValue();
        assertEquals(1, rrule.errors().size());
    }
    
    @Test
    public void canDetectCountAndUntilFrequency()
    {
        RecurrenceRuleValue rrule = new RecurrenceRuleValue()
                .withFrequency(FrequencyType.YEARLY)
                .withCount(10)
                .withUntil("19970610T172345Z"); // invalid
        assertEquals(1, rrule.errors().size());
    }

    @Test (expected = IllegalArgumentException.class)
    public void canDetectDuplicateByRule()
    {
        RecurrenceRuleValue.parse("FREQ=WEEKLY;BYDAY=TU;BYDAY=TU");
    }

    @Test (expected = IllegalArgumentException.class)
    public void canDetectDuplicateByRule2()
    {
    	RecurrenceRuleValue.parse("FREQ=WEEKLY;BYDAY=TU;BYDAY=FR");
    }

    @Test
    public void canDetectDuplicateByRule3()
    {
        RecurrenceRuleValue rrule = RecurrenceRuleValue.parse("FREQ=WEEKLY;BYMONTH=1;BYDAY=TU");
        rrule.getByRules().add(new ByDay(DayOfWeek.FRIDAY));
        List<String> expectedErrors = Arrays.asList("RRULE:ByDay can only occur once in a RRULE.");
        assertEquals(expectedErrors, rrule.errors());
    }
}
