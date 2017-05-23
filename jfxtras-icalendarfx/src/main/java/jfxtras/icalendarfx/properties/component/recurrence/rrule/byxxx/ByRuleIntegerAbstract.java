package jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import jfxtras.icalendarfx.properties.component.recurrence.rrule.RRuleElement;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByRuleAbstract;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByRuleIntegerAbstract;

public abstract class ByRuleIntegerAbstract<U> extends ByRuleAbstract<Integer, U>
{
    @Override
    public void setValue(List<Integer> values)
    {
        // validate values
        values.forEach(value -> 
        {
            if (! isValidValue().test(value))
            {
                throw new IllegalArgumentException("Out of range " + name().toString() + " value: " + value);
            }            
        });
        super.setValue(values);
    }
    /** predicate tests value range in listener attached to {@link #getValue()} 
     * Ensures added values are within allowed range */
    abstract Predicate<Integer> isValidValue();
    
    /*
     * CONSTRUCTORS
     */
    public ByRuleIntegerAbstract()
    {
        super();
        setValue(new ArrayList<>());
    }
    
    public ByRuleIntegerAbstract(Integer... values)
    {
        super(values);
    }
    
    public ByRuleIntegerAbstract(ByRuleIntegerAbstract<U> source)
    {
        super(source);
    }
        
    
    @Override
	public List<String> errors()
    {
    	List<String> errors = super.errors();
    	List<String> myErrors = getValue()
			.stream()
			.filter(v -> ! isValidValue().test(v))
			.map(v -> "Out of range " + name() + " value: " + v)
			.collect(Collectors.toList());
    	errors.addAll(myErrors);
    	return errors;
    }
    
	@Override
    public String toString()
    {
        String values = getValue().stream()
                .map(value -> value.toString())
                .collect(Collectors.joining(","));
        return RRuleElement.fromClass(getClass()).toString() + "=" + values;
    }
    
    @Override
    protected List<Message> parseContent(String content)
    {
    	String valueString = extractValue(content);
        Integer[] monthDayArray = Arrays.asList(valueString.split(","))
                .stream()
                .map(s -> Integer.parseInt(s))
                .toArray(size -> new Integer[size]);
        setValue(monthDayArray);
        return Collections.EMPTY_LIST;
    }
}
