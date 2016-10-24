package jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx;

import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.RRuleElementBase;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.RRuleElementType;

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
public abstract class ByRuleAbstract<T, U> extends RRuleElementBase<ObservableList<T>, U> implements ByRule<ObservableList<T>>
{
    @Override
    public void setValue(ObservableList<T> values)
    {
        super.setValue(values);
    }
    public void setValue(T... values)
    {
        setValue(FXCollections.observableArrayList(values));
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
        setValue(FXCollections.observableArrayList());
    }

    ByRuleAbstract(T... values)
    {
        setValue(values);
    }
    
    // Copy constructor
    ByRuleAbstract(ByRuleAbstract<T, U> source)
    {
        setValue(FXCollections.observableArrayList(source.getValue()));
    }

    @Override
    public List<String> errors()
    {
        List<String> errors = new ArrayList<>();
        if (getValue() == null)
        {
            errors.add(elementType() + " value is null.  The element MUST have a value."); 
        } else if (getValue().isEmpty())
        {
            errors.add(elementType() + " value list is empty.  List MUST have at lease one element."); 
        }

        return errors;
    }
    
    @Override
    public int compareTo(ByRule<ObservableList<T>> byRule)
    {
        int p1 = RRuleElementType.enumFromClass(getClass()).sortOrder();
        int p2 = RRuleElementType.enumFromClass(byRule.getClass()).sortOrder();
        return Integer.compare(p1, p2);
    }
    
    @Override
    public String toString()
    {
        return super.toString() + ", " + toContent();
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) return true;
        if((obj == null) || (obj.getClass() != getClass())) {
            return false;
        }
        ByRuleAbstract<T, U> testObj = (ByRuleAbstract<T, U>) obj;
        boolean valueEquals = getValue().equals(testObj.getValue());
        return valueEquals;
    }
    
    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = (31 * hash) + getValue().hashCode();
        return hash;
    }

}
