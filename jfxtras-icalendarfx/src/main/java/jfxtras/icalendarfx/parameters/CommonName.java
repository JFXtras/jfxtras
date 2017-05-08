package jfxtras.icalendarfx.parameters;

import jfxtras.icalendarfx.parameters.CommonName;
import jfxtras.icalendarfx.parameters.VParameterBase;
import jfxtras.icalendarfx.utilities.StringConverter;
import jfxtras.icalendarfx.utilities.StringConverters;

/**
 * CN
 * Common Name
 * RFC 5545, 3.2.2, page 15
 * 
 * To specify the common name to be associated with the calendar user specified by the property.
 * 
 * Example:
 * ORGANIZER;CN="John Smith":mailto:jsmith@example.com
 * 
 * @author David Bal
 */
public class CommonName extends VParameterBase<CommonName, String>
{
	private static final StringConverter<String> CONVERTER = StringConverters.defaultStringConverterWithQuotes();
	
    public CommonName()
    {
        super(CONVERTER);
    }

    public CommonName(CommonName source)
    {
        super(source, CONVERTER);
    }
    
    public static CommonName parse(String content)
    {
    	return CommonName.parse(new CommonName(), content);
    }
}
