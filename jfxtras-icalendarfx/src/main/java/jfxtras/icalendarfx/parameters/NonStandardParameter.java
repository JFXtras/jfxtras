package jfxtras.icalendarfx.parameters;

import java.util.List;

/**
 * A non-standard, experimental parameter.
 * 
 * @author David Bal
 *
 */
public class NonStandardParameter extends ParameterBase<NonStandardParameter, String>
{
    final String name;
    @Override
    public String name() { return name; }
    
    public NonStandardParameter(String content)
    {
        super();
        int equalsIndex = content.indexOf('=');
        name = (equalsIndex >= 0) ? content.substring(0, equalsIndex) : content;
        String value = (equalsIndex >= 0) ? content.substring(equalsIndex+1) : null;
        setValue(value);
    }

    public NonStandardParameter(NonStandardParameter source)
    {
        super(source);
        this.name = source.name;
    }

    public static NonStandardParameter parse(String content)
    {
        return new NonStandardParameter(content);
    }
    
    @Override
    public List<String> errors()
    {
        List<String> errors = super.errors();
        if (name() != null && ! name().substring(0, 2).equals("X-"))
        {
            errors.add(name() + " is not a proper non-standard parameter name.  It must begin with X-");
        }
        return errors;
    }
    
    @Override
    public String toContent()
    {
        return (getValue() != null) ? name() + "=" + getValue() : null;
    }
}
