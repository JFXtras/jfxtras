package jfxtras.icalendarfx.parameters;

import jfxtras.icalendarfx.parameters.Language;
import jfxtras.icalendarfx.parameters.VParameterBase;
import jfxtras.icalendarfx.utilities.StringConverter;
import jfxtras.icalendarfx.utilities.StringConverters;

/**
 * LANGUAGE
 * Language
 * RFC 5545, 3.2.10, page 21
 * 
 * To specify the language for text values in a property or property parameter.
 * 
 * Example:
 * SUMMARY;LANGUAGE=en-US:Company Holiday Party
 * 
 * @author David Bal
 *
 */
public class Language extends VParameterBase<Language, String>
{
	private static final StringConverter<String> CONVERTER = StringConverters.defaultStringConverterWithQuotes();

    public Language()
    {
        super(CONVERTER);
    }

    public Language(Language source)
    {
        super(source, CONVERTER);
    }
    
    public static Language parse(String content)
    {
    	return Language.parse(new Language(), content);
    }
}
