package jfxtras.icalendarfx.parameters;

import java.net.URI;

import jfxtras.icalendarfx.parameters.SentBy;
import jfxtras.icalendarfx.parameters.VParameterBase;
import jfxtras.icalendarfx.utilities.StringConverter;
import jfxtras.icalendarfx.utilities.StringConverters;

/**
 * SENT-BY
 * Sent By
 * RFC 5545, 3.2.18, page 27
 * 
 * To specify the calendar user that is acting on behalf of
 *  the calendar user specified by the property.
 * 
 * Example:
 * ORGANIZER;SENT-BY="mailto:sray@example.com":mailto:
 *  jsmith@example.com
 * 
 * @author David Bal
 *
 */
public class SentBy extends VParameterBase<SentBy, URI>
{
	private static final StringConverter<URI> CONVERTER = StringConverters.uriConverterWithQuotes();

    public SentBy(URI uri)
    {
        super(uri, CONVERTER);
    }

    public SentBy()
    {
        super(CONVERTER);
    }
    
    public SentBy(SentBy source)
    {
        super(source, CONVERTER);
    }
    
    public static SentBy parse(String content)
    {
    	return SentBy.parse(new SentBy(), content);
    }
}
