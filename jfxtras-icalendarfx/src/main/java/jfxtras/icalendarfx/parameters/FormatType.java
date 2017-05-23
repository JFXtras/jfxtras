package jfxtras.icalendarfx.parameters;

import jfxtras.icalendarfx.parameters.FormatType;
import jfxtras.icalendarfx.parameters.VParameterBase;
import jfxtras.icalendarfx.properties.component.descriptive.Attachment;
import jfxtras.icalendarfx.utilities.StringConverter;
import jfxtras.icalendarfx.utilities.StringConverters;

/**
 * Format Type
 * FMTYPE
 * RFC 5545 iCalendar 3.2.8 page 19
 * 
 * To specify the content type of a referenced object.
 * 
 *  Example:
 *  ATTACH;FMTTYPE=application/msword:ftp://example.com/pub/docs/
 *   agenda.doc
 *   
 *   @see Attachment
 */
public class FormatType extends VParameterBase<FormatType, String>
{
	private static final StringConverter<String> CONVERTER = StringConverters.defaultStringConverterWithQuotes();

    public String getTypeName() { return typeName; }
    private String typeName;
    public void setTypeName(String typeName)
    {
    	this.typeName = typeName;
    	buildNewValue();
	}

    public String getSubtypeName() { return subtypeName; }
    private String subtypeName;
    public void setSubtypeName(String subtypeName)
    {
    	this.subtypeName = subtypeName;
    	buildNewValue();
	}

    // capture type and subtype names
    @Override
    public void setValue(String value)
    {
        int slashIndex = value.indexOf('/');
        if (slashIndex > 0)
        {
            setTypeName(value.substring(0, slashIndex));
            setSubtypeName(value.substring(slashIndex+1));
            super.setValue(value);            
        } else
        {
            throw new IllegalArgumentException(getClass().getSimpleName() + " requires both type-name / subtype-name as defined in RFC4288");
        }
    }
    
	private void buildNewValue()
    {
		if ((getTypeName() != null) && (getSubtypeName() != null))
		{
			String newValue = getTypeName() + "/" + getSubtypeName();
			super.setValue(newValue);
		}
    }

    /*
     * CONSTRUCTORS
     */  
    public FormatType()
    {
        super(CONVERTER);
    }

    public FormatType(FormatType source)
    {
        super(source, CONVERTER);
    }
    
    public static FormatType parse(String content)
    {
    	return FormatType.parse(new FormatType(), content);
    }
}
