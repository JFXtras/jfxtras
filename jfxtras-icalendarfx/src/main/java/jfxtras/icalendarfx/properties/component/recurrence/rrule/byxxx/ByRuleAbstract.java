package jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx;

import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import jfxtras.icalendarfx.properties.component.recurrence.rrule.RRulePartBase;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByHour;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByMinute;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByMonth;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByMonthDay;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByRule;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByRuleAbstract;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.BySecond;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.BySetPosition;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByWeekNumber;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByYearDay;

/**
 * BYxxx rule that modify frequency rule (see RFC 5545, iCalendar 3.3.10 Page 42)
 * The BYxxx rules must be applied in a specific order
 * 
 * @author David Bal
 * @see ByMonth
 * @see ByWeekNumber
 * @see ByYearDay
 * @see ByMonthDay
 * @see ByDay
 * @see ByHour
 * @see ByMinute
 * @see BySecond
 * @see BySetPosition
 */
public abstract class ByRuleAbstract<T, U> extends RRulePartBase<List<T>, U> implements ByRule<List<T>>
{
    @Override
    public void setValue(List<T> values)
    {
        super.setValue(values);
    }
    public void setValue(T... values)
    {
        setValue(new ArrayList<>(Arrays.asList(values)));
    }
    public void setValue(String values)
    {
        parseContent(values);
    }
    public U withValue(T... values)
    {
        setValue(values);
        return (U) this;
    }
    public U withValue(String values)
    {
        setValue(values);
        return (U) this;
    }
    

    @Override
    public Stream<Temporal> streamRecurrences(Stream<Temporal> inStream, ChronoUnit chronoUnit, Temporal dateTimeStart) { throw new RuntimeException("not implemented"); }
    
    /*
     * Constructors
     */
    
    ByRuleAbstract()
    {
        super();
    }

    ByRuleAbstract(T... values)
    {
        setValue(values);
    }
    
    // Copy constructor
    ByRuleAbstract(ByRuleAbstract<T, U> source)
    {
        setValue(new ArrayList<>(source.getValue()));
        setParent(source.getParent());
    }

    private final static List<Class<?>> SORT_ORDER = Arrays.asList(
    			ByMonth.class,
    			ByWeekNumber.class,
    			ByYearDay.class,
    			ByMonthDay.class,
    			ByDay.class,
    			ByHour.class,
    			ByMinute.class,
    			BySecond.class,
    			BySetPosition.class
    		);
    @Override
    public int compareTo(ByRule<List<T>> byRule)
    {
    	return SORT_ORDER.indexOf(getClass()) - SORT_ORDER.indexOf(byRule.getClass());
    }
    
    @Override
    public List<String> errors()
    {
        List<String> errors = super.errors();
        if ((getValue() != null) && (getValue().isEmpty()))
        {
            errors.add(name() + " value list is empty.  List MUST have at lease one element."); 
        }

        return errors;
    }
}
