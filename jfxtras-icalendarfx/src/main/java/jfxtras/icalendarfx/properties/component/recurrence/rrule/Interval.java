package jfxtras.icalendarfx.properties.component.recurrence.rrule;

import java.util.Collections;
import java.util.List;

import jfxtras.icalendarfx.properties.component.recurrence.rrule.Interval;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.RRulePartBase;

/**
 * INTERVAL
 * RFC 5545 iCalendar 3.3.10, page 40
 * 
 * The INTERVAL rule part contains a positive integer representing at
 * which intervals the recurrence rule repeats.  The default value is
 * "1", meaning every second for a SECONDLY rule, every minute for a
 * MINUTELY rule, every hour for an HOURLY rule, every day for a
 * DAILY rule, every week for a WEEKLY rule, every month for a
 * MONTHLY rule, and every year for a YEARLY rule.  For example,
 * within a DAILY rule, a value of "8" means every eight days.
 */
public class Interval extends RRulePartBase<Integer, Interval>
{
    public static final int DEFAULT_INTERVAL = 1;
    
    public Interval()
    {
        super();
        setValue(DEFAULT_INTERVAL);
    }
    
    public Interval(Integer interval)
    {
        this();
        setValue(interval);
    }

    public Interval(Interval source)
    {
        this();
        setValue(source.getValue());
    }

    @Override
    protected List<Message> parseContent(String content)
    {
    	String valueString = extractValue(content);
        setValue(Integer.parseInt(valueString));
        return Collections.EMPTY_LIST;
    }
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getValue() == null) ? 0 : getValue().hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Interval other = (Interval) obj;
        
        return (getValue() == null) ? other.getValue() == null : getValue().equals(other.getValue());
    }
    
    @Override
    public List<String> errors()
    {
        List<String> errors = super.errors();
        if (getValue() != null && getValue() < 1)
        {
            errors.add("INTERVAL is " + getValue() + ".  The value MUST be greater than or equal to 1.");
        }
        return errors;
    }
    
    public static Interval parse(String content)
    {
    	return Interval.parse(new Interval(), content);
    }
}
