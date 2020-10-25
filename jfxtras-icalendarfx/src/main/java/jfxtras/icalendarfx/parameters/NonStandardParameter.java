/**
 * Copyright (c) 2011-2020, JFXtras
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *    Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *    Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *    Neither the name of the organization nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL JFXRAS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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
