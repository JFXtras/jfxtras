package jfxtras.icalendarfx.properties.component.recurrence.rrule;

import java.util.Collections;
import java.util.List;

import jfxtras.icalendarfx.properties.component.recurrence.rrule.Count;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.RRulePartBase;

/**
 * COUNT:
 * RFC 5545 iCalendar 3.3.10, page 41
 * 
 * The COUNT rule part defines the number of occurrences at which to
 * range-bound the recurrence.  The "DTSTART" property value always
 * counts as the first occurrence.
 */
public class Count extends RRulePartBase<Integer, Count>
{
	@Override
	public void setValue(Integer value)
	{
        if ((value != null) && (value < 1))
        {
            throw new IllegalArgumentException(name() + " is " + value + ".  It can't be less than 1");
        }
        super.setValue(value);	
	}
	
	/*
	 * CONSTRUCTORS
	 */
    public Count(int count)
    {
        this();
        setValue(count);
    }

    public Count()
    {
        super();
    }

    public Count(Count source)
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

    public static Count parse(String content)
    {
    	return Count.parse(new Count(), content);
    }
}
