package jfxtras.icalendarfx.parameter.rrule;

import static org.junit.Assert.assertEquals;

import java.time.DayOfWeek;
import java.util.Arrays;

import org.junit.Test;

import jfxtras.icalendarfx.properties.component.recurrence.rrule.FrequencyType;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.RecurrenceRuleValue;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay;

public class ByRuleTest
{
    @Test
    public void canParseByDay()
    {
    	ByDay b = new ByDay(DayOfWeek.FRIDAY);
    	RecurrenceRuleValue r = new RecurrenceRuleValue()
        	.withFrequency(FrequencyType.DAILY)
        	.withByRules(b);
    	assertEquals(Arrays.asList(b), r.getByRules());
    }
}
