package jfxtras.icalendarfx.parameters;

import java.util.Collections;
import java.util.List;

import jfxtras.icalendarfx.parameters.ParameterEnumBasedWithUnknown;
import jfxtras.icalendarfx.parameters.VParameterBase;
import jfxtras.icalendarfx.utilities.StringConverter;

public abstract class ParameterEnumBasedWithUnknown<U,T> extends VParameterBase<U,T>
{
    private String nonStandardValue; // contains exact string for unknown value
    
    /*
     * CONSTRUCTORS
     */
    public ParameterEnumBasedWithUnknown(StringConverter<T> stringConverter)
    {
        super(stringConverter);
    }
  
    public ParameterEnumBasedWithUnknown(T value, StringConverter<T> stringConverter) 
    {
        this(stringConverter);
        setValue(value);
    }
    
    public ParameterEnumBasedWithUnknown(ParameterEnumBasedWithUnknown<U,T> source, StringConverter<T> stringConverter)
    {
        super(source, stringConverter);
        nonStandardValue = source.nonStandardValue;
    }
        
    @Override
    String valueAsString()
    {
        return (getValue().toString().equals("UNKNOWN")) ? nonStandardValue : super.valueAsString();
    }
    
    @Override
    protected List<Message> parseContent(String content)
    {
        super.parseContent(content);
        if (getValue().toString().equals("UNKNOWN"))
        {
            String valueString = VParameterBase.extractValue(content);
            nonStandardValue = valueString;
        }
        return Collections.EMPTY_LIST;
    }
}
