package jfxtras.icalendarfx.parameters;

import java.util.Collections;
import java.util.List;

import jfxtras.icalendarfx.parameters.NonStandardParameter;
import jfxtras.icalendarfx.parameters.VParameterBase;
import jfxtras.icalendarfx.utilities.StringConverter;
import jfxtras.icalendarfx.utilities.StringConverters;

/**
 * A non-standard, experimental parameter.
 * 
 * @author David Bal
 *
 */
public class NonStandardParameter extends VParameterBase<NonStandardParameter, String>
{
	private static final StringConverter<String> CONVERTER = StringConverters.defaultStringConverterWithQuotes();

    String name;
    @Override
    public String name() { return name; }
    
    /*
     * CONSTRUCTORS
     */
    public NonStandardParameter(String content)
    {
        super(CONVERTER);
        construct(content);
    }

	private void construct(String content) {
		int equalsIndex = content.indexOf('=');
        name = (equalsIndex >= 0) ? content.substring(0, equalsIndex) : content;
        String value = (equalsIndex >= 0) ? content.substring(equalsIndex+1) : null;
        setValue(value);
	}

    public NonStandardParameter(NonStandardParameter source)
    {
        super(source, CONVERTER);
        this.name = source.name;
    }

    public NonStandardParameter()
    {
    	super(CONVERTER);
	}

    @Override
	protected List<Message> parseContent(String content)
    {
    	construct(content);
    	return Collections.EMPTY_LIST;
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
    public String toString()
    {
        return (getValue() != null) ? name() + "=" + getValue() : null;
    }
    
    public static NonStandardParameter parse(String content)
    {
    	return NonStandardParameter.parse(new NonStandardParameter(), content);
    }
}
