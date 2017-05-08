package jfxtras.icalendarfx.properties.component.recurrence.rrule;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import jfxtras.icalendarfx.VElement;
import jfxtras.icalendarfx.parameters.VParameterElement;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.Count;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.Frequency;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.Interval;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.RRuleElement;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.RRulePart;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.Until;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.WeekStart;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByHour;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByMinute;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByMonth;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByMonthDay;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.BySecond;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.BySetPosition;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByWeekNumber;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByYearDay;

public enum RRuleElement
{
    FREQUENCY ("FREQ", Frequency.class, null),
    INTERVAL ("INTERVAL", Interval.class, null),
    UNTIL ("UNTIL", Until.class, null),
    COUNT ("COUNT", Count.class, null),
    WEEK_START ("WKST", WeekStart.class, null),
    BY_MONTH ("BYMONTH", ByMonth.class, ChronoUnit.MONTHS),
    BY_WEEK_NUMBER ("BYWEEKNO", ByWeekNumber.class, ChronoUnit.DAYS),
    BY_YEAR_DAY ("BYYEARDAY", ByYearDay.class, ChronoUnit.DAYS),
    BY_MONTH_DAY ("BYMONTHDAY", ByMonthDay.class, ChronoUnit.DAYS),
    BY_DAY ("BYDAY", ByDay.class, ChronoUnit.DAYS),
    BY_HOUR ("BYHOUR", ByHour.class, ChronoUnit.HOURS),
    BY_MINUTE ("BYMINUTE", ByMinute.class, ChronoUnit.MINUTES),
    BY_SECOND ("BYSECOND", BySecond.class, ChronoUnit.SECONDS),
    BY_SET_POSITION ("BYSETPOS", BySetPosition.class, null)
    ;
    
    // Map to match up name to enum
    private static final Map<String, RRuleElement> NAME_MAP = Arrays.stream(values())
    		.collect(Collectors.toMap(
    				v -> v.toString(),
    				v -> v));
    public static RRuleElement fromName(String propertyName)
    {
        return NAME_MAP.get(propertyName.toUpperCase());
    }
    
    // Map to match up class to enum
    private static final Map<Class<? extends RRulePart<?>>, RRuleElement> CLASS_MAP = Arrays.stream(values())
    		.collect(Collectors.toMap(
    				v -> v.elementClass(),
    				v -> v));
    /** get enum from map */
    public static RRuleElement fromClass(Class<? extends VElement> myClass)
    {
        RRuleElement p = CLASS_MAP.get(myClass);
        if (p == null)
        {
            throw new IllegalArgumentException(VParameterElement.class.getSimpleName() + " does not contain an enum to match the class:" + myClass.getSimpleName());
        }
        return p;
    }
    
    private String name;
    @Override
    public String toString() { return name; }
    
    private Class<? extends RRulePart<?>> myClass;
	public Class<? extends RRulePart<?>> elementClass() { return myClass; }
    
    private ChronoUnit chronoUnit;
    public ChronoUnit getChronoUnit() { return chronoUnit; }

    RRuleElement(String name, Class<? extends RRulePart<?>> myClass, ChronoUnit chronoUnit)
    {
        this.name = name;
        this.myClass = myClass;
        this.chronoUnit = chronoUnit;
    }
}
