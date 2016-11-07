package jfxtras.icalendarfx.components;

import java.time.DateTimeException;
import java.time.temporal.Temporal;
import java.util.List;
import java.util.stream.Stream;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import jfxtras.icalendarfx.properties.PropertyType;
import jfxtras.icalendarfx.properties.component.recurrence.RecurrenceDates;
import jfxtras.icalendarfx.properties.component.recurrence.RecurrenceRule;
import jfxtras.icalendarfx.properties.component.recurrence.RecurrenceRuleCache;
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
    public ObjectProperty<ObservableList<RecurrenceDates>> recurrenceDatesProperty()
    {
        if (recurrenceDates == null)
        {
            recurrenceDates = new SimpleObjectProperty<>(this, PropertyType.RECURRENCE_DATE_TIMES.toString());
        }
        return recurrenceDates;
    }
    @Override
    public ObservableList<RecurrenceDates> getRecurrenceDates()
    {
        return (recurrenceDates == null) ? null : recurrenceDates.get();
    }
    private ObjectProperty<ObservableList<RecurrenceDates>> recurrenceDates;
    @Override
    public void setRecurrenceDates(ObservableList<RecurrenceDates> recurrenceDates)
    {
        if (recurrenceDates != null)
        {
            orderer().registerSortOrderProperty(recurrenceDates);
            recurrenceDates.addListener(getRecurrencesConsistencyWithDateTimeStartListener());
            String error = checkRecurrencesConsistency(recurrenceDates);
            if (error != null) throw new DateTimeException(error);
        } else
        {
            orderer().unregisterSortOrderProperty(recurrenceDatesProperty().get());
        }
        recurrenceDatesProperty().set(recurrenceDates);
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
    @Override public ObjectProperty<RecurrenceRule> recurrenceRuleProperty()
    {
        if (recurrenceRule == null)
        {
            recurrenceRule = new SimpleObjectProperty<>(this, PropertyType.UNIQUE_IDENTIFIER.toString());
            orderer().registerSortOrderProperty(recurrenceRule);
        }
        return recurrenceRule;
    }
    @Override
    public RecurrenceRule getRecurrenceRule() { return (recurrenceRule == null) ? null : recurrenceRuleProperty().get(); }
    private ObjectProperty<RecurrenceRule> recurrenceRule;
    
    /*
     * CONSTRUCTORS
     */
    VRepeatableBase() { }
    
//    VComponentRepeatableBase(String contentLines)
//    {
//        super(contentLines);
//    }    
    
    public VRepeatableBase(StandardOrDaylight<T> source)
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
        return VRepeatable.errorsRepeatable(this);
    }

    /*
     *  RECURRENCE STREAMER
     *  produces recurrence set
     */
    private RecurrenceRuleCache streamer = new RecurrenceRuleCache(this);
    @Override
    public RecurrenceRuleCache recurrenceCache() { return streamer; }
    
}
