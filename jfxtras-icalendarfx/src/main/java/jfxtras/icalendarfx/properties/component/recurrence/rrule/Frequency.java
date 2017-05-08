package jfxtras.icalendarfx.properties.component.recurrence.rrule;

import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import jfxtras.icalendarfx.properties.component.recurrence.rrule.Frequency;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.FrequencyType;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.RRulePartBase;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.RecurrenceRuleValue;

/**
 * FREQUENCY
 * FREQ
 * RFC 5545 iCalendar 3.3.10 p40
 * 
 * The FREQ rule part identifies the type of recurrence rule.  This
 * rule part MUST be specified in the recurrence rule.  Valid values
 * include SECONDLY, to specify repeating events based on an interval
 * of a second or more; MINUTELY, to specify repeating events based
 * on an interval of a minute or more; HOURLY, to specify repeating
 * events based on an interval of an hour or more; DAILY, to specify
 * repeating events based on an interval of a day or more; WEEKLY, to
 * specify repeating events based on an interval of a week or more;
 * MONTHLY, to specify repeating events based on an interval of a
 * month or more; and YEARLY, to specify repeating events based on an
 * interval of a year or more.
 * 
 * Frequency value.  Possible values include:
 *  <br>
 * {@link FrequencyType#SECONDLY } <br>
 * {@link FrequencyType#MINUTELY } <br>
 * {@link FrequencyType#HOURLY } <br>
 * {@link FrequencyType#DAILY } <br>
 * {@link FrequencyType#WEEKLY } <br>
 * {@link FrequencyType#MONTHLY } <br>
 * {@link FrequencyType#YEARLY }
 * 
 * @author David Bal
 * 
 * @see FrequencyType
 */
public class Frequency extends RRulePartBase<FrequencyType, Frequency>
{
    void setValue(String frequency) { parseContent(frequency); }
    public Frequency withValue(String frequency) { setValue(frequency); return this; }

    /** Time unit of last rule applied.  It represents the time span to apply future changes to the output stream of date/times
     * For example:
     * 
     * following FREQ=WEEKLY it is WEEKS
     * following FREQ=YEARLY it is YEARS
     * following FREQ=YEARLY;BYWEEKNO=20 it is WEEKS
     * following FREQ=YEARLY;BYMONTH=3 it is MONTHS
     * following FREQ=YEARLY;BYMONTH=3;BYDAY=TH it is DAYS
     * 
     * Note: ChronoUnit is wrapped in an ObjectProperty to enable receiving classes to have the
     * reference to the object and make changes to it.  If I passed a ChronoUnit object, which is an enum,
     * changes are not propagated back.  In that case, I would need a reference to the Frequency object that owns
     * it.  The ObjectProperty wrapper is easier.
     */
    
    /** TemporalAdjuster to enable frequency stream in {@link RecurrenceRuleValue#streamRecurrences(Temporal)} */
    TemporalAdjuster adjuster(int interval)
    {
        return (temporal) -> temporal.plus(interval, getValue().getChronoUnit());
    }
    
    /*
     * CONSTRUCTORS
     */
    public Frequency(FrequencyType frequencyType)
    {
        this();
        setValue(frequencyType);
    }
    
    public Frequency()
    {
        super();
    }
    
    public Frequency(Frequency source)
    {
        this();
        setValue(source.getValue());
    }
    
    /** STREAM 
     * Resulting stream of start date/times by applying Frequency temporal adjuster and all, if any,
     * Rules.
     * Starts on startDateTime, which MUST be a valid occurrence date/time, but not necessarily the
     * first date/time (DTSTART) in the sequence. A later startDateTime can be used to more efficiently
     * get to later dates in the stream.
     * 
     * @param start - starting point of stream (MUST be a valid occurrence date/time)
     * @return
     */
    public Stream<Temporal> streamRecurrences(Temporal start, int interval)
    {
        TemporalAdjuster adjuster = (temporal) -> temporal.plus(interval, getValue().getChronoUnit());
        return Stream.iterate(start, a -> a.with(adjuster));
    }

    @Override
    protected List<Message> parseContent(String content)
    {
    	String valueString = extractValue(content);
        setValue(FrequencyType.valueOf(valueString.toUpperCase()));
        return Collections.EMPTY_LIST;
    }
    
    public static Frequency parse(String content)
    {
    	return Frequency.parse(new Frequency(), content);
    }
}
