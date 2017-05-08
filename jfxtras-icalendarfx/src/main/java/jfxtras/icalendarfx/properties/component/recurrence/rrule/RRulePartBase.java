package jfxtras.icalendarfx.properties.component.recurrence.rrule;

import java.util.ArrayList;
import java.util.List;

import jfxtras.icalendarfx.VElementBase;
import jfxtras.icalendarfx.VParent;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.RRuleElement;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.RRulePart;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.RRulePartBase;

abstract public class RRulePartBase<T, U> extends VElementBase implements RRulePart<T>
{
    private VParent myParent;
    @Override public void setParent(VParent parent) { myParent = parent; }
    @Override public VParent getParent() { return myParent; }
    
    /*
     * Recurrence Rule element value
     * For example, FREQ=DAILY the value is DAILY
     * 
     */
    @Override
    public T getValue() { return value; }
    private T value;
    @Override
    public void setValue(T value) { this.value = value; }
    public U withValue(T value)
    {
    	setValue(value);
    	return (U) this;
	}
    
    final protected RRuleElement elementType;
    @Override
    public String name()
    {
    	return elementType.toString();
  	}
    
    /*
     * CONSTRUCTORS
     */
    protected RRulePartBase()
    {
        elementType = RRuleElement.fromClass(getClass());
    }
    
    @Override
    public List<String> errors()
    {
        List<String> errors = new ArrayList<>();
        if (getValue() == null)
        {
            errors.add(name() + ": value is null.  The RRULE part MUST have a value."); 
        }
        return errors;
    }
    
    @Override
    public String toString()
    {
        return RRuleElement.fromClass(getClass()).toString() + "=" + getValue().toString();
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
        RRulePartBase<?,?> other = (RRulePartBase) obj;
        if (getValue() == null)
        {
            if (other.getValue() != null)
                return false;
        } else if (!getValue().equals(other.getValue()))
            return false;
        return true;
    }
    
	/*
	 * Get value from a name-value pair separated by an equal sign
	 */
    protected static String extractValue(String content)
    {
        int equalsIndex = content.indexOf('=');
        final String valueString;
        if (equalsIndex > 0)
        {
            String name = content.substring(0, equalsIndex);
            boolean hasName1 = RRuleElement.fromName(name.toUpperCase()) != null;
//            boolean hasName2 = (IANAParameter.getRegisteredIANAParameters() != null) ? IANAParameter.getRegisteredIANAParameters().contains(name.toUpperCase()) : false;
            valueString = (hasName1) ? content.substring(equalsIndex+1) : content;    
        } else
        {
            valueString = content;
        }
        return valueString;
    }
}
