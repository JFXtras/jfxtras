package jfxtras.icalendarfx.components;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import jfxtras.icalendarfx.components.DaylightSavingTime;
import jfxtras.icalendarfx.components.StandardTime;
import jfxtras.icalendarfx.components.VPrimary;
import jfxtras.icalendarfx.components.VRepeatable;
import jfxtras.icalendarfx.components.VRepeatableBase;
import jfxtras.icalendarfx.properties.component.recurrence.PropertyBaseRecurrence;
import jfxtras.icalendarfx.properties.component.recurrence.RecurrenceDates;
import jfxtras.icalendarfx.properties.component.recurrence.RecurrenceRule;
import jfxtras.icalendarfx.properties.component.recurrence.RecurrenceRuleCache;
import jfxtras.icalendarfx.properties.component.time.DateTimeStart;
import jfxtras.icalendarfx.utilities.DateTimeUtilities;
import jfxtras.icalendarfx.utilities.DateTimeUtilities.DateTimeType;
/**
 * Contains following properties:
 * @see RecurrenceRule
 * @see RecurrenceDates
 * 
 * @author David Bal
 *
 * @param <T> - concrete subclass
 * @see DaylightSavingTime
 * @see StandardTime
 */
public abstract class VRepeatableBase<T> extends VPrimary<T> implements VRepeatable<T>
{
    /**
     * RDATE
     * Recurrence Date-Times
     * RFC 5545 iCalendar 3.8.5.2, page 120.
     * 
     * This property defines the list of DATE-TIME values for
     * recurring events, to-dos, journal entries, or time zone definitions.
     * 
     * NOTE: DOESN'T CURRENTLY SUPPORT PERIOD VALUE TYPE
     * */
    @Override
    public List<RecurrenceDates> getRecurrenceDates() { return recurrenceDates; }
    private List<RecurrenceDates> recurrenceDates;
    @Override
    public void setRecurrenceDates(List<RecurrenceDates> recurrenceDates)
    {
    	if (this.recurrenceDates != null)
    	{
    		this.recurrenceDates.forEach(e -> orderChild(e, null)); // remove old elements
    	}
    	this.recurrenceDates = recurrenceDates;
    	if (recurrenceDates != null)
    	{
    		recurrenceDates.forEach(c -> orderChild(c)); // order new elements
    	}
	}

    /**
     * RRULE, Recurrence Rule
     * RFC 5545 iCalendar 3.8.5.3, page 122.
     * This property defines a rule or repeating pattern for recurring events, 
     * to-dos, journal entries, or time zone definitions
     * If component is not repeating the value is null.
     * 
     * Examples:
     * RRULE:FREQ=DAILY;COUNT=10
     * RRULE:FREQ=WEEKLY;UNTIL=19971007T000000Z;WKST=SU;BYDAY=TU,TH
     */
    @Override
    public RecurrenceRule getRecurrenceRule() { return recurrenceRule; }
    private RecurrenceRule recurrenceRule;
	@Override
	public void setRecurrenceRule(RecurrenceRule recurrenceRule)
	{
		orderChild(this.recurrenceRule, recurrenceRule);
		this.recurrenceRule = recurrenceRule;
	}

    /*
     * CONSTRUCTORS
     */
    VRepeatableBase() { }
    
    public VRepeatableBase(VRepeatableBase<T> source)
    {
        super(source);
    }

    @Override
    public Stream<Temporal> streamRecurrences(Temporal start)
    {
        Stream<Temporal> inStream = VRepeatable.super.streamRecurrences(start);
        if (getRecurrenceRule() == null)
        {
            return inStream; // no cache is no recurrence rule
        }
        return recurrenceCache().makeCache(inStream);   // make cache of start date/times
    }
    
    @Override
    public List<String> errors()
    {
    	List<String> errors = super.errors();
    	errors.addAll(errorsRepeatable(this));
    	errors.addAll(errorsRecurrence(getRecurrenceDates(), getDateTimeStart()));
        return errors;
    }
    
    protected static List<String> errorsRepeatable(VRepeatable<?> testObj)
    {
        List<String> errors = new ArrayList<>();
        String recurrenceDateError = testObj.checkRecurrencesConsistency(testObj.getRecurrenceDates());
        if (recurrenceDateError != null) errors.add(recurrenceDateError);

        if (testObj.getRecurrenceRule() != null && testObj.getRecurrenceRule().getValue().getUntil() != null)
        {
            Temporal until = testObj.getRecurrenceRule().getValue().getUntil().getValue();
            DateTimeType untilType = DateTimeType.of(until);
            DateTimeType startType = DateTimeType.of(testObj.getDateTimeStart().getValue());
            switch (startType)
            {
            case DATE:
                if (untilType != DateTimeType.DATE)
                {
                    errors.add("If DTSTART specifies a DATE then UNTIL must also specify a DATE value instead of:" + untilType);
                }
                break;
            case DATE_WITH_LOCAL_TIME:
            case DATE_WITH_LOCAL_TIME_AND_TIME_ZONE:
            case DATE_WITH_UTC_TIME:
                if (untilType != DateTimeType.DATE_WITH_UTC_TIME)
                {
                    errors.add("If DTSTART specifies a DATE_WITH_LOCAL_TIME, DATE_WITH_LOCAL_TIME_AND_TIME_ZONE or DATE_WITH_UTC_TIME then UNTIL must specify a DATE_WITH_UTC_TIME value instead of:" + untilType);
                }
                break;
            default:
                throw new RuntimeException("unsupported DateTimeType:" + startType);
            }
        }
        return errors;
    }
    
    protected static List<String> errorsRecurrence(List<? extends PropertyBaseRecurrence<?>> recurrenceDates, DateTimeStart dtstart)
    {
    	List<String> errors = new ArrayList<>();
    	
    	// error check - all Temporal types must be same
    	if ((recurrenceDates != null) && (! recurrenceDates.isEmpty()))
		{
    		PropertyBaseRecurrence<?> sample = recurrenceDates.get(0);
        	Temporal sampleTemporal = recurrenceDates.stream()
            		.flatMap(r -> r.getValue().stream())
            		.findAny()
            		.get();
    		DateTimeType sampleType = DateTimeUtilities.DateTimeType.of(sampleTemporal);
        	Optional<String> error1 = recurrenceDates
        		.stream()
        		.flatMap(r -> r.getValue().stream())
	        	.map(v ->
	        	{
	        		DateTimeType recurrenceType = DateTimeUtilities.DateTimeType.of(v);
	        		if (! recurrenceType.equals(sampleType))
	        		{
	                    return sample.name() + ": DateTimeType " + recurrenceType +
	                            " doesn't match previous recurrence's DateTimeType " + sampleType;            
	        		}
	        		return null;
	        	})
	        	.filter(s -> s != null)
	        	.findAny();
        	
        	if (error1.isPresent())
        	{
        		errors.add(error1.get());
        	} else
        	{ // don't check ZoneID if some types don't match - can cause ClassCastException otherwise
	            // ensure all ZoneId values are the same
	            if (sampleTemporal instanceof ZonedDateTime)
	            {
	                ZoneId zone = ((ZonedDateTime) sampleTemporal).getZone();
	                boolean allZonesIdentical = recurrenceDates
	                        .stream()
	                        .flatMap(r -> r.getValue().stream())
	                        .map(t -> ((ZonedDateTime) t).getZone())
	                        .allMatch(z -> z.equals(zone));
	                if (! allZonesIdentical)
	                {
	                	errors.add("ZoneId are not all identical");
	                }
	            }
        	}
            
        	// DTSTART check
        	if (dtstart != null)
        	{
	            DateTimeType dateTimeStartType = DateTimeUtilities.DateTimeType.of(dtstart.getValue());
	            if (sampleType != dateTimeStartType)
	            {
	                errors.add("DTSTART, " + sample.name() + ": The value type of " + sample.name() + 
	    	            	" elements MUST be the same as the DTSTART property (DTSTART=" + 
	    	            	dateTimeStartType + ", " + sample.name() + "=" + sampleType);
	            }
        	}
        }
        return errors;
    }

    /*
     *  RECURRENCE STREAMER
     *  produces recurrence set
     */
    private RecurrenceRuleCache streamer = new RecurrenceRuleCache(this);
    @Override
    public RecurrenceRuleCache recurrenceCache() { return streamer; }
    
}
