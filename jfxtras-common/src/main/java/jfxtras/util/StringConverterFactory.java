/**
 * StringConverterFactory.java
 *
 * Copyright (c) 2011-2015, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the organization nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package jfxtras.util;

import javafx.util.StringConverter;

/**
 * A central place to get basic StringConverters from.
 * 
 * @author Tom Eugelink
 *
 */
public class StringConverterFactory
{
	/**
	 * 
	 * @return A default string converter that does no extra processing on the strings it is given.
	 */
	static public StringConverter<String> forString()
	{
		return new StringConverter<String>()
		{
			@Override
			public String fromString(String string)
			{
				return string;
			}

			@Override
			public String toString(String value)
			{
				return value;
			}
		};
	}
	
	/**
	 * 
	 * @return A default string converter that does no extra processing on the strings it is given.
	 */
	static public StringConverter<Integer> forInteger()
	{
		return new StringConverter<Integer>()
		{
			@Override
			public Integer fromString(String string)
			{
				return Integer.valueOf(string);
			}

			@Override
			public String toString(Integer value)
			{
				return "" + value;
			}
		};
	}
}
